package org.cham.springkafkatemplatesample.consumer;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cham.springkafkatemplatesample.domain.Order;
import org.cham.springkafkatemplatesample.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class KafkaConsumer {
    private final Gson gson = new Gson();
    @Autowired
    private OrderRepository orderRepository;
    @KafkaListener(topics = "${test.topic}")
    public Order receive(String message) {
        log.info("received payload='{}'", message);
        Order receivedOrder = gson.fromJson(message, Order.class);
        log.info("received message is " + receivedOrder.toString());
        return  orderRepository.save(receivedOrder);
    }
}