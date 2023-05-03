package sbu.cs;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
    In this exercise, you must write a multithreaded program that finds all
    integers in the range [1, n] that are divisible by 3, 5, or 7. Return the
    sum of all unique integers as your answer.
    Note that an integer such as 15 (which is a multiple of 3 and 5) is only
    counted once.

    The Positive integer n > 0 is given to you as input. Create as many threads as
    you need to solve the problem. You can use a Thread Pool for bonus points.

    Example:
    Input: n = 10
    Output: sum = 40
    Explanation: Numbers in the range [1, 10] that are divisible by 3, 5, or 7 are:
    3, 5, 6, 7, 9, 10. The sum of these numbers is 40.

    Use the tests provided in the test folder to ensure your code works correctly.
 */

public class FindMultiples
{

    static final int MAX_THREAD = 4; 

    public static class Task implements Runnable {
        ArrayList<Integer> devidables;
        int upperBound;
        int lowerBound;
        public Task(int lowerBound, int upperBound) {
            devidables = new ArrayList<Integer>(); 
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        @Override
        public void run() {
            for(int i = lowerBound; i < upperBound; i++) {
                if(i % 3 == 0 || i % 5 == 0 || i % 7 == 0) {
                    devidables.add(i);
                }
            }
        }
    }

    // TODO create the required multithreading class/classes using your preferred method.


    /*
    The getSum function should be called at the start of your program.
    New Threads and tasks should be created here.
    */
    public int getSum(int n) {
        int sum = 0;

        ArrayList<Task> tasks = new ArrayList<Task>();
        for(int i = 0; i < n-(n%MAX_THREAD); i += (n/MAX_THREAD)) {
            Task newTask = new Task(i, i+n/MAX_THREAD);
            tasks.add(newTask);
        }
        Task finalTask = new Task(n-(n%MAX_THREAD), n+1);
        tasks.add(finalTask);

        ExecutorService pool = Executors.newFixedThreadPool(MAX_THREAD);

        for(Task task: tasks) {
            pool.execute(task);
        }

        pool.shutdown();
        try {
            while (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                continue;    
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        for(Task task: tasks) {
            for(int number : task.devidables) {
                sum += number;
            }
        }

        return sum;
    }

    public static void main(String[] args) {
    }
}
