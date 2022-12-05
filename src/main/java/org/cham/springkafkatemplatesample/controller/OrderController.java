package org.cham.springkafkatemplatesample.controller;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cham.springkafkatemplatesample.domain.Order;
import org.cham.springkafkatemplatesample.producer.KafkaProducer;
import org.cham.springkafkatemplatesample.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class OrderController {
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private OrderRepository orderRepository;
    @Value("${test.topic}")
    private String topic;
    private Gson gson = new Gson();
    @PostMapping("/api/orders")
    @ResponseStatus(HttpStatus.OK)
    public String createOrder(@RequestBody Order order) {
        log.info("Inside OrderController.createOrder()", order.toString());
        kafkaProducer.send(topic, gson.toJson(order));
        return order.getId().toString();
    }
    @GetMapping("/api/orders")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAllOrders(){
        log.info("Inside OrderController.getAllOrders()");
        return orderRepository.findAll();
    }
}
