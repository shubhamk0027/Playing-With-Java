package ServerStarter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerStarter {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch zookeeperLatch = new CountDownLatch(1);
        CountDownLatch allReady = new CountDownLatch(3);

        int maxRuntimeWait = 600000;
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        threadPool.submit(new ZookeeperServer(zookeeperLatch,allReady,maxRuntimeWait));
        threadPool.submit(new KafkaServer(zookeeperLatch,allReady,maxRuntimeWait));
        threadPool.submit(new ElasticSearchServer(allReady,maxRuntimeWait));

        allReady.await();
        System.out.println("All Servers Ready!");
    }

}
