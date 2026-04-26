package org.example.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/*
    Dealy Queue keeps element internally until a certain delay has expired.
    an object can only be taken from the queue when it's delay has expired.

    We cannot place null items in the queue, the queue is sorted so that the object at the head has a delay
    that has expired for the longest time.

    if no delay has expired, then there is no head element and poll() method will return null

    size() return the count of both expired and unexpired elements in the queue.


 */
public class DelayBlockingQueueExample {
    static void main(String[] args) {
        BlockingQueue<DelayedWorker> queue = new DelayQueue<>();
        try {
            queue.put(new DelayedWorker(10, "Task 1"));
            queue.put(new DelayedWorker(20, "Task 2"));
            queue.put(new DelayedWorker(30, "Task 3"));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //get message

        while (!queue.isEmpty()) {
            try {
                System.out.println("Processing: " + queue.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class DelayedWorker implements Delayed{

    private long duration;
    private String message;

    public DelayedWorker(long duration, String message) {
            this.duration = System.currentTimeMillis() + duration;
            this.message = message;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "DelayedWorker{" +
                "duration=" + duration +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(duration,TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if(duration < ((DelayedWorker) o).getDuration()){
            return -1;
        }
        if(duration > ((DelayedWorker) o).getDuration()){
            return 1;
        }
        return 0;
    }
}