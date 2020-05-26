package com.api.newsriver;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.AcknowledgedResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class ElasticsearchClient {

    private final RestHighLevelClient client;
    private final ESMapper esMapper;
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchClient.class);

    public ElasticsearchClient(ESMapper esMapper) throws IOException {
        this.esMapper = esMapper;

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
        client =  RestClients.create(clientConfiguration).rest();

        org.elasticsearch.action.support.master.AcknowledgedResponse putMappingResponse =
                client.indices().putMapping(esMapper.getMappingRequest(), RequestOptions.DEFAULT);
        boolean acknowledged = putMappingResponse.isAcknowledged();

        if(acknowledged){
            logger.info("MAPPING SUCCESSFULLY ACKNOWLEDGED!");
        }else{
            logger.error("MAPPER NOT REGISTERED!");
        }
    }

    public void add(String index,String jsonString) throws IOException {
        IndexRequest indexRequest = new IndexRequest(index)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
                .source(jsonString, XContentType.JSON);

        IndexResponse indexResponse = client.index(
                indexRequest,
                RequestOptions.DEFAULT
        );
    }

    public void close() throws IOException { client.close(); }
}
// https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.clients