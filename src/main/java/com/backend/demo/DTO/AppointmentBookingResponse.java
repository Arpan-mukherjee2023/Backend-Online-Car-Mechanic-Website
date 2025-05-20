package com.backend.demo.DTO;

import com.backend.demo.entity.ServiceStatus;
import lombok.Data;

@Data
public class AppointmentBookingResponse {
    private Long appointmentId;
    private String message;
    private ServiceStatus status;

    // getters and setters
}
