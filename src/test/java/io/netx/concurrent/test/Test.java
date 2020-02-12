package io.netx.concurrent.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) {
        BlockingQueue<Runnable> tasks = new ArrayBlockingQueue<>(100);
        tasks.offer(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行任务1");
                tasks.offer(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("任务1调用任务2");
                    }
                });
            }
        });
        ExecutorService executors = Executors.newSingleThreadExecutor();
        while (true) {
            Runnable task = tasks.poll();
            executors.execute(task);
        }
    }
}
