package org.example.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SimpleRecursiveTask extends RecursiveTask<Integer> {

    private int num;

    public SimpleRecursiveTask(int num){
        this.num = num;
    }

    @Override
    protected Integer compute() {
        if(num>100){
            System.out.println("Parallel execution so split the task..."+num);

            SimpleRecursiveTask task1 = new SimpleRecursiveTask(num/2);
            SimpleRecursiveTask task2 = new SimpleRecursiveTask(num/2);

            //add the tasks to thread pool
            task1.fork();
            task2.fork();

            //wait for these tasks to be finished
            int subSolution = 0;
            subSolution+=task1.join();
            subSolution+=task2.join();
            return subSolution;

        } else{
            // the problem is small so sequential approach is useful
            System.out.println("The task is small we can execute it sequentially:"+num);
            return 2*num;
        }
    }

    public static void main(String arg[]){
        ForkJoinPool pool = new ForkJoinPool();
        SimpleRecursiveTask task = new SimpleRecursiveTask(200);
        System.out.println(task.invoke());
    }
}
