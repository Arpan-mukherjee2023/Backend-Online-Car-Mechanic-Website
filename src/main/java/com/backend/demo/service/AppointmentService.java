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

    @Autowired
    private ServiceRepository serviceRepository;


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
