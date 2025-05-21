package com.backend.demo.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequestDTO {
    private String appointmentDate;
    private String appointmentTime;
    private String garageId;
    private String registrationNumber;
    private String serviceType; // service ID
    private String userEmail;
    private String userId;
    private String userName;
    private boolean confirmWait = false; // default false
}



