package com.springfactory.springfactory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class SpringfactoryApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("System Tests Running");
    }

    @Test
    public void googleTest() throws Exception {
        APIInterface api = APIFactory.getAPI("GOOGLE-API");
        assertEquals(api.detect("TEXT"),"DONE");
        assertEquals(api.name(),"GOOGLE-API");
        assertThrows(NullPointerException.class,()->api.detect(null));
    }

    @Test
    public void twitterTest() throws Exception {
        APIInterface api = APIFactory.getAPI("TWITTER-API");
        assertEquals(api.detect("TEXT"),"DONE");
        assertEquals(api.name(),"TWITTER-API");
        assertThrows(NullPointerException.class,()->api.detect(null));
    }

    @Test
    public void redditTest() throws Exception {
        APIInterface api = APIFactory.getAPI("REDDIT-API");
        assertEquals(api.detect("TEXT"),"DONE");
        assertEquals(api.name(),"REDDIT-API");
        assertThrows(NullPointerException.class,()->api.detect(null));
    }

}
