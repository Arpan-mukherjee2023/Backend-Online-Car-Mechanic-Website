package com.backend.demo.repository;

import com.backend.demo.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, String> {
    @Query("SELECT s.serviceName FROM ServiceEntity s WHERE s.serviceId = :serviceId")
    String findServiceNameByServiceId(String serviceId);
}
