package Deadlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    /* Classes Deadlock.Deadlock3 and Deadlock.Deadlock4 gives a deadlock*/
    public static void main(String[] args) throws InterruptedException {

        ExecutorService threadPool =  Executors.newFixedThreadPool(2);
        // CountDownLatch countDownLatch = new CountDownLatch(2);
        threadPool.submit(new Runnable() {
            @lombok.SneakyThrows
            public void run() {
                System.out.println("Hello");
                Deadlock4.methodEven();
                System.out.println("Hello");
                // countDownLatch.countDown();
            }
        });
        threadPool.submit(new Runnable() {
            @lombok.SneakyThrows
            public void run() {
                System.out.println("Bello");
                Deadlock4.methodOdd();
                System.out.println("Bello");
                // countDownLatch.countDown();
            }
        });
        Thread.sleep(5000);
        // countDownLatch.await();
        System.out.println("Threads.Threads Deadlocked!");
    }
}
