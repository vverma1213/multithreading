package org.example.volatileexample;
/*
    Java threads may cache variables locally (in cpu registers or in thread local memory)
    if one thread updates a variable the change might not be visible to other until it's flushed to main memory.

    Without Volatile: one thread's update to a variable may not be seen by another thread immediately.
    due to cpu caching or compiler optimizations, leading to stale data being read by other threads.

    Volatile keyword in java is a lightweight synchronization mechanism that ensures changes made by one thread
    to a variable are immediately visible to other threads.

 */

public class VolatileExample {

    public static void main(String[] args) {
        Worker worker = new Worker();
        Thread workerThread = new Thread(worker);
        workerThread.start();
        try {
            Thread.sleep(2000); // Let the worker thread run for a while
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        worker.setTerminated(true); // Signal the worker thread to stop
        System.out.println("Main thread signaled worker to terminate.");
    }
}


class Worker implements Runnable{
    // This variable is shared between threads, and we want to ensure that changes to it are visible across threads.
    private volatile boolean isTerminated;

    public boolean isTerminated() {
        return isTerminated;
    }

    public void setTerminated(boolean terminated) {
        isTerminated = terminated;
    }

    @Override
    public void run() {
        while(!isTerminated){
            System.out.println("Working class is running...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}