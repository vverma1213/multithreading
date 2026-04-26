package org.example.lock;

public class ThreadExample {
    private int counter1;
    private static int counter2;

    private Object lock1 = new Object();
    private Object lock2 = new Object();

    //object-level locking
    public void incrementCounter1() {
        synchronized(lock1) {
            counter1++;
        }
    }
    //class-level locking
    public static void incrementCounter2() {
        synchronized (ThreadExample.class) {
            counter2++;
        }
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
