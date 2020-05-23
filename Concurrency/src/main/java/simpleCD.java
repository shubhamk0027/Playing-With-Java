import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class simpleCD {

    public static void main(String[] args) throws InterruptedException {
        int numThreads=5;
        List<String> outScraper= Collections.synchronizedList(new ArrayList<>());   // A common sync list
        CountDownLatch countDownLatch= new CountDownLatch(numThreads);              // A common latch

        List<Thread> workerThreads =  Stream
                .generate( () -> new Thread(new Worker(outScraper,countDownLatch)))
                .limit(numThreads)
                .collect(toList());

        workerThreads.forEach(Thread::start);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            //Handle when a thread gets interrupted.
        }

        // await guarantees that all the 5 threads are executed before running this!
        Thread.sleep(2000);
        System.out.println("All tasks have finished..");
    }
}
