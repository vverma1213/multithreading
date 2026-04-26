package org.example.lock;

public class App {

    public static void main(String args[]) throws InterruptedException {
//        var t = new ThreadExample();
//        t.execute();

        //both are indepdent objects so they will not block each other
//        var obj1 = new ObjectLocking();
//        var obj2 = new ObjectLocking();
//        Runnable task1 = obj1::instanceMethod;
//        Runnable task2 = obj2::instanceMethod;
//        Thread thread1 = new Thread(task1, "Obj Lock Thread-1");
//        Thread thread2 = new Thread(task2, "Obj Lock Thread-2");
//        thread1.start();
//        thread2.start();

        //class level lock so both threads will block each other
        Runnable task3 = ClassLocking::staticMethod;
        Runnable task4 = ClassLocking::staticMethod;
        Thread thread3 = new Thread(task3, "Class Lock Thread-1");
        Thread thread4 = new Thread(task4, "Class Lock Thread-2");
        thread3.start();
        thread4.start();
    }
}

class ObjectLocking{

    public synchronized void instanceMethod(){
        System.out.println(Thread.currentThread().getName()+" entered instance method");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" Finished instance method");
    }
}

class ClassLocking{
    public static synchronized void staticMethod(){
        System.out.println(Thread.currentThread().getName()+" entered static method");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" Finished static method");
    }}