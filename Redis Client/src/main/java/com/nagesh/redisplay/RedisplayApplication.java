package com.shubh.redisplay;

import org.redisson.client.RedisAskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
public class RedisplayApplication {

    private final RedisClient redisClient;
    private final static Logger logger = LoggerFactory.getLogger(RedisplayApplication.class);

    RedisplayApplication(RedisClient redisClient){
        this.redisClient=redisClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(RedisplayApplication.class, args);
    }

    private static final AtomicLong counter = new AtomicLong(0);
    private static final String MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    public static String generateNextURL(){

        long num = counter.incrementAndGet();
        String binary = Long.toBinaryString((1L<<35)|num).substring(1); // to binary with padding 0s
        binary = new StringBuilder(binary).reverse().toString();           // swapping ith with 35-1-ith character
        num =  Long.parseLong(binary,2);                             // back to integer

        StringBuilder url= new StringBuilder();
        for(int i=0;i<6;i++){
            long SZ = 62;
            url.append(MAP.charAt((int) (num%SZ)));
            num=num/SZ;
        }

        String newURL = url.reverse().toString();
        System.out.println(newURL);
        return newURL;
    }

    @Bean
    CommandLineRunner commandLineRunner(){
        return args -> {
            generateNextURL();
            generateNextURL();
            generateNextURL();
            generateNextURL();
            generateNextURL();
            generateNextURL();
        };
    }
}
