package org.example.threadpool;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/*

    Runnable vs Callable

    Runnable doesn't return result
    cannot throw checked exception

    can be executed using both execute() and submit() method of ExecutorService

    Callable:
    returns result
    can throw checked exception
    can only be executed using submit() method of ExecutorService that returns a future object

 */
class StockMarket implements Runnable {
    @Override
    public void run() {
        System.out.println("Updating and Downloading stock market data from web...");
    }
}
public class ScheduledThreadPool {

    static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new StockMarket(), 1000, 2000, TimeUnit.MILLISECONDS);
    }
}
