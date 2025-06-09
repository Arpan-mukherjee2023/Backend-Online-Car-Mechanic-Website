package com.backend.demo.DTO;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private String userId;
    private String garageId;
    private String productId;
    private Long variantId;
    private int quantity;
    private String paymentMode;
}
