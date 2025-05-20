package com.backend.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mechanic {

    @Id
    private String mechanicId;

    @NotBlank(message = "Mechanic name is required")
    private String mechanicName;

    @Email(message = "Invalid email format")
    private String mechanicEmail;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String mechanicPhoneNumber;

    @Pattern(regexp = "\\d{12}", message = "Aadhar number must be exactly 12 digits")
    private String mechanicAadharNumber;

    @NotBlank(message = "Address is required")
    private String mechanicAddress;

    @Min(value = 18, message = "Mechanic must be at least 18 years old")
    private Integer mechanicAge;

    @Min(value = 0)
    private Integer yearsOfExperience;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "5.0", inclusive = true)
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "garage_id", referencedColumnName = "garageId")
    @JsonIgnore
    private Garage garage;

    @OneToMany(mappedBy = "mechanic", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();

}
