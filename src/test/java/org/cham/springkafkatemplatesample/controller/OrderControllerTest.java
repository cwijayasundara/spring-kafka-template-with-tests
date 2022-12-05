package org.cham.springkafkatemplatesample.controller;

import com.google.gson.Gson;
import org.cham.springkafkatemplatesample.domain.Order;
import org.cham.springkafkatemplatesample.producer.KafkaProducer;
import org.cham.springkafkatemplatesample.repository.OrderRepository;
import org.cham.springkafkatemplatesample.util.OrderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    private KafkaProducer kafkaProducerMock;
    private OrderRepository orderRepositoryMock;
    private OrderController orderController;
    OrderFactory orderFactory = new OrderFactory();
    private String testTopic= "test-topic";
    private Gson gson = new Gson();

    @BeforeEach
    void setUp() {
        kafkaProducerMock = mock(KafkaProducer.class);
        orderRepositoryMock = mock(OrderRepository.class);
        orderController = new OrderController(kafkaProducerMock, orderRepositoryMock,testTopic,gson);
    }
    @Test
    void createOrder() {
        Order testOrder = orderFactory.createDummyOrder();
        doNothing().when(kafkaProducerMock).send(anyString(), anyString());
        String orderId = orderController.createOrder(testOrder);
        assertEquals("1", orderId);
    }

    @Test
    void getAllOrders() {
        when(orderRepositoryMock.findAll()).thenReturn(orderFactory.createOderList());
        List<Order> orderList = orderController.getAllOrders();
        assertEquals(5, orderList.size());
        Order firstOrder = orderList.stream().findFirst().get();
        assertEquals(31L, firstOrder.getId());
        assertEquals(6, firstOrder.getCustomerId());
        assertEquals("Jeff Bridges", firstOrder.getCustomerName());
        assertEquals(6, firstOrder.getCustomerId());
    }
}