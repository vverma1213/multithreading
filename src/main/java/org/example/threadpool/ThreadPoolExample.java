package org.example.threadpool;

/*
    A thread pool is a collection of worker threads that are used to execute many tasks.
    Instead of creating a new thread for each task, task is submitted to the thread pool and thread picks up and execute them.

    Java provides thread pool through the Executor framework, which is part of the java.util.concurrent package.

    Threads are created once and reuse multiple times. it reduces overhead of frequent thread creation and destruction.

    if all threads are busy, submitted task are queued they are held in a blocking queue data structure until a thread becomes available.

    newFixedThreadPook(int n) - The thread pool is initialized lazily, threads are not created at the start but they are created on demand.
    Once all threads are created and busy, new tasks are put into the queue and wait until a thread becomes available.

    newCachedThreadPool() - The thread pool creates new threads as needed and reuse previously constructed threads when available.
    This approach automatically removes idle thread after a short timeout. (60 seconds by default)

    Beneficial for many short lived async tasks.

    newSingleThreadExecutor() - This creates a thread pool that uses only one worker thread to execute tasks sequentially + uses unbounded queue.

    newScheduledThreadPool(int n) - This creates a thread pool that can schedule tasks to run after a delay, or schedule task to run periodically.
     Thread starvation risks - if your core pool size is small and schedule task running long. new schedule task may be delayed.

     Supports Future and Callable - you can submit task that can return value using Callable and the results are wrapped into Future object.
     Automatic Thread lifecycle management - Executor handles creation, execution and shutdown of threads
     Thread Reuse - Threads are created once and reused for multiple tasks reduces overhead of thread creation and destruction.


     */

public class ThreadPoolExample {

}
