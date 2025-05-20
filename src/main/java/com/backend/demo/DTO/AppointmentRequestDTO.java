package com.backend.demo.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequestDTO {
    private String registrationNumber;
    private String serviceType;
    private String garageId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String userId;
}

