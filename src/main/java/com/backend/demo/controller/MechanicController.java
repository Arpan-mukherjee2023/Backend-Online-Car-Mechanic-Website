package com.backend.demo.controller;

import com.backend.demo.DTO.MechanicResponseDTO;
import com.backend.demo.entity.Mechanic;
import com.backend.demo.service.MechanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mechanics")
@CrossOrigin(origins = "http://localhost:5173")
public class MechanicController {

    @Autowired
    private MechanicService mechanicService;

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
