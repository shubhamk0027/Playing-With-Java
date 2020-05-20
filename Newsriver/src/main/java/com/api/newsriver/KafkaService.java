package com.api.newsriver;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class KafkaService {

    private static ArrayList<String> TOPICS = new ArrayList<>();

    KafkaService() throws IOException {
        TOPICS.add("Trial");
        KafkaTopic.createTopic("Trial");
    }

    public void sendMessages(){
        ProducerFactory producer = new ProducerFactory();
        String Topic= TOPICS.get(0);
        for(int i=0;i<10;i++){
            String key =  "ID"+Integer.toString(i+1);
            String value = "This message is from "+Integer.toString(i+1)+ " key";
            ProducerRecord<String,String> record = new ProducerRecord<String, String>(Topic,key,value);
            producer.sendWithCallback(record);
        }
        producer.close();
    }

    public void readMessages(){
        ConsumerFactory consumer = new ConsumerFactory();
        consumer.subscribe(TOPICS);
        consumer.read();
    }
}

