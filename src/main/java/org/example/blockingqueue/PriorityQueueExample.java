package org.example.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/*
    Heap based implementation of PriorityBlockingQueue.
    MinHeap/MaxHeap


 */
public class PriorityQueueExample {
    static void main(String[] args) {
        BlockingQueue<String> queue = new PriorityBlockingQueue<>();
        Thread t1 = new Thread(new FirstPriorityWorker(queue));
        Thread t2 = new Thread(new SecondPriorityWorker(queue));

        t1.start();
        t2.start();
    }
}


class FirstPriorityWorker implements Runnable {

    BlockingQueue<String> queue;
    public FirstPriorityWorker(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("First Worker is executing");
        try {
            queue.put("B");
            queue.put("A");
            queue.put("C");
            queue.put("z");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class SecondPriorityWorker implements Runnable {

    BlockingQueue<String> queue;
    public SecondPriorityWorker(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("Second Worker is executing");
        try {
            Thread.sleep(100);
            System.out.println(queue.take());
            System.out.println(queue.take());
            Thread.sleep(100);
            System.out.println(queue.take());
            System.out.println(queue.take());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}