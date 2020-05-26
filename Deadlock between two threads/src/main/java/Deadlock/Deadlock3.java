package Deadlock;

import org. apache. log4j. Logger;

public class Deadlock3 {

    private static Logger logger = Logger.getLogger(Deadlock3.class);
    private static String odd;
    private static String even;

    public static void methodOdd() throws InterruptedException {
        synchronized (odd){
            odd+="1";
            logger. info("Method1 locked odd");
            Thread.sleep(500);
            synchronized (even){
                logger. info("Method1 locked even");
                Thread.sleep(500);
                even+="0";
                logger. info(Thread.currentThread().getId()+" "+odd+"\t\t"+even);
                methodEven();
            }
        }
    }

    public static void methodEven() throws InterruptedException {
        synchronized (even){
            logger. info("Method2 locked even");
            Thread.sleep(500);
            even+="0";
            synchronized (odd){
                logger. info("Method2 locked odd");
                odd+="1";
                Thread.sleep(500);
                logger. info(Thread.currentThread().getId()+" "+odd+"\t\t"+even);
                methodOdd();
            }
        }
    }
}
