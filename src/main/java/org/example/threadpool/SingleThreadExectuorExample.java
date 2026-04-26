package org.example.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SingleThreadExectuorExample {

    public static void main(String[] args) {
        // SingleThreadExecutor creates a single worker thread to execute tasks sequentially.
        // If the thread is busy executing a task, additional tasks will wait in a queue until the thread is available.
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            executorService.submit(new Task(i));
        }
        // Shutdown the executor service gracefully
        executorService.shutdown();
    }
}


class Task implements Runnable{

    private int id;

    public Task(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Task "+id+" is running in thread "+Thread.currentThread().getName());
        long duration = (long) (Math.random() * 5);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}