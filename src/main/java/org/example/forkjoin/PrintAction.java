package org.example.forkjoin;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrintAction extends RecursiveAction {

    private List<Integer> nums;

    public PrintAction(List<Integer> nums) {
        this.nums = nums;
    }

    @Override
    protected void compute() {
        // the problem is small enough (containing 2 items) so we use
        // standard sequential print operation
        if(nums.size() < 2) {
            for(Integer num : nums)
                System.out.println(num);
        } else {
            // otherwise we split the problem into 2 sub-problems
            // [a,b,c] --> [a] and [b,c]
            // [a,b,c,d] --> [a,b] and [c,d]
            List<Integer> left = nums.subList(0, nums.size()/2);
            List<Integer> right = nums.subList(nums.size()/2,  nums.size());

            PrintAction action1 = new PrintAction(left);
            PrintAction action2 = new PrintAction(right);

            // it means these actions are thrown into the pool
            // fork-join assigns different threads to different tasks
            // so these will be executed with different treads
            invokeAll(action1, action2);
        }
    }

    public static void main(String args[]){

        int[] intArray = IntStream.range(1,10000).toArray();

        // Convert to List<Integer>
        List<Integer> integerList = Arrays.stream(intArray)
                .boxed()  // Convert int to Integer
                .collect(Collectors.toList())
                .stream().sorted().toList();

        PrintAction action = new PrintAction(integerList);
        action.invoke();
    }
}
