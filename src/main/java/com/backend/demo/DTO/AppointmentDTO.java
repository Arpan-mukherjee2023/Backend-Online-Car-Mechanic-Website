package com.backend.demo.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long appointmentId;

    // Vehicle
    private String vehicleMake;
    private String vehicleModel;
    private String vehicleRegistrationNumber;

    // Mechanic
    private String mechanicId;
    private String mechanicName;

    // User
    private String userId;
    private String userName;
    private String userPhoneNumber;

    // Garage
    private String garageId;
    private String garageName;

    // Appointment Details
    private String serviceName;
    private String date;
    private String time;

    // Getters and Setters
}
