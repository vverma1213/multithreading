package org.example.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FibonacciTask extends RecursiveTask<Integer> {

    private int n;

    public FibonacciTask(int n){
        this.n=n;
    }
    @Override
    protected Integer compute() {

        if(n<=1){
            return n;
        }
        FibonacciTask task1 = new FibonacciTask(n-1);
        FibonacciTask task2 = new FibonacciTask(n-2);

        task1.fork();
        task2.fork();

        return task1.join()+task2.join();
    }

    public static void main(String arg[]){
        ForkJoinPool pool = new ForkJoinPool();
        FibonacciTask task = new FibonacciTask(9);
        System.out.println(pool.invoke(task));
    }
}
