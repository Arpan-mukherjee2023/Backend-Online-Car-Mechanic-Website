package com.backend.demo.DTO;

import com.backend.demo.entity.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDetailsDTO {
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String serviceType;
    private ServiceStatus status;

    private String garageId;
    private String garageName;

    private String mechanicId;
    private String mechanicName;

    private String vehicleId;
    private String registrationNumber;
}
