package com.backend.demo.controller;


import com.backend.demo.DTO.AppointmentDTO;
import com.backend.demo.DTO.AppointmentDetailsDTO;
import com.backend.demo.DTO.AppointmentRequestDTO;
import com.backend.demo.DTO.AppointmentResponseDTO;
import com.backend.demo.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


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

    @PostMapping("/book")
    public String bookAppointment(@RequestBody AppointmentRequestDTO appointment) {
       return appointmentService.bookAppointment(appointment);
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByGarage(@PathVariable String garageId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByGarageId(garageId);
        return ResponseEntity.ok(appointments);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) {
        try {
            appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity.ok("Appointment with ID " + appointmentId + " cancelled successfully.");
        } catch (NoSuchElementException e) {
            // If the appointment with the given ID is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Appointment with ID " + appointmentId + " not found.");
        } catch (IllegalStateException e) {
            // If the appointment status cannot be cancelled (e.g., already completed/cancelled)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to cancel appointment with ID " + appointmentId + ": " + e.getMessage());
        }
    }

}
