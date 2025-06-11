package com.backend.demo.DTO;

import com.backend.demo.entity.ProductVariant;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderNotification {

    private String userName;
    private String userPhoneNumber;
    private String userEmailId;
    private String paymentMode;
    private double totalAmount;
    private String productName;
    private String productVariantName;
    private String paymentStatus;
    private int quantity;
    private LocalDate orderDate;
}
