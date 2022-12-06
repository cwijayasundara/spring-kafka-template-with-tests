package org.cham.springkafkatemplatesample.consumer;

import com.google.gson.Gson;
import org.cham.springkafkatemplatesample.domain.Order;
import org.cham.springkafkatemplatesample.repository.OrderRepository;
import org.cham.springkafkatemplatesample.util.OrderFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class KafkaConsumerTest {
    private KafkaConsumer kafkaConsumer;
    private Gson gson = new Gson();
    private OrderFactory orderFactory = new OrderFactory();

    private OrderRepository orderRepositoryMock;
    @BeforeEach
    void setUp() {
        orderRepositoryMock = mock(OrderRepository.class);
        kafkaConsumer = new KafkaConsumer(orderRepositoryMock);
    }
    @AfterEach
    void tearDown() {
    }
    @Test
    void shouldTestOrderMessageConsumption() {
        String testOrderStr = gson.toJson(orderFactory.createDummyOrder());
        when(orderRepositoryMock.save(any())).thenReturn(orderFactory.createDummyOrder());
        Order testOrder = kafkaConsumer.receive(testOrderStr);
        assertEquals(1L, testOrder.getId());
        assertEquals("SELL", testOrder.getType().toString());
        assertEquals(10, testOrder.getProductCount());
    }

    @Test
    void shouldTestDbNotAvailable() {
        String testOrderStr = gson.toJson(orderFactory.createDummyOrder());
        doThrow(new DataAccessException("Service failure") {
        }).when(orderRepositoryMock).save(any());
        assertThrows(RuntimeException.class, () -> {
            kafkaConsumer.receive(testOrderStr);
        });
    }
}