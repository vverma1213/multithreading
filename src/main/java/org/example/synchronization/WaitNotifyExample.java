package org.example.synchronization;

/*
Threads that are waiting on the same intrinsic lock cannot be proceeded
until another thread call notify() or notifyAll() and the lock is released.

Both wait() and notify() must be called from within a synchronized context,
either a synchronized block or a synchronized method.
and specifically, on the same object whose monitor is being used.

Inter thread communication.

Problem: Starvation: when a thread is waiting indefinitely for a condition that may never be satisfied.
Fairness: when multiple threads are waiting for a condition,
they should be given a chance to proceed in a fair manner, preventing any thread from being indefinitely delayed.
JVM cannot guarantee that longest waiting thread will be notified first, threads will be selected randomly for notification.

No threads get starved and threads get their turn in a reasonable or predictable order.


WAIT vs SLEEP:

you call wait on the Object while on the other hand you call sleep on the Thread itself

wait can be interrupter (this is why we need the InterruptedException) while on the other hand sleep can not

wait (and notify) must happen in a synchronized  block on the monitor object whereas sleep does not

sleep operation does not release the locks it holds while on the other hand wait releases the lock on the object that wait() is called on
 */

public class WaitNotifyExample {

    public static void main(String[] args) {
        Process process = new Process();

        Thread producerThread = new Thread(() -> {
            try {
                process.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                process.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producerThread.start();
        consumerThread.start();
    }
}

class Process{

    public void produce() throws InterruptedException {
        synchronized (this){
            System.out.println("Producer thread is running...");
            wait();
            System.out.println("Producer thread resumed...");
        }
    }

    public void consume() throws InterruptedException {
        Thread.sleep(1000); // Simulate some work
        synchronized (this){
            System.out.println("Consumer thread is running...");
            notify();
            System.out.println("Consumer thread sent notification...");
        }
    }
}
