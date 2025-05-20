package com.backend.demo.repository;

import com.backend.demo.DTO.ServiceHistoryDTO;
import com.backend.demo.entity.ServiceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceHistoryRepository extends JpaRepository<ServiceHistory, Long> {

    List<ServiceHistory> findByUserUserId(String userId);

    List<ServiceHistory> findByGarageGarageId(String garageId);

    List<ServiceHistory> findByVehicleVehicleId(String vehicleId);

    @Query("SELECT new com.backend.demo.DTO.ServiceHistoryDTO(" +
            "sh.serviceId, " +
            "sh.serviceName, " +
            "sh.status, " +  // remove .name if status is an Enum
            "sh.serviceStartTime, " +
            "sh.serviceEndTime, " +
            "sh.cost, " +
            "sh.paymentMethod, " +
            "sh.userRating, " +
            "sh.userReview, " +
            "u.userId, " +
            "v.vehicleId, v.registrationNumber," +
            "g.garageId, g.name, " +
            "m.mechanicId, m.mechanicName) " +
            "FROM ServiceHistory sh " +
            "JOIN sh.user u " +
            "JOIN sh.vehicle v " +
            "JOIN sh.garage g " +
            "JOIN sh.mechanic m " +
            "WHERE u.userId = :userId")
    List<ServiceHistoryDTO> fetchServiceHistoriesByUserId(@Param("userId") String userId);


}
