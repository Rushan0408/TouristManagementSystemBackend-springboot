package com.tourist_management_springboot.demo.repository;

import com.tourist_management_springboot.demo.model.Review;
import com.tourist_management_springboot.demo.model.Tour;
import com.tourist_management_springboot.demo.model.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTourist(Tourist tourist);
    List<Review> findByTour(Tour tour);
    List<Review> findByRating(Integer rating);
    List<Review> findByTourId(Long tourId);
    List<Review> findByTouristId(Long touristId);
    List<Review> findByTourGuideId(Long guideId);
}
