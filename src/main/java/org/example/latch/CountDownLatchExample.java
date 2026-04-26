package org.example.latch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
    countdownlatch solves by decoupling task coordination from thread tracking.
    the main thread doesn't need to know or track threads at all.

    CountDownLatch is a synchronization aid that allows one or more threads to wait until a set of operations being performed
    in other threads completes. It is initialized with a count, and the await() method blocks until the count reaches zero.
    The count can be decremented by calling the countDown() method from other threads.

    CountDownLatch with counter value equal to the number of threads we want to run in parallel.

    when we call await() the calling thread goes into waiting state like wait() in traditional java concurrency.
    worker threads call countDown() when they finish their work, which decrements the count of the latch.
    once the count reaches zero, the await() method unblocks and allows the waiting thread to proceed.

 */
public class CountDownLatchExample {
    static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Work(i, countDownLatch));
        }

        try {
            countDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("All workers have finished their work. Main thread is proceeding...");
        executorService.shutdown();
    }
}


class Work implements Runnable{

    private int id;
    private CountDownLatch latch;

    public Work(int id, CountDownLatch latch) {
        this.id = id;
        this.latch = latch;
    }

    @Override
    public void run() {
        doWork();
        latch.countDown();
    }

    private void doWork() {
        try{
            System.out.println("Worker " + this.id + " is doing work...");
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}