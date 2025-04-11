package com.tourist_management_springboot.demo.controller;

import com.tourist_management_springboot.demo.model.Booking;
import com.tourist_management_springboot.demo.model.Review;
import com.tourist_management_springboot.demo.model.Tour;
import com.tourist_management_springboot.demo.model.Tourist;
import com.tourist_management_springboot.demo.model.User;
import com.tourist_management_springboot.demo.repository.*;
import com.tourist_management_springboot.demo.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TouristRepository touristRepository;

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Optional<Review> reviewData = reviewRepository.findById(id);

        return reviewData.map(review -> new ResponseEntity<>(review, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/tour/{tourId}")
    public ResponseEntity<List<Review>> getReviewsByTour(@PathVariable Long tourId) {
        Optional<Tour> tourData = tourRepository.findById(tourId);

        if (tourData.isPresent()) {
            List<Review> reviews = reviewRepository.findByTour(tourData.get());
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/my-reviews")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Review>> getMyReviews() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<com.tourist_management_springboot.demo.model.User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = (User) userOptional.get();
        Optional<Tourist> touristOptional = touristRepository.findByUser((com.tourist_management_springboot.demo.model.User) user);

        if (touristOptional.isPresent()) {
            List<Review> reviews = reviewRepository.findByTourist(touristOptional.get());
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        try {
            // Get current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Optional<com.tourist_management_springboot.demo.model.User> userOptional = userRepository.findById(userDetails.getId());
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            User user = (User) userOptional.get();
            Optional<Tourist> touristOptional = touristRepository.findByUser(user);

            if (touristOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Tourist tourist = touristOptional.get();

            // Validate tour
            if (review.getTour() == null || review.getTour().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Optional<Tour> tourOptional = tourRepository.findById(review.getTour().getId());
            if (tourOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Tour tour = tourOptional.get();

            // Check if the tourist has booked this tour and completed it
            List<Booking> bookings = bookingRepository.findByTourist(tourist);
            boolean hasCompletedTour = bookings.stream()
                    .anyMatch(b -> b.getTour().getId().equals(tour.getId()) &&
                            b.getStatus() == Booking.BookingStatus.COMPLETED);

            if (!hasCompletedTour) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Set review details
            review.setTourist(tourist);
            review.setTour(tour);
            review.setReviewDate(LocalDateTime.now());

            // Validate rating
            if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Review newReview = reviewRepository.save(review);
            return new ResponseEntity<Review>(newReview, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review) {
        Optional<Review> reviewData = reviewRepository.findById(id);

        if (reviewData.isPresent()) {
            Review existingReview = reviewData.get();

            // Security check
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (existingReview.getTourist().getUser().getId().equals(userDetails.getId())) {
                // Update allowed fields
                if (review.getRating() != null) {
                    if (review.getRating() >= 1 && review.getRating() <= 5) {
                        existingReview.setRating(review.getRating());
                    }
                }

                if (review.getComment() != null) {
                    existingReview.setComment(review.getComment());
                }

                existingReview.setReviewDate(LocalDateTime.now());

                return new ResponseEntity<>(reviewRepository.save(existingReview), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable Long id) {
        try {
            Optional<Review> reviewData = reviewRepository.findById(id);

            if (reviewData.isPresent()) {
                Review review = reviewData.get();

                // Security check
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                boolean isAdmin = userDetails.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                boolean isOwner = review.getTourist().getUser().getId().equals(userDetails.getId());

                if (isAdmin || isOwner) {
                    reviewRepository.deleteById(id);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
