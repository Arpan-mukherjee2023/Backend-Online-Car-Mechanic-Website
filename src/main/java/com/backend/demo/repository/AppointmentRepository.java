package com.backend.demo.repository;

import com.backend.demo.DTO.AppointmentDetailsDTO;
import com.backend.demo.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT new com.backend.demo.DTO.AppointmentDetailsDTO(" +
            "a.appointmentDate, a.appointmentTime, a.serviceType, a.status," +
            "g.garageId, g.name, " +
            "m.mechanicId, m.mechanicName, " +
            "v.vehicleId, v.registrationNumber) " +
            "FROM Appointment a " +
            "JOIN a.garage g " +
            "JOIN a.mechanic m " +
            "JOIN a.vehicle v " +
            "WHERE a.user.userId = :userId")
    List<AppointmentDetailsDTO> findAppointmentsByUserId(@Param("userId") String userId);


    @Query("SELECT a FROM Appointment a WHERE a.garage.garageId = :garageId AND " +
            "(a.appointmentDate > :currentDate OR " +
            "(a.appointmentDate = :currentDate AND a.appointmentTime > :currentTime)) " +
            "ORDER BY a.appointmentDate ASC, a.appointmentTime ASC")
    List<Appointment> findUpcomingAppointmentsByGarageId(String garageId, LocalDate currentDate, LocalTime currentTime);
}



