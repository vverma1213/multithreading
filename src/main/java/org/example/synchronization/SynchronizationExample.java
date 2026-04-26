package org.example.synchronization;

public class SynchronizationExample {
    private static int counter;

    // This method is not thread-safe, multiple threads can access and modify the counter variable at the same time, leading to
    // race conditions and incorrect results. To make it thread-safe,
    // we can use synchronization mechanisms like synchronized keyword or locks.
    public static void increment(){
        counter++;
    }

    public static void main(String argsp[]){
        Thread t1 = new Thread(()->{
            for(int i=0;i<1000;i++){
                increment();
            }
        });

        Thread t2 = new Thread(()->{
            for(int i=0;i<1000;i++){
                increment();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Counter Value is:"+counter);
    }
}
