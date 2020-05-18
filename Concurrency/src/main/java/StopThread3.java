import java.util.concurrent.TimeUnit;

// Cooperative thread termination with a volatile field Better than ST2
public class StopThread3 {
    private static volatile boolean stopRequested;
    // Volatile->any thread will read the most recent value!
    public static void main(String[] args)
            throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested)
                i++;
        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
}
/*

Broken - requires synchronization!
private static volatile int nextSerialNumber = 0;
public static int generateSerialNumber() {
    return nextSerialNumber++;
}

The language specification guarantees that reading or writing a variable is atomic unless
the variable is of type long or double BUT! Operations on it is not
Increment operator (++) is not atomic. It performs two operations on the nextSerialNumber field:
first it reads the value, and then it writes back a new value, equal to the old value plus one.

Solution:
// Lock-free synchronization with java.util.concurrent.atomic
private static final AtomicLong nextSerialNum = new AtomicLong();
    public static long generateSerialNumber() {
    return nextSerialNum.getAndIncrement();
}
*/