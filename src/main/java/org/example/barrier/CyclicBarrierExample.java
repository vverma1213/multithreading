package org.example.barrier;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
    CyclicBarrier is a synchronization aid that allows a set of threads to all wait for each other to reach a common barrier point.
    It is useful in scenarios where you want to perform some action after a group of threads have completed their tasks.

    CyclicBarrier - it can be re-used over and over again
 */
public class CyclicBarrierExample {
    static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(5); // Number of threads to wait for
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(int i=0;i<5;i++){
            executor.execute(new Worker(i+1,barrier));
        }
        System.out.println("All workers have been submitted to the executor.");
         executor.shutdown();
    }
}


class Worker implements Runnable{
    private CyclicBarrier barrier;
    private int id;
    private Random random;

    public Worker(int id, CyclicBarrier barrier) {
        this.id = id;
        this.barrier = barrier;
        this.random = new Random();
    }

    @Override
    public void run() {
        doWork();
    }

    private void doWork() {
        try {
            System.out.println("Worker " + id + " is doing work...");
            Thread.sleep(random.nextInt(3000)); // Simulate work by sleeping for a random time
            System.out.println("Worker " + id + " has finished work, waiting at the barrier...");
            barrier.await(); // Wait for all threads to reach this point
            System.out.println("Worker " + id + " has passed the barrier, proceeding with next steps...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}