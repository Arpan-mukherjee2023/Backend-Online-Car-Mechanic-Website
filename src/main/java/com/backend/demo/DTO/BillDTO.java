package com.backend.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillDTO {
    private double basePrice;
    private double gst;
    private double deliveryCharge;
    private double totalAmount;
}
