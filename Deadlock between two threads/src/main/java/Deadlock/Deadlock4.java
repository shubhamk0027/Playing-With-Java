package Deadlock;

public class Deadlock4 {
    static String odd;
    static String even;

    public static void methodOdd() throws InterruptedException {
        synchronized (odd) {
            System.out.println(Thread.currentThread().getId() + " Method1 locked odd");
            odd += "1";
            Thread.sleep(500);
            synchronized (even) {
                 System.out.println(Thread.currentThread().getId() + " Method1 locked even");
                 Thread.sleep(500);
                 even += "0";
                 System.out.println(Thread.currentThread().getId() + " Method1 released even");
             }
             System.out.println(Thread.currentThread().getId() + " Method1 released odd");
            }
        }

    public static void methodEven() throws InterruptedException {
        synchronized (even) {
            System.out.println(Thread.currentThread().getId() + " Method2 locked even");
            Thread.sleep(500);
            even += "0";
            synchronized (odd) {
                System.out.println(Thread.currentThread().getId() + " Method2 locked odd");
                odd += "1";
                Thread.sleep(500);
                methodOdd();
                System.out.println(Thread.currentThread().getId() + " Method2 released odd");
            }
            System.out.println(Thread.currentThread().getId() + " Method2 released even");
        }
    }
}
