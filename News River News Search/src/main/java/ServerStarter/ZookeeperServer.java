package ServerStarter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

public class ZookeeperServer implements Runnable {

    private static final String[] zookeeper = new  String[]{"/home/shubham/ideaIU/Libraries/kafka_2.12-2.5.0/bin/zookeeper-server-start.sh","/home/shubham/ideaIU/Libraries/kafka_2.12-2.5.0/config/zookeeper.properties"};
    private static final String zookeeperMatch = "INFO Using checkIntervalMs=60000 maxPerMinute=10000";
    private CountDownLatch zookeeperLatch;
    private CountDownLatch allReady;
    private final int maxRuntime;

    ZookeeperServer(CountDownLatch zookeeperLatch, CountDownLatch allReady,int maxRuntime) {
        this.allReady=allReady;
        this.zookeeperLatch=zookeeperLatch;
        this.maxRuntime=maxRuntime;
    }

    @Override
    public void run(){
        Process process = null;
        try {
            System.out.println("Starting Zookeeper Server...");
            process = new ProcessBuilder(zookeeper).start();
            var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.contains(zookeeperMatch)){
                    zookeeperLatch.countDown();
                    allReady.countDown();
                    System.out.println("Zookeeper Started Successfully!");
                }
            }
            Thread.sleep(maxRuntime);
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println("Shutting Down Zookeeper...");
            process.destroy();
        }
    }
}
