package ServerStarter;

import com.api.newsriver.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class ServerStarter {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch zookeeperLatch = new CountDownLatch(1);
        CountDownLatch allReady = new CountDownLatch(3);

        int maxRuntimeWait = 60000;
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        threadPool.submit(new ZookeeperServer(zookeeperLatch,allReady,maxRuntimeWait));
        threadPool.submit(new KafkaServer(zookeeperLatch,allReady,maxRuntimeWait));
        threadPool.submit(new ElasticSearchServer(allReady,maxRuntimeWait));

        allReady.await();
        System.out.println("ALL SERVERS STARTED SUCCESSFULLY!");
    }

}
