package com.api.newsriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	private final Controller controller;
	public Application(Controller controller) {
		this.controller = controller;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx){
		return args -> {
			// across M Producers X N consumers with max 20 calls to Newsriver in total
			controller.produce("cyclone",3,3);
			controller.consume(3);
		};
	}

}

/**
 * --------------------------CLASSES OVERVIEW-------------------------------------------------------------------
 * APIQuery (Query Objects) | APIResponse (ResponseObjects|For Jackson Serializing and De-serializing)
 * APIClient (to fetch the api from APIQuery)
 * NewsProducer (using apiClient send records to kafka after Serialization/Deserialization)
 * ESClient (client to connect to Elastic Search and send records)
 * NewsConsumer(send from kafka to Elastic Search)
 * Controller(Does the magic, and runs n producers and across m consumers reading last XDays news, Handles Rate limits)
 * -----------------------------------------------------------------------------------------------------------
 * Autowire is just like passing a parameter to the spring bean but we do not need to write the calling paramters
 * constructor is called before any @Autowired fields are set, so autowired fields are null at that point
 * A message to a consumer group can be recieved by any one of the consumers in that group
 * Kafka internally creates a topic to store offsets.
 * This configuration controls the number of partitions for the offset topic.
 * Replication factor should be less than kafa-brokers
 */

