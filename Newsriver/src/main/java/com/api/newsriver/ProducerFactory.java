package com.api.newsriver;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class ProducerFactory {

    private final KafkaProducer<String,String> producer;
    private static Properties props = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(ProducerFactory.class);

    static{
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:2181");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    }

    ProducerFactory(){ producer = new KafkaProducer<>(props);}

    public void send(ProducerRecord<String,String> record){ producer.send(record);}

    public void sendWithCallback(ProducerRecord<String,String> record){
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if(e==null) {                     // Message Sent Successfully
                    logger.info("Recieved new metadata.\n"+
                            "Topic: "+recordMetadata.topic()+
                            "Partition: "+recordMetadata.partition()+
                            "Offset: "+recordMetadata.offset()+
                            "TimeStamp: "+recordMetadata.timestamp());
                }else { logger.error("Error while sending message",e); }
            }
        });
    }

    public void close(){ producer.close(); }
}

/**
 * send is asynchronous. We need to close the producer to make sure it is sent
 * Producer->   A Kafka client that publishes records to the Kafka cluster.
 * single producer instance across threads will generally be faster than having multiple instances.
 Producer<String, String> producer = new KafkaProducer<>(props);
 for (int i = 0; i < 100; i++)
 producer.send(new ProducerRecord<String, String>("my-topic", Integer.toString(i), Integer.toString(i)));
 producer.close();
 */
