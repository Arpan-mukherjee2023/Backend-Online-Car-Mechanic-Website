package com.backend.demo.repository;

import com.backend.demo.entity.User;
import com.backend.demo.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    List<Vehicle> findByUser_UserId(String userId);

    Optional<Vehicle> findByRegistrationNumberAndUser(String registrationNumber, User user);
}
