package com.backend.demo.DTO;

import lombok.Data;

@Data
public class BillRequestDTO {
    private String productId;
    private Long variantId;
    private int quantity;
    private String garageId;
}
