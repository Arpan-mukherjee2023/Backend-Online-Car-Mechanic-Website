package com.backend.demo.DTO;

import com.backend.demo.entity.GarageType;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;
@Data
public class GarageDTO {
    private String garageId;
    private String name;
    private String phoneNumber;
    private String email;
    private String locationAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal ratings;
    private int reviewCount;
    private Time openTime;
    private Time closeTime;
    private GarageType garageType;
    private boolean isOpen;
    private BigDecimal totalRevenue;
    private String profilePhotoUrl;
    private double distance;
    private String proprietorName;

}
