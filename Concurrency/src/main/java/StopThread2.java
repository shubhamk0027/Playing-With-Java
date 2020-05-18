import java.util.concurrent.TimeUnit;

public class StopThread2 {
    private static boolean stopRequested;
    private static synchronized void requestStop() {
        stopRequested = true;
    }
    private static synchronized boolean stopRequested() {
        return stopRequested;
    }
    public static void main(String[] args)
            throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested())
                i++;
        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        requestStop();
    }
    /*
    Synchronization is not guaranteed to work unless both read and
write    operations are synchronized. Occasionally a program that synchronizes
only writes (or reads) may appear to work on some machines, but in this case,
appearances are deceiving.
     */
}