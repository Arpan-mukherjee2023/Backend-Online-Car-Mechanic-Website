package com.backend.demo.DTO;

import lombok.Data;

@Data
public class GarageRegistrationDTO {
    private String garageId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String proprietorName;
    private String locationAddress;
    private String openingTime; // String or java.sql.Time depending on conversion
    private String closingTime;
    private String garageType;  // Will map to enum GarageType
    private Double latitude;
    private Double longitude;

    // getters and setters
}
