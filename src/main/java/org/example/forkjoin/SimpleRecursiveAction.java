package org.example.forkjoin;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class SimpleRecursiveAction extends RecursiveAction {

    private int simulatedWork;

    public SimpleRecursiveAction(int simulatedWork){
        this.simulatedWork = simulatedWork;
    }
    @Override
    protected void compute() {
        // if the task is too large then we split it and execute the task in parallel
        if(simulatedWork>100){
            System.out.println("Parallel execution and split the task..."+simulatedWork);

            SimpleRecursiveAction recursiveAction1 = new SimpleRecursiveAction(simulatedWork/2);
            SimpleRecursiveAction recursiveAction2 = new SimpleRecursiveAction(simulatedWork/2);

            recursiveAction1.fork();
            recursiveAction2.fork();

            recursiveAction1.join();
            recursiveAction2.join();
            //Same as below
            //invokeAll(recursiveAction1,recursiveAction2);
        } else{
            System.out.println("The task is rather small so sequential is fine....");
            System.out.println("The size of the task:"+simulatedWork);
        }

    }
}
