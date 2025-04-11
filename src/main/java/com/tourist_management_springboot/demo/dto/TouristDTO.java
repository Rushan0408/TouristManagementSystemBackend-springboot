package com.tourist_management_springboot.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
public class TouristDTO {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Passport number is required")
    @Size(min = 5, max = 20, message = "Passport number must be between 5 and 20 characters")
    private String passportNumber;

    @NotBlank(message = "Nationality is required")
    @Size(min = 2, max = 100, message = "Nationality must be between 2 and 100 characters")
    private String nationality;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Emergency contact is required")
    private String emergencyContact;

    private List<String> preferences;
    private String specialRequirements;
    
    // Additional fields for response
    private String fullName;
    private String email;
    private Integer numberOfBookings;
    private List<String> upcomingTours;
    private Double averageRating;
}
