package com.backend.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;


@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    private String userId;

    private String name;

    private String email;

    private String phone;

    private String password;

    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Vehicle> vehicles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_favourite_garages",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "garage_id")
    )
    @JsonIgnore
    private Set<Garage> favouriteGarages = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "favourite_mechanics",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "mechanic_id")
    )
    @JsonIgnore
    private Set<Mechanic> favouriteMechanics = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User ) o;
        return userId.equals(user.userId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }



}
