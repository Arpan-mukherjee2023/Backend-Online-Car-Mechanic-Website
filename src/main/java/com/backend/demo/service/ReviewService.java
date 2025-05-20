package com.backend.demo.service;

import com.backend.demo.entity.Review;
import com.backend.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Method to save a review
    public Review submitReview(Review review) {
        return reviewRepository.save(review);
    }

    // Other business logic can be added here, e.g., fetching reviews, etc.
    public List<Review> getTopReviewsByGarageId(String garageId) {
        return reviewRepository.findTop2ByGarageIdOrderByRatingDesc(garageId);
    }
}

