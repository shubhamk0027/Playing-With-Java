package com.kafka.kafa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
/*a single producer instance across threads will generally be faster than having multiple instances.*/
@SpringBootApplication
public class KafaApplication {
	@SpringBootApplication
	public class KafkaApplication implements ApplicationRunner {

		@Autowired
		private KafkaTemplate<String, String> kafkaTemplate;

		public void sendMessage(String topicName,String msg) {
			kafkaTemplate.send(topicName, msg);
		}

		@KafkaListener(topics="topic1",groupId = "group-id")
		public void  listen(String message){
			System.out.println("I am not called but still  listening!");
			System.out.println("Msg Rec. in group-id:"+message);
		}


		public void main(String[] args) {
			System.out.println("Application Running");
			SpringApplication.run(KafkaApplication.class, args);
		}

		@Override
		public void run(ApplicationArguments args) throws Exception{
			sendMessage("topic1","Hello Kafka");
		}

	}
	public static void main(String[] args) {
		SpringApplication.run(KafaApplication.class, args);
	}
	// ERROR: BROKER MAY NOT BE AVAILABLE!
}
