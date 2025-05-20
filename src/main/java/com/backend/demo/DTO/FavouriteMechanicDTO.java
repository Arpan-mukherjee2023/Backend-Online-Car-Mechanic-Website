package com.backend.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteMechanicDTO {
    private String mechanicId;
    private String mechanicName;
    private String garageName;
    private String phoneNumber;
    private String specialization;
}
