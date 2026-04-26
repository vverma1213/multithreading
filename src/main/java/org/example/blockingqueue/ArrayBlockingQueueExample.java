package org.example.blockingqueue;

/*
    A blocking queue is a thread-safe queue and is widely used in producer-consumer scenarios.
    it is blocked when you try to take from an empty queue.
    it is blocked when you try to put into a full queue.

    ArrayBlockingQueue - Fixed in size
    Single lock for all operations. (insert/remove)

    ArrayblockingQueue uses ReentrantLock


    Convinient to use synchronized data structures
    we don't have to worry about synchronization and thread safety when using blocking queues.
    everything is handled internally by java.

 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ArrayBlockingQueueExample {

    public static void main(String[] args) {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);

        Thread producerThread = new Thread(new FirstWorker(blockingQueue));
        Thread consumerThread = new Thread(new SecondWorker(blockingQueue));

        producerThread.start();
        consumerThread.start();
    }
}

class FirstWorker implements Runnable{

    private BlockingQueue<Integer> blockingQueue;

    public FirstWorker(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        int counter=0;
        while(true){
            try{
                blockingQueue.put(counter);
                System.out.println("Putting item into the queue: "+counter);
                counter++;
                Thread.sleep(100);

            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

class SecondWorker implements Runnable{

    private BlockingQueue<Integer> blockingQueue;

    public SecondWorker(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        while(true){
            try{
                int counter = blockingQueue.take();
                System.out.println("Takeout item from the queue: "+counter);
                Thread.sleep(3000);

            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}