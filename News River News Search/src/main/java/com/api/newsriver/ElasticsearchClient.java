package com.api.newsriver;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ElasticsearchClient {

    private final RestHighLevelClient client;
    private final Logger logger = LoggerFactory.getLogger(ElasticsearchClient.class);
    private final CouchDB couchDB;

    private final int maxTries;
    private final double MULTIPLIER;
    private final int JITTER;
    private long currWait;

    private ElasticsearchClient(
            RestHighLevelClient client,
            CouchDB couchDB,
            @Value("${maxTries}") int maxTries,
            @Value("${MULTIPLIER}") double MULTIPLIER,
            @Value("${JITTER}") int JITTER
            ) {
        this.client = client;
        this.couchDB = couchDB;
        this.maxTries=maxTries;
        this.MULTIPLIER=MULTIPLIER;
        this.JITTER=JITTER;
        currWait=0;
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
    }

    private long nextWaitDuration(){
        currWait = (long) (currWait*MULTIPLIER + new Random().nextInt(JITTER));
        return currWait;
    }

    private void reset(){
        currWait=0;
    }

    public void createIndexRequest(String topic) throws InterruptedException {

        CreateIndexRequest request = new CreateIndexRequest(topic);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 3));


        InputStream is = null;
        // try maxTries times
        for(int tryCount = 0; tryCount < maxTries; tryCount++) {
            try {
                is = new FileInputStream("mapping.json");
                BufferedReader buf = new BufferedReader(new InputStreamReader(is));
                String line = buf.readLine();
                StringBuilder sb = new StringBuilder();
                while(line != null) {
                    sb.append(line).append("\n");
                    line = buf.readLine();
                }
                String fileAsString = sb.toString();
                request.source(fileAsString, XContentType.JSON);
                logger.info("Mapping Json: " + fileAsString);
                break; // Success
            } catch(IOException e) {
                if(tryCount == maxTries - 1) {
                    logger.info("FAILED to read mapping file", e);
                    e.printStackTrace();
                    return;
                }
            }
        }

        CreateIndexResponse createIndexResponse = null;
        // retry using exponential backoff
        while(true) {
            try {
                createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
                boolean acknowledged = createIndexResponse.isAcknowledged();
                if(!acknowledged) {
                    logger.error("FAILED create index request acknowledgement by some nodes!");
                    continue;
                }
                boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
                if(!shardsAcknowledged) {
                    logger.info("Failed shard acknowledgement by some nodes at {}", LocalDateTime.now());
                    continue;
                }
                // success
                reset();
                break;
            } catch(IOException | NullPointerException e) {
                logger.info("FAILED to create index");
                e.getStackTrace();
                Thread.sleep(nextWaitDuration());
            }
        }
    }

    /**
     * handle failures, Potential failures->
     * Not able to insert in all shards -> Retry sending once more
     * Not able to insert into any shard -> retry once more
     * IO exp-> No response from server-> Try once again
     * ES exception-> BadRequest(bad syntax) don't try
     * other -> try once again
     */

    public void add(String index, String key, String jsonString)  {
        reset(); // before starting to send a request
        IndexRequest indexRequest = new IndexRequest(index)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
                .source(jsonString, XContentType.JSON);
        indexRequest.id(key);
        indexRequest.timeout("1s");
        // try maxTries times with exponential backoff!
        IndexResponse indexResponse=null;
        try {
            for(int tryCount = 0; tryCount < maxTries; tryCount++) {
                try {
                    indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                    ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                    break; // success
                } catch(ElasticsearchException e) {
                    if(tryCount == maxTries - 1 || e.status() == RestStatus.CONFLICT || e.status() == RestStatus.BAD_REQUEST) {
                        e.getStackTrace();
                        logger.info("FAILED to send response to Elasticsearch at {}", LocalDateTime.now());
                        couchDB.dump(index,key,jsonString);
                    } else Thread.sleep(nextWaitDuration());
                } catch(IOException e) {
                    if(tryCount == maxTries - 1) {
                        e.getStackTrace();
                        couchDB.dump(index,key,jsonString);
                        break;
                    } else  Thread.sleep(nextWaitDuration());
                }
            }
        }catch(InterruptedException e){
            logger.info("FAILED to send request to Elastic Search");
            couchDB.dump(index,key,jsonString); // dump and log
        }
    }

    @PreDestroy
    public void destroy() throws IOException {
        client.close();
    }
}
