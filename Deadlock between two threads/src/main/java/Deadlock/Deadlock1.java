package Deadlock;

public class Deadlock1 {
    private static int num=0;

    synchronized static public void methodEven() throws InterruptedException {
            if(num>9) return;
            num++;
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+" Method1 num="+num);
            methodOdd();
    }
    synchronized public static void methodOdd() throws InterruptedException {
            if(num>9) return;
            num++;
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+" Method2 num="+num);
            methodEven();
    }
}
// sync(this) or sync method with this object both will work the same, by not alllowing other method involving this obj to be called
// adds locks on the resource
/*
      ExecutorService threadPool =  Executors.newFixedThreadPool(2);
        threadPool.submit(new Runnable() {
            @lombok.SneakyThrows
            public void run() {
                System.out.println("Hello");
                object.methodEven();
                System.out.println("Hello");
            }
        });

        threadPool.submit(new Runnable() {
            @lombok.SneakyThrows
            public void run() {
                System.out.println("Bello");
                object.methodOdd();
                System.out.println("Bello");
            }
        });
        FAILED!
 */
