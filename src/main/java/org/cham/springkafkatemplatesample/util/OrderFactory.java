package org.cham.springkafkatemplatesample.util;

import org.cham.springkafkatemplatesample.domain.Order;
import org.cham.springkafkatemplatesample.domain.OrderType;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class OrderFactory {

    private Long orderId = 30L;
    public Order createDummyOrder() {
        Order sellOrder = Order
                .builder()
                .id(1L)
                .type(OrderType.SELL)
                .productId(1)
                .customerId(10)
                .customerName("John Smith")
                .productCount(10)
                .amount(100)
                .creationDate(LocalDateTime.now().toString())
                .build();
        return sellOrder;
    }

    public List<Order> createOderList() {
        LinkedList<Order> sellOrders = new LinkedList<>(List.of(
                new Order(++orderId, 6, "Jeff Bridges", 1, 200, LocalDateTime.now().toString(), OrderType.SELL, 950),
                new Order(++orderId, 7, "Andor Black", 1, 100, LocalDateTime.now().toString(), OrderType.SELL, 1000),
                new Order(++orderId, 8, "Jane Scott", 1, 100, LocalDateTime.now().toString(), OrderType.SELL, 1050),
                new Order(++orderId, 9, "Cam Harley", 1, 300, LocalDateTime.now().toString(), OrderType.SELL, 1000),
                new Order(++orderId, 10, "Kiara Small", 1, 200, LocalDateTime.now().toString(), OrderType.SELL, 1020)
        ));
        return sellOrders;
    }
}
