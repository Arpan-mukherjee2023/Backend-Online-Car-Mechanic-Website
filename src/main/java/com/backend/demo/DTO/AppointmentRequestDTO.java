package com.backend.demo.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequestDTO {
    private String registrationNumber;
    private String serviceId;
    private String garageId;
    private String appointmentDate;
    private String appointmentTime;
    private String canWait;
    private String userId;
    private String userName;
    private String userEmail;

    // Getters and Setters
}
