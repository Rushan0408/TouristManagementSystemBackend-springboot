package com.tourist_management_springboot.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import java.util.List;

@Data
public class GuideDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Contact information is required")
    @Email(message = "Please provide a valid email address")
    private String contactInfo;

    @NotBlank(message = "Expertise is required")
    @Size(min = 2, max = 200, message = "Expertise must be between 2 and 200 characters")
    private String expertise;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;

    private List<String> languages;
    private Double rating;
    private Boolean availability;
    
    // Additional fields for response
    private Integer numberOfTours;
    private Integer numberOfReviews;
    private List<String> specializations;
    private List<String> upcomingTours;
}
