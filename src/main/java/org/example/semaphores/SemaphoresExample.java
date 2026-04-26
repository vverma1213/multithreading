package org.example.semaphores;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/*
Semaphores are a synchronization mechanism used to control access to a shared resource in concurrent programming.
They are often used to manage access to shared resources such as critical sections of code,
shared data structures, or hardware devices.

Semaphore keeps track of how many units of a particular resource are available.
A thread mush wait until a unit becomes available before proceeding.

1 or 0 - binary semaphore
used to enforce mutual exclusion (mutex) - only one thread can access the resource at a time.

Counting Semaphore - allows a specified number of threads to access the resource concurrently.

MUTEX - Mutual Exclusion
a mutex is a programming construct used to prevent multiple threads from accessing a shared resource simultaneously.

Only one thread at a time can hold the mutex and execute the protected code section.
other hands attempting to acquire the mutex are blocked until it becomes available.

 */
public class SemaphoresExample {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Downloader.INSTANCE.download();
                }
            });
        }
    }
}

enum Downloader{
    INSTANCE;
    private Semaphore semaphore = new Semaphore(3,true);

    public void download(){
        try{
            semaphore.acquire();
            downloadData();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            semaphore.release();
        }
    }

    private void downloadData() {
        try{
            System.out.println("Downloading data from the web....");
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}