package ServerStarter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

@Service
public class KafkaServer implements Runnable {
    private static final String[] kafkaBrokers = new String[]{"/home/shubham/ideaIU/Libraries/kafka_2.12-2.5.0/bin/kafka-server-start.sh","/home/shubham/ideaIU/Libraries/kafka_2.12-2.5.0/config/server.properties"};
    private static final String kafkaMatch = "INFO [ZooKeeperClient Kafka server] Connected. (kafka.zookeeper.ZooKeeperClient)";
    private CountDownLatch zookeeperLatch;
    private CountDownLatch allReady;
    private final int maxRuntime;

    KafkaServer(CountDownLatch zookeeperLatch, CountDownLatch allReady,int maxRuntime){
        this.allReady=allReady;
        this.zookeeperLatch=zookeeperLatch;
        this.maxRuntime=maxRuntime;
    }

    @Override
    public void run(){
        Process process = null;
        try {
            zookeeperLatch.await();
            System.out.println("STARTING KAFKA SERVER...");
            process = new ProcessBuilder(kafkaBrokers).start();
            var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.contains(kafkaMatch)){
                    allReady.countDown();
                    System.out.println(" KAFKA STARTED SUCCESSFULLY!");
                }
            }
            Thread.sleep(maxRuntime);
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println("KAFKA SHUTTING DOWN...");
            process.destroy();
        }
    }
}
