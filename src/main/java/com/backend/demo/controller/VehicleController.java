package com.backend.demo.controller;


import com.backend.demo.DTO.VehicleDTO;
import com.backend.demo.entity.User;
import com.backend.demo.entity.Vehicle;
import com.backend.demo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backend.demo.repository.UserRepository;
import com.backend.demo.repository.VehicleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/add")
    public ResponseEntity<?> addVehicle(@RequestBody VehicleDTO dto) {
        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleId(UUID.randomUUID().toString());
            vehicle.setMake(dto.make);
            vehicle.setModel(dto.model);
            vehicle.setRegistrationNumber(dto.registrationNumber);
            vehicle.setVin(dto.vin);
            vehicle.setYear(dto.year);
            vehicle.setPurchaseDate(LocalDate.parse(dto.purchaseDate));

            User user = userRepository.findById(dto.userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.userId));
            vehicle.setUser(user);

            Vehicle savedVehicle = vehicleRepository.save(vehicle);
            return ResponseEntity.ok(savedVehicle);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/edit/{userId}")
    public ResponseEntity<List<Vehicle>> getVehiclesByUser(@PathVariable String userId) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByUserId(userId);
        return ResponseEntity.ok(vehicles);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String id) {
        boolean deleted = vehicleService.deleteVehicleById(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content on success
        } else {
            return ResponseEntity.notFound().build(); // 404 if vehicle not found
        }
    }

    @GetMapping("/user/{userId}")
    public List<Vehicle> getVehiclesByUserId(@PathVariable String userId) {
        return vehicleService.getVehiclesByUserId(userId);
    }
}
