package org.example.lock;
/*
    What are re-entrant locks?
In the previous lectures we have been talking about locks (intrinsic locks or monitor locks).

there is a single intrinsic lock associated with every object or class in Java

a given  thread that needs exclusive and consistent access to an object's fields

has to acquire the object's intrinsic lock before accessing them,

and then the thread releases the intrinsic lock when it's done with them

with Locks: the acquired lock can be released any thread

RLocks can be released by the thread that acquired it exclusively

Ok so a thread cannot acquire a lock owned by another thread. But a given thread can acquire a lock that it already owns. Allowing a thread to acquire the same lock more than once is called re-entrant synchronization. And this is exactly what is happening in Python when using RLocks- the same thread may acquire the lock more than once.

For example: let's consider recursive method calls. If a given thread calls a recursive and synchronized method several times then it is fine (note that in this case the same thread "enters" the synchronized block several times). There will be no deadlock because of re-entrant synchronization.



In the previous lectures, we discussed locks—specifically, intrinsic (or monitor) locks.

In Java, every object or class has a single intrinsic lock associated with it.

When a thread needs exclusive and consistent access to an object's fields, it must first acquire the object's intrinsic lock before accessing them. Once it is done, the thread releases the lock.

A thread cannot acquire a lock that is owned by another thread.
However, a thread can acquire a lock that it already owns. This is known as re-entrant synchronization.

Re-entrant synchronization allows a thread to acquire the same lock multiple times without causing a deadlock.

For example, consider a recursive method.
If a thread calls a recursive, synchronized method multiple times,
it's fine—the same thread re-enters the synchronized block each time.
There’s no deadlock because re-entrant synchronization allows the thread to re-acquire the lock it already holds.

outerMethod and innerMethod are both synchronized.

When a thread enters outerMethod, it acquires the object's intrinsic lock.

It can then safely enter innerMethod—even though it's also synchronized—because it already holds the lock.
This is re-entrant synchronization.
*/
public class ReentrantExample {

    public synchronized void outermethod(){
        System.out.println("Executing outer method");
        innermethod();
        System.out.println("Finished outer method");
    }

    public synchronized void innermethod(){
        System.out.println("Executing inner method");
        // Simulate some work
        System.out.println("Finished inner method");
    }

    public static void main(String[] args) {
        ReentrantExample example = new ReentrantExample();

        Thread thread = new Thread(() -> {
            example.outermethod();
        });

        thread.start();
    }
}
