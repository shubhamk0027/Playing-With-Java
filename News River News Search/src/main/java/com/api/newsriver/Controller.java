package com.api.newsriver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Controller {

    private final ObjectFactory <NewsConsumer> newsConsumerFactory;
    private final ObjectFactory <NewsProducer> newsProducerFactory;
    private final Logger logger = LoggerFactory.getLogger(Application.class);
    private final int rateLimit;
    private final AtomicInteger consumedCount;

    private Integer producedCount;
    private ExecutorService producerPool;

    private Controller(
            ObjectFactory <NewsConsumer> newsConsumerFactory,
            ObjectFactory <NewsProducer> newsProducerFactory,
            @Value("${COUNTER}") int counter,
            @Value("${rateLimit}") int rateLimit) {
        this.newsConsumerFactory = newsConsumerFactory;
        this.newsProducerFactory = newsProducerFactory;
        this.consumedCount = new AtomicInteger(counter);
        this.rateLimit = rateLimit;
        producedCount=0;
    }

    /* HANDLE MULTIPLE CONSUMERS */

    public int getConsumedCount() {
        return consumedCount.incrementAndGet();
    }

    public void consume(int nConsumers) {
        ExecutorService consumerPool = Executors.newFixedThreadPool(nConsumers);
        for(int i = 0; i < nConsumers; i++) {
            consumerPool.execute(newsConsumerFactory.getObject());
        }
    }

    /* MULTIPLE PRODUCERS, RATE LIMITER AND CIRCUIT BREAKER */

    public boolean canCall() {
        boolean res = true;
        synchronized (producedCount) {
            producedCount++;
            if(producedCount > rateLimit) res = false;
        }
        return res;
    }


    public void produce(String queryString, int lastXdays, int nThreads) {
        if(nThreads < 1 || lastXdays < 1) throw new InvalidParameterException();

        long A = lastXdays * 86400;
        long D = A / nThreads;
        Instant from = Instant.now();

        List <Instant> times = new ArrayList <>();
        List <NewsProducer> producers = new ArrayList <>();

        times.add(from);
        for(int i = 0; i < nThreads; i++) {
            times.add(times.get(i).minus(Duration.ofSeconds(D)));
            producers.add(newsProducerFactory.getObject());
            producers.get(i).setQuery(queryString, times.get(i + 1), times.get(i));
            logger.info("PRODUCER " + (i + 1) + " FROM " + times.get(i + 1) + " TO " + times.get(i));
        }

        producerPool = Executors.newFixedThreadPool(nThreads);
        for(int i = 0; i < nThreads; i++) {
            producerPool.execute(producers.get(i));
        }
    }

}
    /*Fetch news by equal distribution of the work*/
    /*public void produceWithEqualDistributionOfWork(String queryString, int lastXdays, int nThreads) {
        if(nThreads < 1 || lastXdays < 1) throw new InvalidParameterException();

        long A = lastXdays * 86400;
        long D = A / nThreads;
        Instant from = Instant.now();

        List <Instant> times = new ArrayList <>();
        List <NewsProducer> producers = new ArrayList <>();

        times.add(from);
        for(int i = 0; i < nThreads; i++) {
            times.add(times.get(i).minus(Duration.ofSeconds(D)));
            producers.add(newsProducerFactory.getObject());
            producers.get(i).setQuery(queryString, times.get(i + 1), times.get(i), rateLimit / nThreads);
            logger.info("PRODUCER " + (i + 1) + " FROM " + times.get(i + 1) + " TO " + times.get(i) + " ATMOST " + rateLimit / nThreads + " CLASS");
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
        for(int i = 0; i < nThreads; i++) {
            threadPool.execute(producers.get(i));
        }
    }*/

/*AtomicInteger incrementAndGet() is not same as  AtomicInt++ then return atomicInt*/
// this.consumedCount.set(counter); assign before set
        /* While synchronizing a non final obj, Note that If the object reference changes, the same section of code may be run in parallel.
        i.e., if one thread runs the code in the synchronized block and someone calls setO(...),
        another thread can run the same synchronized block on the same instance concurrently.*/

