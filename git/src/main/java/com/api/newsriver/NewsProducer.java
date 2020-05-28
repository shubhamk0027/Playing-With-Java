package com.api.newsriver;

import ch.qos.logback.classic.pattern.ClassNameOnlyAbbreviator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.JacksonYAMLParseException;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.lucene.util.ThreadInterruptedException;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.*;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.Random;

@Component
@Scope("prototype")
public class NewsProducer implements Runnable {

    private final APIClient apiClient;
    private final KafkaProducer <String, String> producer;
    private final Controller controller;
    private final String Topic;
    private final int newsLimitPerCall;
    private final double MULTIPLIER;
    private final int JITTER;

    private static final Logger logger = LoggerFactory.getLogger(NewsProducer.class);
    private static final Properties props = new Properties();
    private static final ObjectMapper mapper = new ObjectMapper();

    private APIQuery apiQuery;
    private String queryString;
    private Instant from;
    private Instant to;
    private long currWait;

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    }

    private NewsProducer(
            APIClient apiClient,
            Controller controller,
            @Value("${TOPIC}") String topic,
            @Value("${newsLimitPerCall}") int newsLimitPerCall,
            @Value("${MULTIPLIER}") double MULTIPLIER,
            @Value("${JITTER}") int JITTER) {
        producer = new KafkaProducer <>(props);
        this.apiClient = apiClient;
        this.controller = controller;
        this.Topic = topic;
        this.newsLimitPerCall = newsLimitPerCall;
        this.MULTIPLIER = MULTIPLIER;
        this.JITTER = JITTER;
        currWait = 0;
    }

    // SET UP THE BASIC QUERY STRUCTURE SENT BY THIS PRODUCER CALL TO API
    public void setQuery(String queryString, Instant from, Instant to) {
        this.queryString = queryString;
        this.from = from;
        this.to = to;

        apiQuery = new APIQuery.Builder()
                .limit(newsLimitPerCall)
                .setSortBy(APIQuery.SortBy.DISCOVER_DATE)
                .setSortOrder(APIQuery.SortOrder.ASC).build();
        // logger.info("BASE QUERY: " + apiQuery.getLuceneQuery() + " LIMIT: " + apiQuery.getLimit() + " SORT-BY: " + apiQuery.getSortBy() + " SORT-ORDER: " + apiQuery.getSortOrder());
    }

    private long nextWaitDuration() {
        currWait = (long) (currWait * MULTIPLIER + new Random().nextInt(JITTER));
        return currWait;
    }

    private void reset() {
        currWait = 0;
    }

    @Override
    public void run() {
        long id = Thread.currentThread().getId();
        try {
            while(from.compareTo(to) < 1 && controller.canCall()) {
                try {
                    apiQuery.setLuceneQuery(queryString + " AND discoverDate:[" + from.toString() + " TO " + to.toString() + "]");
                    HttpResponse <String> response = apiClient.fetch(apiQuery);
                    // logger.info("ProducerID " + id + " Query: " + apiQuery.getLuceneQuery() + " Status: " + response.statusCode() + " Response: " + response.body());
                    if(response.statusCode() == 400) {
                        logger.info("FAILED to send a good request, Got 400");
                        throw new InterruptedException();
                    }
                    if(response.statusCode() != 200) {
                        logger.info("FAILED to receive valid response, Status code" + response.statusCode());
                        throw new InterruptedException();
                    }
                    List <APIResponse> res = mapper.readValue(response.body(), new TypeReference <List <APIResponse>>() {
                    });
                    for(APIResponse obj : res) {
                        ProducerRecord <String, String> record = new ProducerRecord <>(Topic, obj.id, mapper.writeValueAsString(obj));
                        producer.send(record);
                    }
                    from = Instant.parse(res.get(res.size() - 1).discoverDate);
                    from.plus(Duration.ofMillis(1));
                    reset(); // success
                    break;
                } catch(InterruptedException | JsonProcessingException e) {
                    e.printStackTrace();
                    logger.info("FAILED to get parse response at {}", LocalDateTime.now());
                    Thread.sleep(nextWaitDuration());
                } catch(IOException | URISyntaxException e) {
                    logger.info("FAILED to send request  at {}", LocalDateTime.now());
                    e.printStackTrace();
                    Thread.sleep(nextWaitDuration());
                }
            }
        }catch(InterruptedException e){
            logger.info("FAILED to busy wait for next call time");
            e.getStackTrace();
        }
    }

    @PreDestroy
    private void destroy() {
        producer.close();
    }

}

/*  Simple back off strategy: try x times in a for loop
 * Better: use exponential backoff
 * Best: exponential backoff + Jitter
 **/
