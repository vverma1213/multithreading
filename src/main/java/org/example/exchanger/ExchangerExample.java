package org.example.exchanger;

import java.util.concurrent.Exchanger;

/*
    Exchange objects data between multiple threads using Exchanger class in java.

 */
public class ExchangerExample {
    static void main(String[] args) {
        Exchanger<Integer> exchanger = new Exchanger<>();
        Thread t1 = new Thread(new FirstThread(exchanger));
        Thread t2 = new Thread(new SecondThread(exchanger));

        t1.start();
        t2.start();
    }
}

class FirstThread implements Runnable {

    private int counter;
    private Exchanger<Integer> exchanger;

    public FirstThread(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        while(true){
            try {
                counter++;
                System.out.println("First Thread: Incremented Counter value: " + counter);
                counter = exchanger.exchange(counter);
                System.out.println("First Thread: Counter value after exchange: " + counter);
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

class SecondThread implements Runnable {

    private int counter;
    private Exchanger<Integer> exchanger;

    public SecondThread(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        while(true){
            try {
                counter--;
                System.out.println("Second Thread: Decremented Counter value: " + counter);
                counter = exchanger.exchange(counter);
                System.out.println("Second Thread: Counter value after exchange: " + counter);
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}