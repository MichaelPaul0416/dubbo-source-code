package org.apache.dubbo.rpc.demo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/10 15:52
 * @Description:
 **/
class NioWorker implements Runnable {

    // 持有一个任务队列
    private final ArrayBlockingQueue<Runnable> workTasks = new ArrayBlockingQueue<>(100);

    private volatile boolean shutdown = false;

    public void shutdown() {
        this.shutdown = true;
    }

    @Override
    public void run() {
        while (!shutdown) {
            try {
                Runnable task = workTasks.take();
                task.run();
            } catch (InterruptedException e) {
                System.out.println("获取执行任务异常，被中断");
            } catch (Throwable throwable) {
                System.out.println("NioWorker 执行异常 ：" + throwable.getMessage());
            }
        }
    }

    public boolean execute(Runnable runnable){
        return this.workTasks.offer(runnable);
    }
}
