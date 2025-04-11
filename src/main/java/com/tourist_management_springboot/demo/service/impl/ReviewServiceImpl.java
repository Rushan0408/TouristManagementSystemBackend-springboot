package com.tourist_management_springboot.demo.service.impl;

import com.tourist_management_springboot.demo.model.Review;
import com.tourist_management_springboot.demo.repository.ReviewRepository;
import com.tourist_management_springboot.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Long id, Review reviewDetails) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());
        review.setReviewDate(reviewDetails.getReviewDate());

        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        reviewRepository.delete(review);
    }

    @Override
    public List<Review> getReviewsByTour(Long tourId) {
        return reviewRepository.findByTourId(tourId);
    }

    @Override
    public List<Review> getReviewsByTourist(Long touristId) {
        return reviewRepository.findByTouristId(touristId);
    }

    @Override
    public List<Review> getReviewsByGuide(Long guideId) {
        return reviewRepository.findByTourGuideId(guideId);
    }

    @Override
    public double getAverageRatingForTour(Long tourId) {
        List<Review> reviews = reviewRepository.findByTourId(tourId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

    @Override
    public double getAverageRatingForGuide(Long guideId) {
        List<Review> reviews = reviewRepository.findByTourGuideId(guideId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
