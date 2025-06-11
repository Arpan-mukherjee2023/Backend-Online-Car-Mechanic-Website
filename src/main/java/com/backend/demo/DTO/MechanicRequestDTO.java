package com.backend.demo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MechanicRequestDTO {
    @NotBlank
    private String mechanicName;

    @Email
    private String mechanicEmail;

    @Pattern(regexp = "\\d{10}")
    private String mechanicPhoneNumber;

    @Pattern(regexp = "\\d{12}")
    private String mechanicAadharNumber;

    @NotBlank
    private String mechanicAddress;

    @Min(18)
    private Integer mechanicAge;

    @Min(0)
    private Integer yearsOfExperience;

    @NotBlank
    private String specialization;

    private Boolean isAvailable;

    @NotBlank
    private String garageId;

    // getters and setters
}
