package org.cham.springkafkatemplatesample.producer;

import com.google.gson.Gson;
import org.cham.springkafkatemplatesample.util.OrderFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class KafkaProducerTest {
    private KafkaProducer kafkaProducer;
    private KafkaTemplate<String, String> mockKafkaTemplate;
    private Gson gson = new Gson();
    private final String testTopic = "test-topic";
    private final OrderFactory orderFactory = new OrderFactory();
    @BeforeEach
    void setUp() {
        mockKafkaTemplate = mock(KafkaTemplate.class);
        kafkaProducer = new KafkaProducer(mockKafkaTemplate);
    }
    @AfterEach
    void tearDown() {
    }
    @Test
    void shoudTestMessageSendingUsingKafkaTemplate() {
        String testOrderStr = gson.toJson(orderFactory.createDummyOrder());
        kafkaProducer.send(testTopic,testOrderStr);
        verify(mockKafkaTemplate, times(1)).send(eq(testTopic), argThat(s -> s.equals(testOrderStr)));
    }
    @Test
    void shouldTestExceptionWhileUsingKafkaTemplate(){
        String testOrderStr = gson.toJson(orderFactory.createDummyOrder());
        doThrow(new KafkaException("Service failure")).when(mockKafkaTemplate).send(anyString(), anyString());
        assertThrows(RuntimeException.class, () -> {
            kafkaProducer.send(testTopic,testOrderStr);
        });
    }
}