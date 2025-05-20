package com.backend.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MechanicResponseDTO {
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String specialization;
    @JsonProperty("isAvailable")
    private boolean isAvailable;
}
