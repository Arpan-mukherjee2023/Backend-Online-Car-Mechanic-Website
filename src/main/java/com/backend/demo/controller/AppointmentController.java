package com.backend.demo.controller;

import com.backend.demo.DTO.AppointmentBookingResponse;
import com.backend.demo.DTO.AppointmentDTO;
import com.backend.demo.DTO.AppointmentDetailsDTO;
import com.backend.demo.DTO.AppointmentRequestDTO;
import com.backend.demo.entity.Appointment;
import com.backend.demo.entity.ServiceStatus;
import com.backend.demo.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // get upcoming appointments for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentDetailsDTO>> getAppointmentsForUser(@PathVariable String userId) {
        List<AppointmentDetailsDTO> appointments = appointmentService.getAppointmentsByUserId(userId);
        return ResponseEntity.ok(appointments);
    }

    // get the upcoming appointments of a specific garage
    @GetMapping("/upcoming/{garageId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointments(@PathVariable String garageId) {
        List<AppointmentDTO> appointments = appointmentService.getUpcomingAppointmentsByGarageId(garageId);
        return ResponseEntity.ok(appointments);
    }





}
