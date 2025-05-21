package com.backend.demo.repository;

import com.backend.demo.entity.MechanicAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MechanicAppointmentRepository extends JpaRepository<MechanicAppointment, Long> {

    @Query("SELECT m.mechanicId FROM MechanicAppointment m WHERE m.garageId = :garageId AND m.appointmentDate = :date AND m.appointmentTime = :time")
    List<String> findMechanicIdsBookedAtTime(@Param("garageId") String garageId,
                                             @Param("date") LocalDate date,
                                             @Param("time") LocalTime time);
}

