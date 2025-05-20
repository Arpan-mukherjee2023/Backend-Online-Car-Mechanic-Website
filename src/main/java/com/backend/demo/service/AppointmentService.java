package com.backend.demo.service;

import com.backend.demo.DTO.AppointmentBookingResponse;
import com.backend.demo.DTO.AppointmentDTO;
import com.backend.demo.DTO.AppointmentDetailsDTO;
import com.backend.demo.DTO.AppointmentRequestDTO;
import com.backend.demo.entity.*;
import com.backend.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MechanicRepository mechanicRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;


    public List<AppointmentDetailsDTO> getAppointmentsByUserId(String userId) {
        return appointmentRepository.findAppointmentsByUserId(userId);
    }

    public List<AppointmentDTO> getUpcomingAppointmentsByGarageId(String garageId) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        List<Appointment> appointments = appointmentRepository
                .findUpcomingAppointmentsByGarageId(garageId, currentDate, currentTime);

        List<AppointmentDTO> dtoList = new ArrayList<>();
        for (Appointment appointment : appointments) {
            AppointmentDTO dto = new AppointmentDTO();
            dto.setAppointmentId(appointment.getAppointmentId());

            // Vehicle
            Vehicle vehicle = appointment.getVehicle();
            dto.setVehicleMake(vehicle.getMake());
            dto.setVehicleModel(vehicle.getModel());
            dto.setVehicleRegistrationNumber(vehicle.getRegistrationNumber());

            // Mechanic
            Mechanic mechanic = appointment.getMechanic();
            dto.setMechanicId(mechanic.getMechanicId());
            dto.setMechanicName(mechanic.getMechanicName());

            // User
            User user = appointment.getUser();
            dto.setUserId(user.getUserId());
            dto.setUserName(user.getName());
            dto.setUserPhoneNumber(user.getPhone());

            // Garage
            Garage garage = appointment.getGarage();
            dto.setGarageId(garage.getGarageId());
            dto.setGarageName(garage.getName());

            // Appointment
            dto.setServiceName(appointment.getServiceType());
            dto.setDate(appointment.getAppointmentDate().toString());
            dto.setTime(appointment.getAppointmentTime().toString());

            dtoList.add(dto);
        }

        return dtoList;
    }

    public AppointmentBookingResponse bookAppointment(AppointmentRequestDTO dto) {
        AppointmentBookingResponse response = new AppointmentBookingResponse();

        // Fetch garage
        Garage garage = garageRepository.findById(dto.getGarageId())
                .orElseThrow(() -> new RuntimeException("Garage not found"));

        // Fetch user
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch vehicle by registration number and user
        Vehicle vehicle = vehicleRepository.findByRegistrationNumberAndUser(dto.getRegistrationNumber(), user)
                .orElseThrow(() -> new RuntimeException("Vehicle not found for user"));

        // Find an available mechanic in the garage
        Optional<Mechanic> availableMechanicOpt = garage.getMechanics().stream()
                .filter(Mechanic::getIsAvailable)
                .findFirst();

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setGarage(garage);
        appointment.setServiceType(dto.getServiceType());
        appointment.setUser(user);
        appointment.setVehicle(vehicle);

        if (availableMechanicOpt.isPresent()) {
            Mechanic mechanic = availableMechanicOpt.get();
            appointment.setMechanic(mechanic);
            appointment.setStatus(ServiceStatus.IN_PROGRESS);

            // Mark mechanic as unavailable
            mechanic.setIsAvailable(false);
            mechanicRepository.save(mechanic);

            response.setMessage("Mechanic assigned successfully.");
            response.setStatus(ServiceStatus.IN_PROGRESS);
        } else {
            // No mechanic available
            appointment.setStatus(ServiceStatus.PENDING);
            appointment.setMechanic(null);

            response.setMessage("No mechanic available currently. Appointment is pending.");
            response.setStatus(ServiceStatus.PENDING);
        }

        // Save appointment
        appointmentRepository.save(appointment);
        response.setAppointmentId(appointment.getAppointmentId());

        return response;
    }

    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // If mechanic assigned, free mechanic
        Mechanic mechanic = appointment.getMechanic();
        if (mechanic != null) {
            mechanic.setIsAvailable(true);
            mechanicRepository.save(mechanic);
        }

        appointmentRepository.delete(appointment);
    }




}
