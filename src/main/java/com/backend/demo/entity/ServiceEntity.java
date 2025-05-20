package com.backend.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity {
    @Id
    private String serviceId;

    private String serviceName;

    private String serviceDesc;

    @ManyToMany(mappedBy = "services")
    @JsonIgnore
    private List<Garage> garages;
}
