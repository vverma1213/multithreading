package org.example.blockingqueue;

import java.util.concurrent.LinkedBlockingQueue;

/*
    take and put both are independent lock.
    this is a key optimization in linkedblocking queue.

    producer and consumer can work in parallel without blocking each other.
    as long as the queue is not full or empty respectively.

    Unbounded or growing queue
    flexible as dual locks available
 */
public class LinkedBlockingQueueExample {

    static void main(String[] args) {

        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

        Runnable producer = () -> {
            int i =0;
            try {
                while(true) {
                    System.out.println("Producing: " + i);
                    queue.put(i);
                    i++;
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumer = () -> {
            try {
                while (true) {
                    int i = queue.take();
                    System.out.println("Consuming: " + i);
                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);
        producerThread.start();
        consumerThread.start();
    }
}
