package org.example.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerReentrantLockExample {

    public static void main(String args[]) {
        Worker worker = new Worker();

        Thread t1 = new Thread(()-> {
            try {
                worker.producer();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t2 = new Thread(()-> {
            try {
                worker.consumer();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();
    }
}


class Worker{

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

        public void producer() throws InterruptedException {
            lock.lock();
            System.out.println("Producer is producing...");
            //wait
            condition.await();
            System.out.println("Producer is resuming...");
            lock.unlock();
        }

        public void consumer() throws InterruptedException {
            lock.lock();
            System.out.println("Consumer is consuming...");
            Thread.sleep(2000);
            System.out.println("Consumer is done consuming...");
            //notify
            condition.signal();
            lock.unlock();
        }
}