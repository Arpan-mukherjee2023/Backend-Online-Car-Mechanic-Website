package com.backend.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "service_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long serviceId;

    private String serviceName;
    private String serviceDescription;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status; // COMPLETED, IN_PROGRESS, CANCELLED, etc.

    private LocalDateTime serviceStartTime;
    private LocalDateTime serviceEndTime;

    private Double cost;
    private String paymentMethod; // CASH, CARD, UPI, etc.

    private Double userRating;    // optional user rating
    private String userReview;    // optional user review

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id")
    @JsonIgnore
    private Garage garage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    @JsonIgnore
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechanic_id")
    @JsonIgnore
    private Mechanic mechanic;

}

