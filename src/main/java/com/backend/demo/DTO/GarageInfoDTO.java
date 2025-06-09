package com.backend.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GarageInfoDTO {
    private String garageId;
    private String garageName;
    private int stock;
}