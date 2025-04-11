package com.tourist_management_springboot.demo.mapper;

import com.tourist_management_springboot.demo.dto.*;
import com.tourist_management_springboot.demo.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public TourDTO toTourDTO(Tour tour) {
        TourDTO dto = new TourDTO();
        dto.setId(tour.getId());
        dto.setName(tour.getName());
        dto.setDescription(tour.getDescription());
        dto.setPrice(tour.getPrice());
        dto.setDuration(tour.getDuration());
        dto.setStartDate(tour.getStartDate());
        dto.setEndDate(tour.getEndDate());
        dto.setMaxCapacity(tour.getMaxCapacity());
        
        if (tour.getDestination() != null) {
            dto.setDestinationId(tour.getDestination().getId());
            dto.setDestinationName(tour.getDestination().getName());
        }
        
        if (tour.getGuide() != null) {
            dto.setGuideId(tour.getGuide().getId());
            dto.setGuideName(tour.getGuide().getName());
        }
        
        return dto;
    }

    public Tour toTourEntity(TourDTO dto) {
        Tour tour = new Tour();
        tour.setId(dto.getId());
        tour.setName(dto.getName());
        tour.setDescription(dto.getDescription());
        tour.setPrice(dto.getPrice());
        tour.setDuration(dto.getDuration());
        tour.setStartDate(dto.getStartDate());
        tour.setEndDate(dto.getEndDate());
        tour.setMaxCapacity(dto.getMaxCapacity());
        return tour;
    }

    public BookingDTO toBookingDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setTourId(booking.getTour().getId());
        dto.setTouristId(booking.getTourist().getId());
        dto.setNumberOfPeople(booking.getNumberOfPeople());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setBookingDate(booking.getBookingDate());
        dto.setStatus(BookingDTO.BookingStatus.valueOf(booking.getStatus().toString()));
        
        if (booking.getTour() != null) {
            dto.setTourName(booking.getTour().getName());
            dto.setTourStartDate(booking.getTour().getStartDate().atStartOfDay());
            dto.setTourEndDate(booking.getTour().getEndDate().atStartOfDay());
        }
        
        if (booking.getTourist() != null) {
            dto.setTouristName(booking.getTourist().getUser().getFirstName() + " " + 
                             booking.getTourist().getUser().getLastName());
        }
        
        return dto;
    }

    public Booking toBookingEntity(BookingDTO dto) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setNumberOfPeople(dto.getNumberOfPeople());
        booking.setTotalPrice(dto.getTotalPrice());
        booking.setBookingDate(dto.getBookingDate());
        booking.setStatus(Booking.BookingStatus.valueOf(dto.getStatus().toString()));
        return booking;
    }

    public UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRoles(user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));
        return dto;
    }

    public User toUserEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        return user;
    }

    public TouristDTO toTouristDTO(Tourist tourist) {
        TouristDTO dto = new TouristDTO();
        dto.setId(tourist.getId());
        dto.setUserId(tourist.getUser().getId());
        dto.setPassportNumber(tourist.getPassportNumber());
        dto.setNationality(tourist.getNationality());
        dto.setDateOfBirth(tourist.getDateOfBirth());
        dto.setEmergencyContact(tourist.getEmergencyContact());
        dto.setPreferences(tourist.getPreferences());
        dto.setSpecialRequirements(tourist.getSpecialRequirements());
        
        if (tourist.getUser() != null) {
            dto.setFullName(tourist.getUser().getFirstName() + " " + tourist.getUser().getLastName());
            dto.setEmail(tourist.getUser().getEmail());
        }
        
        return dto;
    }

    public Tourist toTouristEntity(TouristDTO dto) {
        Tourist tourist = new Tourist();
        tourist.setId(dto.getId());
        tourist.setPassportNumber(dto.getPassportNumber());
        tourist.setNationality(dto.getNationality());
        tourist.setDateOfBirth(dto.getDateOfBirth());
        tourist.setEmergencyContact(dto.getEmergencyContact());
        tourist.setPreferences(dto.getPreferences());
        tourist.setSpecialRequirements(dto.getSpecialRequirements());
        return tourist;
    }

    public GuideDTO toGuideDTO(Guide guide) {
        GuideDTO dto = new GuideDTO();
        dto.setId(guide.getId());
        dto.setName(guide.getName());
        dto.setContactInfo(guide.getContactInfo());
        dto.setExpertise(guide.getExpertise());
        dto.setYearsOfExperience(guide.getYearsOfExperience());
        dto.setLanguages(guide.getLanguages());
        dto.setRating(guide.getRating());
        dto.setAvailability(guide.getAvailability());
        return dto;
    }

    public Guide toGuideEntity(GuideDTO dto) {
        Guide guide = new Guide();
        guide.setId(dto.getId());
        guide.setName(dto.getName());
        guide.setContactInfo(dto.getContactInfo());
        guide.setExpertise(dto.getExpertise());
        guide.setYearsOfExperience(dto.getYearsOfExperience());
        guide.setLanguages(dto.getLanguages());
        guide.setRating(dto.getRating());
        guide.setAvailability(dto.getAvailability());
        return guide;
    }

    public ReviewDTO toReviewDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setTourId(review.getTour().getId());
        dto.setTouristId(review.getTourist().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setReviewDate(review.getReviewDate());
        
        if (review.getTour() != null) {
            dto.setTourName(review.getTour().getName());
            if (review.getTour().getGuide() != null) {
                dto.setGuideName(review.getTour().getGuide().getName());
            }
            if (review.getTour().getDestination() != null) {
                dto.setDestinationName(review.getTour().getDestination().getName());
            }
        }
        
        if (review.getTourist() != null && review.getTourist().getUser() != null) {
            dto.setTouristName(review.getTourist().getUser().getFirstName() + " " + 
                             review.getTourist().getUser().getLastName());
        }
        
        return dto;
    }

    public Review toReviewEntity(ReviewDTO dto) {
        Review review = new Review();
        review.setId(dto.getId());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setReviewDate(dto.getReviewDate());
        return review;
    }
}
