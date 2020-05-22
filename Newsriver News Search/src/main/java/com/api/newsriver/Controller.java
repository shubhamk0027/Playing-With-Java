package com.api.newsriver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Controller {

    private final ElasticsearchClient elasticsearchClient;
    private final ObjectFactory<NewsConsumer> newsConsumerFactory;
    private final ObjectFactory<NewsProducer> newsProducerFactory;
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    Controller(ElasticsearchClient elasticsearchClient,
               ObjectFactory<NewsConsumer> newsConsumerFactory,
               ObjectFactory<NewsProducer> newsProducerFactory){
        this.elasticsearchClient = elasticsearchClient;
        this.newsConsumerFactory= newsConsumerFactory;
        this.newsProducerFactory= newsProducerFactory;
    }

    public void consume(int nConsumers) {
        ExecutorService threadPool = Executors.newFixedThreadPool(nConsumers);
        for(int i=0;i<nConsumers;i++){
            threadPool.execute(newsConsumerFactory.getObject());
        }
    }

    public void produce(String queryString,int lastXdays, int nThreads){
        if( nThreads<1 || lastXdays<1) throw new InvalidParameterException();

        long A = lastXdays*86400;
        long D = A/nThreads;
        Instant from = Instant.now();

        List<Instant> times = new ArrayList <>();
        List<NewsProducer> producers = new ArrayList <>();

        times.add(from);
        for(int i=0;i<nThreads;i++){
            times.add(times.get(i).minus(Duration.ofSeconds(D)));
            producers.add(newsProducerFactory.getObject());
            producers.get(i).setQuery(queryString,times.get(i+1),times.get(i),20/nThreads);
            logger.info("PRODUCER "+(i+1)+" FROM "+times.get(i+1)+" TO "+times.get(i)+ " ATMOST "+20/nThreads+" CALSS");
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
        for(int i=0;i<nThreads;i++){
            threadPool.execute(producers.get(i));
        }
    }
}

