package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class DeadLock {
    private static String lock_a = "A";
    private static String lock_b = "B";

    public static void main(String[] args) throws InterruptedException {

        List<Thread> threads = new ArrayList<>(2);
        threads.add(createThread(lock_a, lock_b));
        threads.add(createThread(lock_b, lock_a));

        for (Thread thread : threads) {
            thread.start();
        }

        sleep(5000);
        System.out.println("Thread MAIN completed");

        for (Thread thread : threads) {
            System.out.println(thread.getName() + " - " + thread.getState());
        }
    }


    private static Thread createThread(Object a, Object b) {
        return new Thread(() -> {
            synchronized (a) {
                System.out.println(Thread.currentThread().getName() + " - locking " + a.toString());
                //try {
                //    sleep(500);                      // with it - 100%
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}
                synchronized (b) {
                    System.out.println(Thread.currentThread().getName() + " - locking " + b.toString());
                }
            }
            System.out.println(Thread.currentThread().getName() + " unlocked " + a.toString() + " & " + b.toString());
        });
    }
}
