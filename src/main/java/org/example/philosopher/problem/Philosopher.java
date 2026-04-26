package org.example.philosopher.problem;

import java.util.Random;

public class Philosopher implements Runnable{
    private int id;
    private volatile boolean isFull;
    private Chopstick leftChopstick;
    private Chopstick rightChopstick;
    private Random random;
    private int eatingCounter;

    public Philosopher(int id, Chopstick leftChopstick, Chopstick rightChopstick) {
        this.id = id;
        this.isFull = false;
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
        this.random = new Random();
        this.eatingCounter = 0;
    }

    public int getEatingCounter(){
        return eatingCounter;
    }

    @Override
    public void run() {
        try {
            // after eating a lot then we will terminate the given thread
            while (!isFull) {
                think();
                if (leftChopstick.pickUp(this, State.LEFT)) {
                    //phhilosoper acquired a lock on left chopstick
                    if (rightChopstick.pickUp(this, State.RIGHT)) {
                        //philosopher acquired a lock on right chopstick
                        eat();
                        rightChopstick.putDown(this, State.RIGHT);

                    }
                    leftChopstick.putDown(this, State.LEFT);
                }
            }
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking");
        Thread.sleep(random.nextInt(1000));
    }

    private void eat() throws InterruptedException {
        System.out.println("Philosopher " + id + " is eating");
        eatingCounter++;
        Thread.sleep(random.nextInt(1000));
    }

    public void setIsFull(boolean isFull) {
        this.isFull = isFull;
    }

    public boolean getIsFull() {
        return this.isFull;
    }

    @Override
    public String toString() {
        return "Philosopher{" +
                "id=" + id +
                ", isFull=" + isFull +
                ", leftChopstick=" + leftChopstick +
                ", rightChopstick=" + rightChopstick +
                ", random=" + random +
                ", eatingCounter=" + eatingCounter +
                '}';
    }
}
