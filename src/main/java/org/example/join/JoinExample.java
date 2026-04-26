package org.example.join;

public class JoinExample {

    public static void main(String args[]) throws InterruptedException {
        Thread t1 = new Thread( ()-> {
            System.out.println("Thread 1 Starting");
            System.out.println("Thread 1 Finished");
        });

        Thread t2 = new Thread(()-> {
            System.out.println("Thread 2 Starting");
            try {
                t1.join();
                System.out.println("Thread 2 Finished");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } );
        Thread t3 = new Thread(()-> {
            System.out.println("Thread 3 Starting");
            try {
                t2.join();
                    System.out.println("Thread 3 Finished");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } );

        t1.start();
        t2.start();
        t3.start();

        System.out.println("Main Thread");
    }
}

