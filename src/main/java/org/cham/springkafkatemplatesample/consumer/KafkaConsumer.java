package org.cham.springkafkatemplatesample.consumer;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cham.springkafkatemplatesample.domain.Order;
import org.cham.springkafkatemplatesample.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    private static final String DATABASE_ACCESS_ERROR = "Error while saving order to the Postgres DB";
    @KafkaListener(topics = "${test.topic}")
    public Order receive(String message) {
        Order receivedOrder = gson.fromJson(message, Order.class);
        log.info("received message is " + receivedOrder.toString());
        Order savedOrder;
        try{
            savedOrder = orderRepository.save(receivedOrder);
        } catch(DataAccessException dataAccessException){
            log.error(DATABASE_ACCESS_ERROR, dataAccessException.getStackTrace());
            throw new RuntimeException(DATABASE_ACCESS_ERROR, dataAccessException);
        }
        return savedOrder;
    }
}