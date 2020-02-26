package org.apache.dubbo.common.extensionloader.demo;

import org.apache.dubbo.common.threadlocal.InternalThread;
import org.apache.dubbo.common.threadlocal.InternalThreadLocal;
import org.apache.dubbo.common.threadlocal.InternalThreadLocalMap;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/26 13:42
 * @Description:
 **/
public class ByteMoveDemo {

    private int oldCapacity = 16;

    @Test
    public void expand() {
        // 获取比newIndex大的，最小的2^n整数
        int newIndex = 19;
        System.out.println(Integer.toBinaryString(20));

        newIndex |= newIndex >>> 1;
        newIndex |= newIndex >>> 2;
        newIndex |= newIndex >>> 4;
        newIndex |= newIndex >>> 8;
        newIndex |= newIndex >>> 16;
        newIndex++;
        System.out.println("new size:" + newIndex);
    }

    @Test
    public void internalThreadLocal() {
        InternalThreadLocal<String> itl = new InternalThreadLocal<>();
        InternalThread t1 = new InternalThread(() -> {
            System.out.println("thread-1 begin");
            /**
             * 当前线程是 {@link InternalThread}，所以使用了fast模式
             */
            itl.set("fast model value");

            System.out.println("internal Thread value:" + itl.get());

            // 这里虽然是两个InternalThreadLocal，但是他们实际i上都是在一个线程内部被set值，所以根本上
            // 值都是存在当前InternalThread实例中的InternalThreadLocalMap属性中
            // 所以itl和second这两个实例对象，在本线程中获取的InternalThreadLocalMap是同一个实例对象
            InternalThreadLocal<String> second = new InternalThreadLocal<>();

            second.set("second fast value");
            System.out.println("second Internal Thread value:" + second.get());
        });
        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        InternalThread t2 = new InternalThread(() -> {
            System.out.println("thread-2 begin");
            // 虽然这里操作的还是同一个InternalThreadLocal实例对象，但是本质上内部的存储数据结构已经变化
            // 因为最后的数据是存储在InternalThread实例内部的InternalThreadLocalMap属性中
            // 所以不同的线程去操作同一个InternalThreadLocal实例对象，会有不同的InternalThreadLocalMap实例
            itl.set("second fast model value");
            System.out.println(itl.get());

            System.out.println("thread-2 end");
        });

        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(reflectThreadForMap(t1) == reflectThreadForMap(t2));
    }

    /**
     * {@link InternalThreadLocalMap#indexedVariables}的是初始大小是32
     * 如果先创建36个{@link InternalThreadLocal}，那么第37个{@link InternalThreadLocal#index}=37
     * 超过32，此时再利用第37个{@link InternalThreadLocal#set} 看{@link InternalThreadLocalMap#indexedVariables.length}是否是64
     */
    @Test
    public void beyondInitSizeAndSetForInternalThreadLocal() {
        // 先将InternalThreadLocalMap#nextIndex[实际是一个AtomicInteger]增大
        for (int i = 0; i < 36; i++) {
            InternalThreadLocal<String> itl = new InternalThreadLocal<>();
        }

        InternalThreadLocal<String> finalItl = new InternalThreadLocal<>();
        InternalThread it = new InternalThread(() -> {
            finalItl.set("final InternalThread begin");
            // finalItl 的 index属性一定是37
            try {
                Field field = finalItl.getClass().getDeclaredField("index");
                field.setAccessible(true);

                int index = (int) field.get(finalItl);
                System.out.println("final InternalThreadLocal#index=" + index);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            InternalThreadLocalMap map = reflectThreadForMap((InternalThread) Thread.currentThread());

            try {
                Field field = map.getClass().getDeclaredField("indexedVariables");
                field.setAccessible(true);

                Object[] objs = (Object[]) field.get(map);
                System.out.println(objs.length);

                // InternalThreadLocalMap#indexedVariables数组的index=37的地方，value一定是之前set的值
                System.out.println(objs[37] == finalItl.get());
            } catch (Throwable e) {
                e.printStackTrace();
            }

            System.out.println(finalItl.get());

            System.out.println("final InternalThread end");
        });

        it.start();
        try {
            it.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private InternalThreadLocalMap reflectThreadForMap(InternalThread thread) {
        Class<InternalThread> clazz = InternalThread.class;
        Field field = null;
        try {
            field = clazz.getDeclaredField("threadLocalMap");
        } catch (NoSuchFieldException e) {
            //
        }

        field.setAccessible(true);
        try {
            InternalThreadLocalMap map = (InternalThreadLocalMap) field.get(thread);
            return map;
        } catch (IllegalAccessException e) {
            //
        }
        return null;
    }
}
