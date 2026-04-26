package org.example.philosopher.problem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = null;
        Philosopher[] philosopher = null;
        Chopstick[] chopsticks = null;

        try{
            philosopher = new Philosopher[Constants.NUMBER_OF_PHILOSOPHERS];
            chopsticks = new Chopstick[Constants.NUMBER_OF_CHOPSTICKS];

            for(int i=0;i<Constants.NUMBER_OF_CHOPSTICKS;i++){
                chopsticks[i] = new Chopstick(i);
            }
            executorService = Executors.newFixedThreadPool(Constants.NUMBER_OF_PHILOSOPHERS);
            for(int i=0;i<Constants.NUMBER_OF_PHILOSOPHERS;i++){
                philosopher[i] = new Philosopher(i,chopsticks[i],chopsticks[(i+1)%Constants.NUMBER_OF_PHILOSOPHERS]);
                executorService.execute(philosopher[i]);
            }
            Thread.sleep(Constants.SIMULATION_TIME);
            for(Philosopher philosopher1:philosopher){
                philosopher1.setIsFull(true);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
            while(!executorService.isTerminated()){
                Thread.sleep(1000);
                for(Philosopher philosopher1:philosopher){
                    System.out.println(philosopher1+" eat #"+philosopher1.getEatingCounter()+" times!");
                }
            }
        }
    }
}
