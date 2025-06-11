package com.backend.demo.controller;

import com.backend.demo.DTO.MechanicRequestDTO;
import com.backend.demo.DTO.MechanicResponseDTO;
import com.backend.demo.entity.Garage;
import com.backend.demo.entity.Mechanic;
import com.backend.demo.repository.GarageRepository;
import com.backend.demo.repository.MechanicRepository;
import com.backend.demo.service.MechanicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/mechanics")
@CrossOrigin(origins = "http://localhost:5173")
public class MechanicController {

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private MechanicRepository mechanicRepository;

    private static final Random random = new Random();

    public static String generateMechanicId() {
        int randomNum = random.nextInt(1000); // 0 to 999
        return String.format("mech.%03d", randomNum);
    }

    @PostMapping("/add-mechanic")
    public ResponseEntity<String> addMechanic(@RequestBody MechanicRequestDTO dto) {
        Garage garage = garageRepository.findById(dto.getGarageId())
                .orElseThrow(() -> new RuntimeException("Garage not found"));

        Mechanic mechanic = Mechanic.builder()
                .mechanicId(generateMechanicId()) // generate ID here
                .mechanicName(dto.getMechanicName())
                .mechanicEmail(dto.getMechanicEmail())
                .mechanicPhoneNumber(dto.getMechanicPhoneNumber())
                .mechanicAadharNumber(dto.getMechanicAadharNumber())
                .mechanicAddress(dto.getMechanicAddress())
                .mechanicAge(dto.getMechanicAge())
                .yearsOfExperience(dto.getYearsOfExperience())
                .specialization(dto.getSpecialization())
                .isAvailable(dto.getIsAvailable())
                .garage(garage)
                .rating(0.0) // or default rating
                .build();

        mechanicRepository.save(mechanic);

        return ResponseEntity.ok("Mechanic added successfully");
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<MechanicResponseDTO>> getMechanicsByGarage(@PathVariable String garageId) {
        List<MechanicResponseDTO> mechanics = mechanicService.getMechanicsByGarageId(garageId);
        return ResponseEntity.ok(mechanics);
    }

    @PutMapping("/{mechanicId}/availability/{isAvailable}")
    public ResponseEntity<Mechanic> updateAvailability(
            @PathVariable String mechanicId,
            @PathVariable boolean isAvailable) {

        Mechanic updatedMechanic = mechanicService.updateAvailability(mechanicId, isAvailable);
        return ResponseEntity.ok(updatedMechanic);
    }
}
