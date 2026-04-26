package org.example.livelock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LiveLockExample {

    private Lock lock1 = new ReentrantLock(true);
    private Lock lock2 = new ReentrantLock(true);

    public static void main(String[] args) {
        LiveLockExample liveLockExample = new LiveLockExample();
        Thread t1 = new Thread(liveLockExample::worker1,"workker-1");
        Thread t2 = new Thread(liveLockExample::worker2,"worker-2");

        t1.start();
        t2.start();
    }

    public void worker1() {
        while (true) {
            try {
                lock1.tryLock(50, TimeUnit.MILLISECONDS);
                System.out.println("Worker 1: Acquired lock1...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Worker 1: Trying to acquire lock2...");
             if(lock2.tryLock()){
                 System.out.println("Worker 1: Acquired lock2...");
                 lock2.unlock();
             }else {
                 System.out.println("Worker 1: Could not acquire lock2...");
                 continue;
             }
             break;
        }
        lock1.unlock();
        lock2.unlock();
    }

    public void worker2() {
        while (true) {
            try {
                lock2.tryLock(50, TimeUnit.MILLISECONDS);
                System.out.println("Worker 2: Acquired lock2...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Worker 2: Trying to acquire lock1...");
            if(lock1.tryLock()){
                System.out.println("Worker 2: Acquired lock1...");
                lock1.unlock();
            }else {
                System.out.println("Worker 2: Could not acquire lock1....");
                continue;
            }
            break;
        }
        lock1.unlock();
        lock2.unlock();
    }
}
