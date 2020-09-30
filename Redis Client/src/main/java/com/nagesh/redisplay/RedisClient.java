package com.shubh.redisplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.concurrent.atomic.AtomicLong;


@Service
public class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private final ObjectMapper mapper= new ObjectMapper();
    private final RedissonClient redissonClient;

    private final AtomicLong counter;
    private final int LEN;


    RedisClient(@Value("${LEN}") int LEN){

        this.LEN=LEN;

        Config config= new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        redissonClient= Redisson.create(config);

        // Load/Store the counter from the redis
        counter = new AtomicLong(loadCounter());
    }


    private long loadCounter(){
        RBucket<Long> counterBucket = redissonClient.getBucket("COUNTER_VALUE");
        if(!counterBucket.isExists()) {
            logger.info("No initial counter value found. Starting it from 0");
            return 0;
        }
        logger.info("Loading counter value from "+counterBucket.get());
        return counterBucket.get();
    }

    public void saveString(String x) throws Exception {
        RBucket<String> rBucket = redissonClient.getBucket(x);
        if(rBucket.isExists()) {
            logger.info("Duplicate generated!!!!! for "+x);
            throw new Exception("Duplicate found!");
        }
        rBucket.set(x);
    }


    public String generateURL(){
        long num = counter.incrementAndGet();
        StringBuilder url= new StringBuilder();

        String binary = Long.toBinaryString((1L<<47)|num).substring(1);
        logger.info("Length " +binary.length());
        logger.info(binary);
        binary = new StringBuilder(binary).reverse().toString();
        logger.info(binary);
        num =  Long.parseLong(binary,2);
        logger.info("Num"+num);

        for(int i=0;i<LEN;i++){
            long SZ = 62;
            String INDICES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            url.append(INDICES.charAt((int) (num%SZ)));
            num=num/SZ;
        }

        return url.reverse().toString();
    }

    // For writing 10000 ints to redis 1121 elapsed!
    public void SpeedTestWrite(int limit){
        StopWatch watch = new StopWatch();
        watch.start();
        for(int i = 0; i<limit; i++){
            RBucket<Integer> rBucket= redissonClient.getBucket(Integer.toString(i));
            rBucket.set(i);
        }
        watch.stop();
        logger.info("For writing "+limit+" ints to redis "+watch.getTotalTimeMillis()+" elapsed! ");
    }

    // For reading 10000 ints to redis 862 elapsed! one operation 0.1mill per op
    public void SpeedTestRead(int limit){
        StopWatch watch = new StopWatch();
        watch.start();
        for(int i = 0; i<limit; i++){
            RBucket<Integer> rBucket= redissonClient.getBucket(Integer.toString(i));
            rBucket.get();
        }
        watch.stop();
        logger.info("For reading "+limit+" ints to redis "+watch.getTotalTimeMillis()+" elapsed! ");
    }

}

// https://stackoverflow.com/questions/30087921/redis-best-way-to-store-a-large-map-dictionary/30094048#:~:text=Prefer%20HSET%20besides%20of%20KEYS,if%20memory%20is%20main%20target.
// Java collections in Redisson are handled the same way as objects. This allows you to perform lock-free, thread-safe, and atomic operations on the Java collections in Redisson.
// https://dzone.com/articles/introducing-redisson-live-object-object-hash-mappi
// https://www.alibabacloud.com/blog/interview-with-the-creator-of-redisson-building-an-open-source-enterprise-redis-client_593854
// https://instagram-engineering.com/storing-hundreds-of-millions-of-simple-key-value-pairs-in-redis-1091ae80f74c