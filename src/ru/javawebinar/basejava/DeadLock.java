package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class DeadLock {
    public static final int COUNT_THREAD = 2;
    public static Lock_A lock_a = new Lock_A("Object A initialisation");
    public static Lock_B lock_b = new Lock_B("Object B initialisation");

    public static void main(String[] args) throws InterruptedException {

        List<Thread> threads = new ArrayList<>(COUNT_THREAD);
        threads.add(createThread(lock_a));
        threads.add(createThread(lock_b));

        for (Thread thread : threads) {
            thread.start();
        }

        sleep(5000);
        System.out.println("Thread MAIN completed");
//        for (Thread thread : threads) {
//            thread.interrupt();
//        }
        for (Thread thread : threads) {
            System.out.println(thread.getName() + " - " + thread.getState());
        }
    }

    private interface Lock {
        void lockField(String field);
    }

    private static Thread createThread(Lock lock) {
        return new Thread(() -> {
            do {
                System.out.println(Thread.currentThread().getName() + " - locking " + lock.getClass().getName());
                lock.lockField(Thread.currentThread().getName() + ", " + Thread.currentThread().getState());
            } while (!Thread.currentThread().isInterrupted());
            System.out.println("Thread " + Thread.currentThread().getName() + " complite is successful");
        });
    }


    public static class Lock_A implements Lock {

        private String field;

        public Lock_A(String field) {
            this.field = field;
            System.out.println(this.field);
        }

        @Override
        public synchronized void lockField(String field) {
            System.out.println(Thread.currentThread().getName() + " - Calling method of object A");
            this.field = field;
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock_b.lockField("Calling method of object B");
            System.out.println(Thread.currentThread().getName() + " - unlock object A");
        }

    }

    public static class Lock_B implements Lock {

        private String field;

        public Lock_B(String field) {
            this.field = field;
            System.out.println(this.field);
        }

        @Override
        public synchronized void lockField(String field) {
            System.out.println(Thread.currentThread().getName() + " - Calling method of object B");
            this.field = field;
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock_a.lockField("Calling method of object A");
            System.out.println(Thread.currentThread().getName() + " - unlock object B");
        }

    }

}
