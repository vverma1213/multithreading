package org.example.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Processor implements Callable<String> {

    private int id;
    public Processor(int id){
        this.id=id;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(2000);
        return "Id: "+id;
    }
}

public class CallableExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<String>> list = new ArrayList<>();

        for(int i=1;i<=10;i++){
            Future<String> future = executor.submit(new Processor(i));
            list.add(future);
        }

        for(Future<String> f:list){
            try{
             System.out.println(f.get());
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                executor.shutdown();
            }
        }
    }
}
