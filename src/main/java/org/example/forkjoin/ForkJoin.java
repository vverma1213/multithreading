package org.example.forkjoin;

import java.util.concurrent.ForkJoinPool;

/*
    Fork-join is an implementation of the ExecutorService interface for parallel execution

    The framework helps to make an algorithm parallel

    no need to bother about low level synchronization or locks
    it is a divide and conquer mechanism
    larger tasks can be divided into smaller ones and then we have to combine the subsolutions into the final
    solution of the problem.

    subtasks have to be independent in order to be executed in parallel

    Tasks are lightweight threads so fork join will be efficient even when there are huge number of tasks

    ForkJoinPool create fix number of threads usually the number of CPU cores
    These threads are executing the tasks but if a thread has no tasks, it can steal a task from more busy
    threads, task are distributed to all threads in the thread pool.

    Fork join framework can handle the problem of load balancing quite efficiently.

    FORK - splits the given tasks into smaller subtasks that can be executed in a parallel manner.

    JOIN - the splitted tasks are being executed and after all of them are finished and merge into one result.

    fork()- asynchronously executes the given tasks in the pool we can call it when using RecursiveTask or RecursiveAction

    join() - return the result of the computation when it is finished.
    invoke() - executes the given task + wait + return the result upon completion
 */
public class ForkJoin {

    static void main(String[] args) {
        //System.out.println(Runtime.getRuntime().availableProcessors());
        ForkJoinPool pool = new ForkJoinPool();
        SimpleRecursiveAction action = new SimpleRecursiveAction(800);
        action.invoke();
    }
}
