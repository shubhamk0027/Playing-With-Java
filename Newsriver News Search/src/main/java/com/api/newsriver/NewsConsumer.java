package com.api.newsriver;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.lucene.util.ThreadInterruptedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
@Scope("prototype")
public class NewsConsumer implements Runnable{

    private ElasticsearchClient elasticsearchClient;
    private String topic;
    private KafkaConsumer <String, String> consumer;
    private static final Logger logger = LoggerFactory.getLogger(NewsConsumer.class);
    private static Properties props = new Properties();
    private Controller controller;

    static {
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"CG1");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
    }

    NewsConsumer(ElasticsearchClient elasticsearchClient, Controller controller, @Value("${TOPIC}") String topic){
        this.controller=controller;
        this.elasticsearchClient = elasticsearchClient;
        this.topic=topic;
        consumer= new KafkaConsumer <>(props);
        List <String> topics = new ArrayList<String>();
        topics.add(topic);
        consumer.subscribe(topics);
    }

    @Override
    public void run() {
        try{
            logger.info("NEW CONSUMER ADDED!");
            while(true){
                ConsumerRecords <String,String> records = consumer.poll(Duration.ofMillis(100));
                for(ConsumerRecord <String,String> record: records){
                    logger.info("CONSUMED BY "+Thread.currentThread().getId()+" KEY: "+record.key());
                    String json = record.value();
                    int count = controller.getCount();
                    json="{\"number\":"+ Integer.toString(count)+","+json.substring(1);
                    logger.info(json);
                    elasticsearchClient.add(topic,json);
                }
            }
        }catch (WakeupException | IOException | ThreadInterruptedException e) {
            logger.info("SHUTTING DOWN CONSUMER "+Thread.currentThread().getId());
        }finally{
            consumer.close();
            consumer.wakeup();
        }
    }

}

/**
 * WakeUpException when preemption of a blocking operation by an external thread
 * Use bean factory for creating new beans CREATES A NEW BEAN
 * Executor service.shutdown does not shuts down the threads, but stops further tasks from gettting scheduled
 * For proper termination, add Shutdown hook or handle thread interruption
 * Avoid basic spring injection
 * @Component is a class level annotation where as
 * @Bean is a method level annotation and name of the method serves as the bean name
 * A message to a consumer group can be recieved by any one of the consumers in that group
 * ERROR: The group member needs to have a valid member id before actually entering a consumer group
 * Kafka internally creates a topic to store offsets.
 * This configuration controls the number of partitions for the offset topic.
 * Replication factor should be less than kafa-brokers
 */
