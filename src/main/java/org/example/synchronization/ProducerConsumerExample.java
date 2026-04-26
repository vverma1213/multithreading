package org.example.synchronization;

import java.util.LinkedList;
import java.util.List;


/*
    Limitation of Synchronized
    No tryLock() - can't attempt to acquire a lock with timeout or non-blocking check
    Not Interruptible - A thread blocked on a synchronized block cant be interrupted
    No fairness - No guarantee that waiting threads will acquire the lock in order
    No Multiple condition variables - only wait/notify, not flexible as Condition.

    Lock is an interface that defines a more flexible locking mechanism than a synchronize keyword.

    Fair Lock:
    Lock fairLock = new ReentrantLock(true); // true for fair lock, false for non-fair lock  - FIFO order of lock acquisition
        Maintain order
        prevents Starvation - ensures that all threads get a chance to acquire the lock

        Slow performance - due to overhead of maintaining the queue of waiting threads and context switching
        When to use: Predictable behaviour is required, and fairness is more important than performance.

    Non-Fair Lock:
    Lock nonFairLock = new ReentrantLock(false); // false for non-fair lock - No guarantee of lock acquisition order

    Non-Deterministic order of lock acquisition
    Higher performance - due to reduced overhead of managing waiting threads
    When to use: High performance is required and fairness is not a concern.

=======================================================================
    Reentrant is an implementation of Lock interface.
    It is called Reentrant because the thread that holds the lock can acquire it again without getting blocked.



    tryLock() - Tries to acquire the lock without blocking.

    If a thread tries to enter the synchronized block or method and the lock is held by another thread.
    it will wait indefinitely untile the lock becomes available.

    lockInterruptibly() - Acquires the lock unless the current thread is interrupted.

    newCondition() - Returns a new Condition instance that is bound to this Lock instance.

 */
public class ProducerConsumerExample {

    static void main(String[] args) {
        var sharedBuffer = new SharedBuffer(5);
        var producer = new Producer(sharedBuffer);
        var consumer = new Consumer(sharedBuffer);
        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);
        producerThread.start();
        consumerThread.start();
    }
}


class SharedBuffer {

    private List<Integer> buffer = new LinkedList<>();
    private int capacity;

    public SharedBuffer(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void produce() throws InterruptedException {
//        if(buffer.size() == capacity){
//            System.out.println("Buffer is full, producer is waiting..");
//            wait();
//        }

        //Josua's Blouch approach
        while(buffer.size() == capacity){
            System.out.println("Buffer is full, producer is waiting..");
            wait();
        }
        System.out.println("Adding items to buffer with the producer....");
        for(int i=0;i<capacity;i++){
            buffer.add(i);
            System.out.println("Added item "+i+" to buffer");
        }
        //wake up the consumer thread to consume the items from buffer
        notify();
    }


    public synchronized void consume() throws InterruptedException {
//        if(buffer.size()<capacity){
//            System.out.println("Buffer is less than it's capacity, consumer waiting...");
//            wait();
//        }

        while(buffer.size()<capacity){
            System.out.println("Buffer is less than it's capacity, consumer waiting...");
            wait();
        }

        while(!buffer.isEmpty()){
            int item = buffer.remove(0);
            System.out.println("Consumed item "+item+" from buffer");
            Thread.sleep(100); // Simulate time taken to consume an item
        }

        notify();

    }
}

class Consumer implements Runnable{
    private SharedBuffer buffer;

    public Consumer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while(true) {
            try {
                buffer.consume();
            Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Producer implements Runnable{
    private SharedBuffer buffer;

    public Producer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while(true) {
            try {
                buffer.produce();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}