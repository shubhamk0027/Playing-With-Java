package com.api.newsriver;

import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.internals.Topic;
import org.apache.lucene.queryparser.flexible.precedence.processors.PrecedenceQueryNodeProcessorPipeline;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class ESMapper {

    private PutMappingRequest putMappingRequest ;
    private static final Logger logger = LoggerFactory.getLogger(ESMapper.class);

    private ESMapper(@Value("${TOPIC}") String INDEX){
        putMappingRequest = new PutMappingRequest(INDEX);
        InputStream is = null;
        try {
            is = new FileInputStream("mapping.json");
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String fileAsString = sb.toString();
            putMappingRequest.source(fileAsString,XContentType.JSON);
            putMappingRequest.setTimeout(TimeValue.timeValueMinutes(2));
            // System.out.println("JSON FILE READ TO: "+ fileAsString);
        } catch(IOException e) {
            logger.info("ERROR OCCURED IN READING THE JSON FILE",e);
            e.printStackTrace();
        }
    }

    public PutMappingRequest getMappingRequest(){
        return putMappingRequest;
    }

}
