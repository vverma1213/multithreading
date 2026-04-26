package org.example.deadlock;

/*
    A deadlock in java occurs when two or more threads are waiting for each other to release resource.
    and none of them ever does As a result all threads involved in the deadlock are blocked forever.

    Solution: we can use lock interface and Reentrantlock implementation ReentrantLock.tryLock()


    LiveLock: LiveLock is a situation where two or more threads keep changing their state in response to each other.
    but Never make actual progress. - similar to deadlock but the threads are not blocked.
    just too busy reacting to each other to finish.

        Solution: we can use lock interface and Reentrantlock implementation ReentrantLock.tryLock()

 */

import java.util.concurrent.locks.ReentrantLock;

public class DeadlockExample {

    private ReentrantLock lock1 = new ReentrantLock(true);
    private ReentrantLock lock2 = new ReentrantLock(true);


    public static void main(String[] args) {
        DeadlockExample example = new DeadlockExample();

        Thread t1 = new Thread(example::worker1,"Worker-1");
        Thread t2 = new Thread(example::worker2,"Worker-2");

        t1.start();
        t2.start();
    }

    public void worker1(){
        lock1.lock();
        System.out.println("Worker 1: Holding lock 1...");
        try{
            Thread.sleep(300);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        lock2.lock();
        System.out.println("Worker 1: Acquired the lock2...");

        lock1.unlock();
        lock2.unlock();
    }

    //eliminate cyclick dependency
    public void worker2(){
        //lock2.lock();
        //solution to avoid deadlock - acquire locks in a consistent order
        lock1.lock();
        System.out.println("Worker 2: Holding the lock2...");
        try{
            Thread.sleep(300);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        lock2.lock();
        System.out.println("Worker 2: Acquired the lock1...");

        lock1.unlock();
        lock2.unlock();
    }
}
