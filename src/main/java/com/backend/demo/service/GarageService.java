package com.backend.demo.service;
import com.backend.demo.DTO.GarageDTOTwo;
import com.backend.demo.entity.Garage;
import com.backend.demo.entity.ServiceProvided;
import com.backend.demo.entity.User;
import com.backend.demo.repository.GarageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GarageService {

    private final GarageRepository garageRepository;

    @Autowired
    public GarageService(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }


    public List<Garage> searchByName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of(); // Return empty list
        }
        return garageRepository.findByNameContainingIgnoreCase(query.trim());
    }

    public Garage findByEmailAndPassword(String email, String password) {
        return garageRepository.findByEmailAndPassword(email, password);
    }

    public List<Garage> findAll() {
        return garageRepository.findAll();
    }

    public List<Garage> getNearbyGarages(double lat, double lon, double radius) {
        return garageRepository.findNearbyGarages(lat, lon, radius);
    }

    public Optional<Garage> findById(String id) {
        return garageRepository.findByGarageId(id);
    }

    public List<ServiceProvided> getServicesByGarageId(String garageId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new RuntimeException("Garage not found with ID: " + garageId));

        return garage.getServices();
    }

    public List<GarageDTOTwo> getFavoriteGarages(String userId) {
        List<Garage> garages = garageRepository.findFavoriteGaragesByUser_Id(userId);
        return garages.stream()
                .map(garage -> new GarageDTOTwo(
                        garage.getGarageId(),
                        garage.getName(),
                        garage.getProprietorName(),
                        garage.getEmail(),
                        garage.getPhoneNumber()))
                .collect(Collectors.toList());
    }

    public List<Garage> getNearbyGarages(double lat, double lng, String serviceId) {
        return garageRepository.findGaragesByServiceIdWithin2KmRadius(serviceId, lat, lng);
    }

}
