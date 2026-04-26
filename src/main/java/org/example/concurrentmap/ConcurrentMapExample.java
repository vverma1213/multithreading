package org.example.concurrentmap;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*

    ConcurrentHashMap is a thread-safe variant of hash map that allows concurrent read and write operations
    without the need for external synchronization.
    Segment level locking
    16 segments by default - each segment is a separate hash table with its own lock
    When a thread wants to perform an operation on the map, it first determines which segment the key belongs to
    and then acquires the lock for that segment before performing the operation.
     This allows multiple threads to access different segments of the map concurrently,
     improving performance in multi-thread environments.

     Reading operations (get) can proceed concurrently without locking,
     while write operations (put, remove) require locking the relevant segment.

     There are volatile fields to ensure visibility of changes across threads,
     and the implementation uses a combination of locking and non-blocking techniques to achieve thread safety.
 */
public class ConcurrentMapExample {
    static void main(String[] args) {
        ConcurrentMap<String,Integer> concurrentMap = new ConcurrentHashMap<>();
        Thread t1 = new Thread(new FirstConcurrentWorker(concurrentMap));
        Thread t2 = new Thread(new SecondConcurrentWorker(concurrentMap));
        t1.start();
        t2.start();
    }
}

class FirstConcurrentWorker implements Runnable{

    private ConcurrentMap<String,Integer> concurrentMap;

    public FirstConcurrentWorker(ConcurrentMap<String, Integer> concurrentMap) {
        this.concurrentMap = concurrentMap;
    }

    @Override
    public void run() {
        concurrentMap.put("A",1);
        concurrentMap.put("B",2);
        concurrentMap.put("C",3);
    }
}

class SecondConcurrentWorker implements Runnable{

    private ConcurrentMap<String,Integer> concurrentMap;

    public SecondConcurrentWorker(ConcurrentMap<String, Integer> concurrentMap) {
        this.concurrentMap = concurrentMap;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(100);
            System.out.println(concurrentMap.get("A"));
            Thread.sleep(100);
            System.out.println(concurrentMap.get("B"));
            Thread.sleep(100);
            System.out.println(concurrentMap.get("C"));
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}