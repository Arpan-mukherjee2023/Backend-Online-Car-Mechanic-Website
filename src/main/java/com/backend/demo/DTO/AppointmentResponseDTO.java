package com.backend.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDTO {
    private Long id;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String vehicleMake;
    private String vehicleModel;
    private int vehicleYear;
    private String vehiclePlateNumber;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String serviceType;
    private String status;

    // Constructor using fields (can use @Builder or manually write it)
}

