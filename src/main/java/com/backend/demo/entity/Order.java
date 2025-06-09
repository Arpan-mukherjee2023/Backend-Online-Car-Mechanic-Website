package com.backend.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String userId;
    private String garageId;
    private String productId;
    private Long variantId;
    private int quantity;
    private double totalAmount;
    private String paymentMode; // COD or ONLINE
    private String paymentStatus; // PENDING, COMPLETED, FAILED
    private LocalDate orderDate;
}
