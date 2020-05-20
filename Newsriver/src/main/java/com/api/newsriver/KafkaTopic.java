package com.api.newsriver;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafkaTopic {
    private static Properties props = new Properties();

    static{
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:8080"); // admin client at 8080!
        props.put(AdminClientConfig.CLIENT_ID_CONFIG,"test");
    }

    public static void createTopic(String topicName) throws IOException {
        AdminClient adminClient = AdminClient.create(props);
        NewTopic newTopic = new NewTopic(topicName, 1, (short)1);
        List<NewTopic> newTopics = new ArrayList<NewTopic>();
        newTopics.add(newTopic);
        adminClient.createTopics(newTopics);
        adminClient.close();
        System.out.println("Topic "+topicName+" created");
    }
}
