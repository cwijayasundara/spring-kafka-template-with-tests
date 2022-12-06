package org.cham.springkafkatemplatesample.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private Long id;
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;
    @Column(name = "customer_name", nullable = false)
    private String customerName;
    @Column(name = "product_id", nullable = false)
    private Integer productId;
    @Column(name = "product_count", nullable = false)
    private int productCount;
    @Column(name = "creation_date", nullable = false)
    private String creationDate;
    @Column(name = "type", nullable = false)
    private OrderType type;
    @Column(name = "amount", nullable = false)
    private int amount;
}