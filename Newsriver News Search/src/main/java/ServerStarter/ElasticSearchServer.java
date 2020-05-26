package ServerStarter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

@Service
public class ElasticSearchServer implements Runnable {
    private static final String[] elasticsearch = new String[]{"/home/shubham/ideaIU/Libraries/elasticsearch-7.6.2/bin/elasticsearch"};
    private static final String elasticsearchMatch ="Active license is now [BASIC]; Security is disabled";
    private CountDownLatch allReady;
    private final int maxRuntime;

    ElasticSearchServer(CountDownLatch allReady,int maxRuntime){
        this.allReady=allReady;
        this.maxRuntime=maxRuntime;
    }

    @Override
    public void run(){
        Process process = null;
        try {
            System.out.println("STARTING ELASTICSEARCH...");
            process = new ProcessBuilder(elasticsearch).start();
            var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.contains(elasticsearchMatch)){
                    allReady.countDown();
                    System.out.println("ELASTICSEARCH STARTED SUCCESSFULLY!");
                }
            }
            Thread.sleep(maxRuntime);
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println("ELASTICSEARCH SHUTTING DOWN...");
            process.destroy();
        }
    }

}
