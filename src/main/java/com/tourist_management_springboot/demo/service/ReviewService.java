package com.tourist_management_springboot.demo.service;

import com.tourist_management_springboot.demo.model.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> getAllReviews();
    Optional<Review> getReviewById(Long id);
    Review createReview(Review review);
    Review updateReview(Long id, Review reviewDetails);
    void deleteReview(Long id);
    List<Review> getReviewsByTour(Long tourId);
    List<Review> getReviewsByTourist(Long touristId);
    List<Review> getReviewsByGuide(Long guideId);
    double getAverageRatingForTour(Long tourId);
    double getAverageRatingForGuide(Long guideId);
}
