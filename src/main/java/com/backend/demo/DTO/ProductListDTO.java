package com.backend.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDTO {
    private String garageId;
    private String productId;
    private String productName;
    private String productDesc;
    private double productPrice;
    private String productUnit;
    private String variantType;
    private String variantValue;
    private int stock;
}

