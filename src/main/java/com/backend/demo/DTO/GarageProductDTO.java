package com.backend.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GarageProductDTO {
    private String garageId;
    private String productId;
    private Long variantId;       // <-- Added this to map ProductVariant by id
    private int stock;
    private String productUnit;
    private String productName;   // You can keep this if you want to send/display product name from frontend
}
