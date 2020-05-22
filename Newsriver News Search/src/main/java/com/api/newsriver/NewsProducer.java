package com.api.newsriver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.lucene.util.ThreadInterruptedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ThemeResolver;

import java.io.*;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
@Scope("prototype")
public class NewsProducer implements Runnable{

    private APIClient apiClient;
    private APIQuery apiQuery;

/*
    static PrintWriter printer;
    static {
        try {
            printer = new PrintWriter("output.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
*/

    private static final String Topic = "test";
    private static final Logger logger = LoggerFactory.getLogger(NewsProducer.class);
    private static Properties props = new Properties();
    private static ObjectMapper mapper = new ObjectMapper();
    private KafkaProducer <String,String> producer;

    private String queryString;
    private Instant from;
    private Instant to;
    private int maxCalls;

    static{
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    }

    NewsProducer(APIClient apiClient){
        this.apiClient=apiClient;
        producer = new KafkaProducer<>(props);
    }

    public void setQuery(String queryString, Instant from, Instant to, int maxCalls){
        this.queryString=queryString;
        this.from=from;
        this.to=to;
        this.maxCalls=maxCalls;

        apiQuery = new APIQuery.Builder()
                .limit(15)
                .setSortBy(APIQuery.SortBy.DISCOVER_DATE)
                .setSortOrder(APIQuery.SortOrder.ASC).build();

        logger.info("QUERY SET FOR THE API:");
        logger.info("QUERY:"+apiQuery.getLuceneQuery()+
                " LIMIT: "+apiQuery.getLimit()+" SORTBY: "+apiQuery.getSortBy()+
                " SORTORDER: "+apiQuery.getSortOrder());
    }

    public void sendWithCallback(ProducerRecord<String,String> record){
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if(e==null) {                     // Message Sent Successfully
                    logger.info("Sent to Kafka: "+
                            " Topic: "+record.topic() +
                            " Value "+record.value()+
                            " Key " +record.key() +
                            " Partition: "+recordMetadata.partition()+
                            " Offset: "+recordMetadata.offset()+
                            " TimeStamp: "+recordMetadata.timestamp()
                    );
                }else { logger.error("Error while sending message",e); }
            }
        });
    }


    @Override
    public void run() {
        if(apiQuery == null) throw new NullPointerException();
        logger.info("ADDED A PRODUCER!");
        int currCalls = 0;
        long id = Thread.currentThread().getId();
        try {
            while(from.compareTo(to) < 1 && currCalls < maxCalls) {

                apiQuery.setLuceneQuery(queryString + " AND discoverDate:[" + from.toString() + " TO " + to.toString() + "]");
                logger.info("PRODUCER " + id + " SENT QUERY " + apiQuery.getLuceneQuery());

                HttpResponse <String> response = apiClient.fetch(apiQuery);
                logger.info("PRODUCER " + id + " STATUS CODE: " + response.statusCode() + " MSG: " + response.body());
                // printer.println(response.body());

                List <APIResponse> res = mapper.readValue(response.body(), new TypeReference <List <APIResponse>>() {});
                for(APIResponse obj : res) {
                    ProducerRecord <String, String> record = new ProducerRecord <>(Topic, obj.id, mapper.writeValueAsString(obj));
                    sendWithCallback(record);
                }

                try{
                    from = Instant.parse(res.get(res.size() - 1).discoverDate);
                    from.plus(Duration.ofSeconds(1));
                }catch(Exception e){
                    // Just Increasing by some number to get valid data in next call
                    from.plus(Duration.ofSeconds(10));
                }

                currCalls++;
                logger.info("PRODUCER "+id+" MADE "+currCalls+" CALLS, NEXT TIME FRAME FROM "+from.toString()+" TO "+to.toString().toString());
            }
        } catch(Exception exception) {
            // printer.close();
            exception.printStackTrace();
            logger.info("SHUTTING DOWN THE PRODUCER, " + currCalls + " CALLS MADE");
            producer.close();
        }
    }

}
