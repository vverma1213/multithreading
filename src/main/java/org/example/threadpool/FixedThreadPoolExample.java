package org.example.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FixedThreadPoolExample {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for(int i=0;i<=10;i++){
            executorService.submit(new FixedTask(i+1));
        }
        //shutdown the executor service
        executorService.shutdown();

    }
}

class FixedTask implements Runnable{

    private int id;

    public FixedTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Task "+id+" is running in thread "+Thread.currentThread().getId());
        long duration = (long) (Math.random() * 5);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
