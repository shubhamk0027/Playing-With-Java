package com.api.newsriver;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

@Component
public class ConsumerFactory {

    private static Properties props = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(ProducerFactory.class);
    private final KafkaConsumer<String,String> consumer;

    static{
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:2181");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"test");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest"); // or can be latest to read the latest msg
    }

    ConsumerFactory(){ consumer= new KafkaConsumer<String, String>(props); }

    void subscribe(ArrayList<String> topics){
        consumer.subscribe(topics);
    }

    public void read(){
/*        while(true){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String,String>record: records){
                logger.info("Read a record\n: Key: "+record.key()+" Value: "+record.value());
                logger.info("Partition: "+record.partition()+" Offset: "+record.offset());
            }
        }*/
    }
}
