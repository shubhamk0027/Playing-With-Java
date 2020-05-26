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
	private final ElasticsearchClient esClient;
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public Application(Controller controller, ElasticsearchClient esClient, @Value("${TOPIC}") String topic) {
		this.esClient=esClient;
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

			/*String jsonString ="{\"id\":\"id1\","+
					"\"discoverDate\":\"2020-05-24T10:35:34.688+0000\","+
					"\"title\":\"JSON Pretty\","+
					"\"text\":\"Way back in January\","+
					"\"url\":\"https://www.twincities.com\","+
					"\"score\":8.394664}";

			logger.info("Adding to ES: "+ jsonString);
			esClient.add("news",jsonString);*/

		};
	}

}

/**
 * --------------------------CLASSES OVERVIEW-------------------------------------------------------------------
 * APIQuery (Query Objects) | APIResponse (ResponseObjects|For Jackson Serializing and Deserializing)
 * APIClient (to fetch the api from APIQuery)
 * NewsProducer (using apiClient send records to kafka after Serialization/Deserialization)
 * ESClient (client to connect to Elastic Search)
 * NewsConsumer(send from kafka to Elastic Search)
 * Controller(Does the magic, and runs n producers and across m consumers reading last XDays news)
 * -----------------------------------------------------------------------------------------------------------
 * Autowire is just like passing a parameter to the spring bean but we do not need to write the calling paramters
 * constructor is called before any @Autowired fields are set, so autowired fields are null at that point
 * A message to a consumer group can be recieved by any one of the consumers in that group
 * Kafka internally creates a topic to store offsets.
 * This configuration controls the number of partitions for the offset topic.
 * Replication factor should be less than kafa-brokers
 */

