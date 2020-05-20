package com.api.newsriver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

@SpringBootApplication
public class NewsriverApplication {

	final APIClientService apiClientService;

	public NewsriverApplication(APIClientService apiClientService) {
		this.apiClientService = apiClientService;
	}

	public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
		SpringApplication.run(NewsriverApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx){
		/* Without setting application context the apiclient will not get autowired!
		* And have to run the non static services here, since main is static*/
		return args -> {
			System.out.println(".................................App Output...................................");
			// apiClientService.getQueries();
			// kafkaService.sendMessages();
			// kafkaService.readMessages();
		};
	}

}
/*  constructor is called before any @Autowired fields are set, so autowired fields are null at that point */