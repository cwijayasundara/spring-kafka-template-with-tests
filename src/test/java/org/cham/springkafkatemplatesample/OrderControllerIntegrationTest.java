package org.cham.springkafkatemplatesample;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cham.springkafkatemplatesample.domain.Order;
import org.cham.springkafkatemplatesample.domain.OrderType;
import org.cham.springkafkatemplatesample.producer.KafkaProducer;
import org.cham.springkafkatemplatesample.repository.OrderRepository;
import org.cham.springkafkatemplatesample.util.OrderFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@Slf4j
class OrderControllerIntegrationTest {
    @LocalServerPort
    @Getter
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private OrderRepository orderRepository;

    private OrderFactory orderFactory = new OrderFactory();

    @Value("${test.topic}")
    private String topic;
    private final Gson gson = new Gson();

    @Autowired
    private KafkaProducer producer;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        List<Order> dummyOrders = orderFactory.createOderList();
        orderRepository.saveAll(dummyOrders);
    }
    @AfterEach
    void cleanUp(){
        orderRepository.deleteAll();
    }
    @Test
    void shouldTestCreateOrder() throws MalformedURLException {
        URL baseUrl = new URL("http://localhost:" + port + "/api/orders");
        ResponseEntity<String> responseEntity = testRestTemplate
                .postForEntity(baseUrl.toString(), orderFactory.createDummyOrder(), String.class);
        log.info("The response is " + responseEntity.getBody());
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertTrue(responseEntity.getBody().contains("1"));
    }
    @Test
    void shouldTestGetAllOrders() throws MalformedURLException {
        URL baseUrl = new URL("http://localhost:" + port + "/api/orders");
        ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl.toString(), String.class);
        log.info("The response is " + response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
        List<Order> orderList = orderRepository.findAll();
        Order testOrder = orderList.stream().findFirst().get();
        assertNotEquals(null, orderList);
        assertTrue(testOrder.getType().equals(OrderType.SELL));
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    public void shouldTestKafkaProducerAndConsumer(CapturedOutput capturedOutput) {
        String testOrderString = gson.toJson(orderFactory.createDummyOrder());
        producer.send(topic, testOrderString);
        assertTrue(capturedOutput.getOut().contains("sending payload"));
//         As the consumer runs on a different thread and making a call to the DB immediately
//         may not return the value hence the tests will fail
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertTrue(capturedOutput.getOut().contains("received message is"));
//         read orders from the repo
        List<Order> orderList = orderRepository.findAll();
        Order testOrder = orderList.stream().findFirst().get();
        assertNotEquals(null, orderList);
        assertTrue(testOrder.getId().equals(1L));
        assertTrue(testOrder.getType().equals(OrderType.SELL));
        assertTrue(testOrder.getCustomerName().equals("John Smith"));
    }
}