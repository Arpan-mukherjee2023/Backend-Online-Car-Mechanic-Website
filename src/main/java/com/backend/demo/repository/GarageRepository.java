package com.backend.demo.repository;
import com.backend.demo.entity.Garage;
import com.backend.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GarageRepository extends JpaRepository<Garage, String> {
    List<Garage> findByNameContainingIgnoreCase(String name);


    Garage findByEmailAndPassword(String email, String password);
    @Query(value = """
    SELECT * 
    FROM garage
    WHERE 6371 * acos(
        cos(radians(:lat)) *
        cos(radians(latitude)) *
        cos(radians(longitude) - radians(:lon)) +
        sin(radians(:lat)) *
        sin(radians(latitude))
    ) <= :radius
    ORDER BY name
    """, nativeQuery = true)
    List<Garage> findNearbyGarages(@Param("lat") double lat, @Param("lon") double lon, @Param("radius") double radius);

    Optional<Garage> findByGarageId(String id);

    @Query("SELECT g FROM Garage g JOIN g.likedByUsers u WHERE u.userId = :userId")
    List<Garage> findFavoriteGaragesByUser_Id(String userId);


    @Query(value = """
        SELECT g.*, 
               (6371 * acos(
                   cos(radians(:latitude)) 
                   * cos(radians(g.latitude)) 
                   * cos(radians(g.longitude) - radians(:longitude)) 
                   + sin(radians(:latitude)) 
                   * sin(radians(g.latitude))
               )) AS distance_km
        FROM garage g
        JOIN garage_service gs ON g.garage_id = gs.garage_id
        WHERE gs.service_id = :serviceId
        HAVING distance_km <= 2
        ORDER BY distance_km
        """, nativeQuery = true)
    List<Garage> findGaragesByServiceIdWithin2KmRadius(
            @Param("serviceId") String serviceId,
            @Param("latitude") double latitude,
            @Param("longitude") double longitude
    );
}
