package io.netx.concurrent.test;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test2 {
    public static void main(String[] args) {
        ExecutorService executors = Executors.newSingleThreadExecutor();
        executors.execute(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                System.out.println("I am alive : " + date.toString());
            }
        });
        executors.shutdown();
    }
}
