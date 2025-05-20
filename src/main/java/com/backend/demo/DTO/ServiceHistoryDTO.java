package com.backend.demo.DTO;

import com.backend.demo.entity.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ServiceHistoryDTO {
    private Long serviceId;
    private String serviceName;
    private ServiceStatus status;
    private LocalDateTime serviceStartTime;
    private LocalDateTime serviceEndTime;
    private Double cost;
    private String paymentMethod;
    private Double userRating;
    private String userReview;
    private String userId;
    private String vehicleId;
    private String registrationNumber;

    // Garage info
    private String garageId;
    private String garageName;

    // Mechanic info
    private String mechanicId;
    private String mechanicName;

    // constructor

    public ServiceHistoryDTO(Long serviceId,
                             String serviceName,
                             ServiceStatus status,
                             LocalDateTime serviceStartTime,
                             LocalDateTime serviceEndTime,
                             Double cost,
                             String paymentMethod,
                             Double userRating,
                             String userReview,
                             String userId,
                             String vehicleId,
                             String registrationNumber,
                             String garageId,
                             String garageName,
                             String mechanicId,
                             String mechanicName) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.status = status;
        this.serviceStartTime = serviceStartTime;
        this.serviceEndTime = serviceEndTime;
        this.cost = cost;
        this.paymentMethod = paymentMethod;
        this.userRating = userRating;
        this.userReview = userReview;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.garageId = garageId;
        this.garageName = garageName;
        this.mechanicId = mechanicId;
        this.mechanicName = mechanicName;
    }


}

