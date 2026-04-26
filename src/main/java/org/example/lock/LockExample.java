package org.example.lock;

import java.util.concurrent.locks.ReentrantLock;

public class LockExample {

    private int counter1;
    private int counter2;

    private ReentrantLock lock = new ReentrantLock(true);

    public void incrementCounter1() {
        lock.lock();
        counter1++;
        lock.unlock();
    }

    public void incrementCounter2() {
        lock.lock();
        counter2++;
        lock.unlock();
    }

    public void execute(){
        var t1 = new Thread(()->{
            for(int i=0;i<1000;i++){
                incrementCounter1();
            }
        });

        var t2 = new Thread(()->{
            for(int i=0;i<1000;i++){
                incrementCounter2();
            }
        });

        t1.start();
        t2.start();

        try{
            t1.join();
            t2.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("counter1 value is "+counter1);
        System.out.println("counter2 value is "+counter2);
    }

    public static void main(String[] args) {
        var example = new ThreadExample();
        example.execute();
    }
}
