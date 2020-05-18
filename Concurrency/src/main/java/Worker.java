import java.util.List;
import java.util.concurrent.CountDownLatch;

// see the test
/*
The Runnable interface should be implemented by any class
whose instances are intended to be executed by a thread.
The class must define a method of no arguments called run.
https://www.baeldung.com/java-countdown-latch
*/
// NOT a thread, but a thread runnable class!

public class Worker implements Runnable{
        private List<String> outScraper;
        private CountDownLatch countDownLatch;

        public Worker(List<String> outScraper, CountDownLatch countDownLatch){
            this.outScraper=outScraper;
            this.countDownLatch=countDownLatch;
        }

        @Override
        public void run(){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outScraper.add("Count Down");
            System.out.println("Worker worked");
            countDownLatch.countDown();
        }
}
