package org.cham.springkafkatemplatesample;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.cham.springkafkatemplatesample.producer.KafkaProducer;
import org.cham.springkafkatemplatesample.util.OrderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication
@ComponentScan
@Slf4j
public class SpringKafkaTemplateSampleApplication implements CommandLineRunner {
	@Autowired
	private KafkaProducer kafkaProducer;
	private final Gson gson = new Gson();
	@Value("${test.topic}")
	private String topic;
	private OrderFactory orderFactory = new OrderFactory();
	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaTemplateSampleApplication.class, args);
	}
	@Override
	public void run(String... args) {
		log.info("Inside the run method of the main class - SpringKafkaTemplateSampleApplication");
		kafkaProducer.send(topic,gson.toJson(orderFactory.createDummyOrder()));
	}

}
