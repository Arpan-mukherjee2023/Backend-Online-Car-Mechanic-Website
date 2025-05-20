package com.backend.demo.service;

import com.backend.demo.DTO.MechanicResponseDTO;
import com.backend.demo.entity.Mechanic;
import com.backend.demo.repository.MechanicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MechanicService {

    @Autowired
    private MechanicRepository mechanicRepository;


    public List<MechanicResponseDTO> getMechanicsByGarageId(String garageId) {
        List<Mechanic> mechanics = mechanicRepository.findByGarageGarageId(garageId);
        return mechanics.stream().map(mechanic -> {
            MechanicResponseDTO dto = new MechanicResponseDTO();
            dto.setId(mechanic.getMechanicId());
            dto.setName(mechanic.getMechanicName());
            dto.setPhoneNumber(mechanic.getMechanicPhoneNumber());
            dto.setEmail(mechanic.getMechanicEmail());
            dto.setSpecialization(mechanic.getSpecialization());
            dto.setAvailable(mechanic.getIsAvailable());
            return dto;
        }).toList();
    }

    public Mechanic updateAvailability(String mechanicId, boolean isAvailable) {
        Optional<Mechanic> optionalMechanic = mechanicRepository.findById(mechanicId);
        if (optionalMechanic.isEmpty()) {
            throw new RuntimeException("Mechanic not found with id: " + mechanicId);
        }

        Mechanic mechanic = optionalMechanic.get();
        mechanic.setIsAvailable(isAvailable);
        return mechanicRepository.save(mechanic);
    }
}
