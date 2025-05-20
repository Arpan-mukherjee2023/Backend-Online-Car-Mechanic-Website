package com.backend.demo.service;

import com.backend.demo.entity.User;
import com.backend.demo.entity.Vehicle;
import com.backend.demo.repository.UserRepository;
import com.backend.demo.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    public Vehicle addVehicle(Vehicle vehicle) {
        String userId = vehicle.getUser().getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        vehicle.setUser(user);

        if (vehicle.getVehicleId() == null || vehicle.getVehicleId().isBlank()) {
            vehicle.setVehicleId(UUID.randomUUID().toString());
        }

        return vehicleRepository.save(vehicle);
    }


    public List<Vehicle> getVehiclesByUserId(String userId) {
        return vehicleRepository.findByUser_UserId(userId);
    }

    public boolean deleteVehicleById(String id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
