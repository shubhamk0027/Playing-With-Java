package com.springfactory.springfactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@SpringBootApplication
@RestController

/*
* This application can be implemented from ApplicationRunner/CommandLineRunner
* @Value("${spring.application.name}") can be explicty use to specify the environment for the app
* @Autowired on AppContext  Will setup the context on its own!
* Can change the logger by->
*   import org.slf4j.Logger;
*   import org.slf4j.LoggerFactory;
* private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);
* The @RestController annotation is used to define the RESTful web services.
* It serves JSON, XML and custom response.
* @RequestMapping annotation is used to define the Request URI
*
*/

public class SpringfactoryApplication implements ApplicationRunner {

    @Autowired
    private ApplicationContext appContext;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringfactoryApplication.class,args);
        /*
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.springfactory.springfactory");
        context.refresh();
        */

        // The spring is automatically sending all the components as a list to the APIFactory constructor!
        /*APIFactory apiFactory= context.getBean(APIFactory.class);
        GoogleAPI googleApi = (GoogleAPI) apiFactory.getAPI("GOOGLE-API");
        System.out.println(googleApi.detect("Hello"));*/
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] beans = appContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        for(String bean:beans){
            System.out.println(bean);
        }
    }
}
