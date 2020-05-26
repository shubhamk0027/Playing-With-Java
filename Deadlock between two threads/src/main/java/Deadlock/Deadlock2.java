package Deadlock;

public class Deadlock2 {
        private static int num=0;

        synchronized static public void methodEven() throws InterruptedException {
            if(num>9) return;
            num++;
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+" Method1 num="+num);
            methodOdd();
        }
        public static void methodOdd() throws InterruptedException {
            if(num>9) return;
            num++;
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+" Method2 num="+num);
            methodEven();
        }
        /*
        public class DeadlockClass2 {
        private int num;
        DeadlockClass2(){num=0;}

        synchronized public static void methodEven() throws InterruptedException {
            if(num>9) return;
            num++;
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+" Method1 num="+num);
            methodOdd();
        }
        public static void methodOdd() throws InterruptedException {
            if(num>9) return;
            num++;
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+" Method2 num="+num);
            methodEven();
        }
         */
/*In Java, synchronized locks are re-entrant.
Or in other words if your thread already holds the lock on an object it doesn't have to wait on itself.
 */
}
/*ExecutorService threadPool =  Executors.newFixedThreadPool(2);
        threadPool.submit(new Runnable() {
            @lombok.SneakyThrows
            public void run() {
                System.out.println("Hello");
                object.methodEven();
                System.out.println("Hello");
            }
        });
 */