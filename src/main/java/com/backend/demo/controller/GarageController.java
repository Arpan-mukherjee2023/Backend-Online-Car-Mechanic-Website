package com.backend.demo.controller;

import com.backend.demo.DTO.GarageDTO;
import com.backend.demo.DTO.GarageRegistrationDTO;
import com.backend.demo.DTO.ServiceDTO;
import com.backend.demo.entity.Garage;
import com.backend.demo.entity.GarageType;
import com.backend.demo.entity.ServiceProvided;
import com.backend.demo.entity.User;
import com.backend.demo.repository.GarageRepository;
import com.backend.demo.service.GarageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/user/garages")
@CrossOrigin(origins = "http://localhost:5173")
public class GarageController {

    @Autowired
    private GarageService garageService;

    @Autowired
    private GarageRepository garageRepository;


    private GarageDTO mapToGarageDTO(Garage garage) {
        GarageDTO dto = new GarageDTO();
        dto.setGarageId(garage.getGarageId());
        dto.setName(garage.getName());
        dto.setProprietorName(garage.getProprietorName());
        dto.setPhoneNumber(garage.getPhoneNumber());
        dto.setEmail(garage.getEmail());
        dto.setLocationAddress(garage.getLocationAddress());
        dto.setLatitude(garage.getLatitude());
        dto.setLongitude(garage.getLongitude());
        dto.setRatings(garage.getRatings());
        dto.setReviewCount(garage.getReviewCount());
        dto.setOpenTime(garage.getOpenTime());
        dto.setCloseTime(garage.getCloseTime());
        dto.setGarageType(garage.getGarageType());
        dto.setTotalRevenue(garage.getTotalRevenue());
        dto.setProfilePhotoUrl(garage.getProfilePhotoUrl());
        dto.setDistance(garage.getDistance());
        dto.setOpen(garage.isOpen());
        return dto;
    }

    @Autowired
    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    // used for search section in user dashboard
    @GetMapping("/search")
    public List<GarageDTO> searchGarages(@RequestParam(value = "q", required = false) String query) {
        List<Garage> garages = garageService.searchByName(query);
        return garages.stream().map(this::mapToGarageDTO).toList();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerGarage(@RequestBody GarageRegistrationDTO request) {
        Garage garage = new Garage();

        garage.setGarageId(request.getGarageId());
        garage.setName(request.getName());
        garage.setEmail(request.getEmail());
        garage.setPhoneNumber(request.getPhone());
        garage.setPassword(request.getPassword());
        garage.setProprietorName(request.getProprietorName());
        garage.setLocationAddress(request.getLocationAddress());

        // Convert String time to java.sql.Time
        garage.setOpenTime(Time.valueOf(request.getOpeningTime() + ":00")); // "09:00" -> "09:00:00"
        garage.setCloseTime(Time.valueOf(request.getClosingTime() + ":00"));

        garage.setGarageType(GarageType.valueOf(request.getGarageType()));

        garage.setLatitude(BigDecimal.valueOf(request.getLatitude()));
        garage.setLongitude(BigDecimal.valueOf(request.getLongitude()));

        // Set default values for ratings and reviewCount
        garage.setRatings(BigDecimal.valueOf(0.00));
        garage.setReviewCount(0);

        // You may want to set other defaults as needed
        garage.setIsOpen(false);
        garage.setTotalRevenue(BigDecimal.ZERO);

        // Save garage to database (assuming you have garageRepository)
        garageRepository.save(garage);

        return ResponseEntity.ok("Garage registered successfully");
    }


    // handles the garage owner login
    @PostMapping("/login")
    public ResponseEntity<?> loginGarage(@RequestBody Garage loginRequest) {
        if(loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            System.out.println("Something went Wrong!");
            return ResponseEntity.status(401).body("Password or Email not Provided");
        } else {
            Garage garage = garageService.findByEmailAndPassword(
                    loginRequest.getEmail().trim(),
                    loginRequest.getPassword().trim()
            );

            if (garage != null) {
                return ResponseEntity.ok(garage); // consider returning a DTO/token instead
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        }
    }



    // finds  the nearby garage to the users location
    @GetMapping("/nearby")
    public ResponseEntity<List<GarageDTO>> getNearbyGarages(@RequestParam double lat, @RequestParam double lon) {
        log.info("Fetching nearby garages for lat: {} and lon: {}", lat, lon);
        List<Garage> nearbyGarages = garageService.getNearbyGarages(lat, lon, 2); // 2 km radius

        List<GarageDTO> garageDTOs = nearbyGarages.stream().map(this::mapToGarageDTO).toList();
        return ResponseEntity.ok(garageDTOs);
    }

    // finds garage based on the given id
    @GetMapping("/{garage_id}")
    public ResponseEntity<Garage> getGarageById(@PathVariable String garage_id) {
        Optional<Garage> garage = garageService.findById(garage_id);
        return garage.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // provides all the services provided by a specific garage
    @GetMapping("/{garageId}/services")
    public ResponseEntity<List<ServiceDTO>> getServicesByGarageId(@PathVariable String garageId) {
        List<ServiceProvided> services = garageService.getServicesByGarageId(garageId);

        // Map services to DTOs
        List<ServiceDTO> serviceDTOs = services.stream()
                .map(service -> new ServiceDTO(service.getServiceId(), service.getServiceName(), service.getServiceDesc()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(serviceDTOs);
    }

    // provides the nearest garages to the users location with user specified service being provided
    @GetMapping("/nearby-service")
    public List<Garage> getNearbyGaragesWithServiceMapped(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam String serviceId
    ) {
        return garageService.getNearbyGarages(lat, lng, serviceId);
    }


}
