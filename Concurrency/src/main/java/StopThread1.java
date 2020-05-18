import java.util.concurrent.TimeUnit;

public class StopThread1 {

    private static volatile boolean stopRequested=false;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested)
                i++;
            System.out.println("Thread Closed");
        });
        backgroundThread.start();
        // It works on my machine! But not guranteed!
        TimeUnit.SECONDS.sleep(2);
        stopRequested = true;
    }
}