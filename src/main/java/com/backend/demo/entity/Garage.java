package com.backend.demo.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.*;

@Entity
@Table(name = "garage")
@Data
public class Garage {

    @Id
    private String garageId;

    private String name;

    
    private String password;

    @Column(name = "proprietor_name")
    private String proprietorName;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    @Column(name = "location_address")
    private String locationAddress;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal ratings;

    @Column(name = "review_count")
    private Integer reviewCount;

    @Column(name = "open_time")
    private Time openTime;

    @Column(name = "close_time")
    private Time closeTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "garage_type")
    private GarageType garageType;

    @Column(name = "is_open")
    private Boolean isOpen;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;

    private String profilePhotoUrl;

    @Transient
    private double distance;

    @ManyToMany
    @JoinTable(
            name = "garage_service",
            joinColumns = @JoinColumn(name = "garage_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @JsonIgnore
    private List<ServiceProvided> services;

    @ManyToMany(mappedBy = "favouriteGarages")
    @JsonIgnore
    private Set<User> likedByUsers = new HashSet<>();

    // each garage can have multiple mechanics working with them
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Mechanic> mechanics = new ArrayList<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();


    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Garage)) return false;
        Garage garage = (Garage) o;
        return garageId.equals(garage.garageId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(garageId);
    }

    @Override
    public String toString() {
        return "Garage{" +
                "garageId='" + garageId + '\'' +
                ", name='" + name + '\'' +
                ", proprietorName='" + proprietorName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", locationAddress='" + locationAddress + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", ratings=" + ratings +
                ", reviewCount=" + reviewCount +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", garageType=" + garageType +
                ", isOpen=" + isOpen +
                ", totalRevenue=" + totalRevenue +
                '}';
    }
}

