/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.apache.dubbo.common.threadlocal;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The internal data structure that stores the threadLocal variables for Netty and all {@link InternalThread}s.
 * Note that this class is for internal use only. Use {@link InternalThread}
 * unless you know what you are doing.
 * 分为fast和slow两种模式，如果是fast的话，要求是当前线程必须是{@link InternalThread},并且设置的缓存对象也是维护在{@code InternalThread.InternalThreadLocalMap中}
 * 如果是slow的话，直接委托给{@code ThreadLocal}处理
 */
public final class InternalThreadLocalMap {

    /**
     * InternalThreadMap的实际容器，默认大小是32
     * 并且，无论以后如何扩容，在执行了InternalThreadLocal#set方法之后，这个容器的index=0的位置，就是一个{@code Set<InternalThreadLocal<?>>}
     * 这个set保存了待remove的本地变量，对应{@link ThreadLocal#remove()}方法作用
     * 所以说，每个{@link InternalThread#threadLocalMap} 其内部数组的index=0元素，都维护了当前待移除的{@link InternalThread}的变量值
     */
    private Object[] indexedVariables;

    private static ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = new ThreadLocal<InternalThreadLocalMap>();

    private static final AtomicInteger nextIndex = new AtomicInteger();

    public static final Object UNSET = new Object();

    public static InternalThreadLocalMap getIfSet() {
        Thread thread = Thread.currentThread();
        if (thread instanceof InternalThread) {
            return ((InternalThread) thread).threadLocalMap();
        }
        return slowThreadLocalMap.get();
    }

    /**
     * 根据当前线程的具体类型是{@link Thread}还是{@link InternalThread}来判断
     * @return
     */
    public static InternalThreadLocalMap get() {
        Thread thread = Thread.currentThread();
        if (thread instanceof InternalThread) {
            return fastGet((InternalThread) thread);// 如果InternalThread内部的InternalThreadLocalMap为null的话，设置然后返回
        }
        return slowGet();
    }

    public static void remove() {
        Thread thread = Thread.currentThread();
        if (thread instanceof InternalThread) {
            ((InternalThread) thread).setThreadLocalMap(null);
        } else {
            slowThreadLocalMap.remove();
        }
    }

    public static void destroy() {
        slowThreadLocalMap = null;
    }

    public static int nextVariableIndex() {
        int index = nextIndex.getAndIncrement();
        if (index < 0) {
            nextIndex.decrementAndGet();
            throw new IllegalStateException("Too many thread-local indexed variables");
        }
        return index;
    }

    public static int lastVariableIndex() {
        return nextIndex.get() - 1;
    }

    private InternalThreadLocalMap() {
        indexedVariables = newIndexedVariableTable();
    }

    public Object indexedVariable(int index) {
        Object[] lookup = indexedVariables;
        return index < lookup.length ? lookup[index] : UNSET;
    }

    /**
     * @return {@code true} if and only if a new thread-local variable has been created
     */
    public boolean setIndexedVariable(int index, Object value) {
        Object[] lookup = indexedVariables;
        if (index < lookup.length) {
            Object oldValue = lookup[index];
            lookup[index] = value;
            return oldValue == UNSET;
        } else {
            // 扩容并且设置
            expandIndexedVariableTableAndSet(index, value);
            return true;
        }
    }

    public Object removeIndexedVariable(int index) {
        Object[] lookup = indexedVariables;
        if (index < lookup.length) {
            Object v = lookup[index];
            lookup[index] = UNSET;
            return v;
        } else {
            return UNSET;
        }
    }

    public int size() {
        int count = 0;
        for (Object o : indexedVariables) {
            if (o != UNSET) {
                ++count;
            }
        }

        //the fist element in `indexedVariables` is a set to keep all the InternalThreadLocal to remove
        //look at method `addToVariablesToRemove`
        return count - 1;
    }

    private static Object[] newIndexedVariableTable() {
        Object[] array = new Object[32];
        Arrays.fill(array, UNSET);
        return array;
    }

    private static InternalThreadLocalMap fastGet(InternalThread thread) {
        // InternalThread.InternalThreadLocalMap对象只有通过InternalThread#setThreadLocalMap方法才可以赋值
        InternalThreadLocalMap threadLocalMap = thread.threadLocalMap();
        if (threadLocalMap == null) {
            thread.setThreadLocalMap(threadLocalMap = new InternalThreadLocalMap());// 默认大小是32
        }
        return threadLocalMap;
    }

    private static InternalThreadLocalMap slowGet() {
        ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = InternalThreadLocalMap.slowThreadLocalMap;
        InternalThreadLocalMap ret = slowThreadLocalMap.get();// 从本地ThreadLocal里面获取
        if (ret == null) {
            ret = new InternalThreadLocalMap();
            slowThreadLocalMap.set(ret);
        }
        return ret;
    }

    /**
     * 扩容，容量扩大至最接近index的并且大于index的2^n的整数
     * 如index=19，那么扩容后，结果为32
     * @param index
     * @param value
     */
    private void expandIndexedVariableTableAndSet(int index, Object value) {
        Object[] oldArray = indexedVariables;
        final int oldCapacity = oldArray.length;
        int newCapacity = index;
        newCapacity |= newCapacity >>> 1;
        newCapacity |= newCapacity >>> 2;
        newCapacity |= newCapacity >>> 4;
        newCapacity |= newCapacity >>> 8;
        newCapacity |= newCapacity >>> 16;
        newCapacity++;

        Object[] newArray = Arrays.copyOf(oldArray, newCapacity);
        Arrays.fill(newArray, oldCapacity, newArray.length, UNSET);
        newArray[index] = value;
        indexedVariables = newArray;
    }
}
