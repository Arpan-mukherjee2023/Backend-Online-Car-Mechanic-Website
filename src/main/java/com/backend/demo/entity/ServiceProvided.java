package com.backend.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "service")
public class ServiceProvided {

    @Id
    private String serviceId;
    private String serviceName;
    private String serviceDesc;

    @ManyToMany(mappedBy = "services")
    @JsonIgnore
    private List<Garage> garages;

    @Override
    public String toString() {
        return "ServiceProvided{" +
                "serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceDesc='" + serviceDesc + '\'' +
                '}';
    }

}
