package ru.javawebinar.basejava;

import java.text.SimpleDateFormat;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MainConcurrency {
    public static final int THREADS_NUMBER = 10000;
    private static int counter;
    public final AtomicInteger atomicCounter = new AtomicInteger();

    //private static final Object LOCK = new Object();
    private static final Lock lock = new ReentrantLock();
    private static final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static final Lock WRITE_LOCK = reentrantReadWriteLock.writeLock();
    private static final Lock READ_LOCK = reentrantReadWriteLock.readLock();

    private static final SimpleDateFormat sdf = new SimpleDateFormat();
    private static final ThreadLocal<SimpleDateFormat> threadLocal = ThreadLocal.withInitial(SimpleDateFormat::new);

    public static void main(String[] args) throws InterruptedException {

        System.out.println(Thread.currentThread().getName());

        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ", " + getState());
            }
        };
        thread0.start();

        final MainConcurrency mainConcurrency = new MainConcurrency();
        CountDownLatch latch = new CountDownLatch(THREADS_NUMBER);
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        //CompletionService completionService = new ExecutorCompletionService(executorService);

        //List<Thread> threads = new ArrayList<>(THREADS_NUMBER);

        for (int i = 0; i < THREADS_NUMBER; i++) {
            Future<Integer> future = executorService.submit(() -> {
                //Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
                    //System.out.println(threadLocal.get().format(new Date()));
                }
                latch.countDown();
                return 5;
            });
            //System.out.println(future.get().toString());
            //thread.start();
            //    threads.add(thread);
        }

//        threads.forEach(t->{
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
        //System.out.println(counter);
        System.out.println(mainConcurrency.atomicCounter.get());

    }

    //    private synchronized void inc() {
    private void inc() {
        //synchronized (this) {
        //try {
        //    lock.lock();
        //    counter++;
        //} finally {
        //    lock.unlock();
        //}
        //}
        atomicCounter.incrementAndGet();
    }
}