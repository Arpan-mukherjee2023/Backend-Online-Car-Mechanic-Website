package com.backend.demo.controller;
import com.backend.demo.entity.Review;
import com.backend.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/user/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> submitReview(
            @RequestParam("garageId") String garageId,
            @RequestParam("customerId") String customerId,
            @RequestParam("customerName") String customerName,
            @RequestParam("rating") Double rating,
            @RequestParam("comments") String comments,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        // Create Review entity or DTO
        Review review = new Review();
        review.setGarageId(garageId);
        review.setCustomerId(customerId);
        review.setCustomerName(customerName);
        review.setRating(rating);
        review.setComments(comments);

        // You can save image to storage and set image path
        if (image != null && !image.isEmpty()) {
            String imageUrl = image.getOriginalFilename(); // or upload and store URL
            review.setImage(imageUrl);
        }

        reviewService.submitReview(review);
        return ResponseEntity.ok("Review submitted successfully!");
    }

    @GetMapping("/top/{garageId}")
    public ResponseEntity<List<Review>> getTopReviews(@PathVariable String garageId) {
        List<Review> reviews = reviewService.getTopReviewsByGarageId(garageId);
        return ResponseEntity.ok(reviews);
    }
}


