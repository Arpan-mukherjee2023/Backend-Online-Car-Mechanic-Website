package com.backend.demo.repository;

import com.backend.demo.entity.Garage;
import com.backend.demo.entity.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, String> {
    List<Mechanic> findByGarageGarageId(String garageId);
    Optional<Mechanic> findFirstByGarageAndIsAvailableTrue(Garage garage);
}
