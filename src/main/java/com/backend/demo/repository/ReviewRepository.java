package com.backend.demo.repository;

import com.backend.demo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // You can add custom query methods here if needed
    List<Review> findTop2ByGarageIdOrderByRatingDesc(String garageId);
}

