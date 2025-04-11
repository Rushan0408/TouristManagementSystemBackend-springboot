package com.tourist_management_springboot.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class DestinationDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotBlank(message = "Location is required")
    @Size(min = 2, max = 200, message = "Location must be between 2 and 200 characters")
    private String location;

    private String imageUrl;
    private String weatherInfo;
    private List<String> localAttractions;
    
    // Additional fields for response
    private Integer numberOfTours;
    private Double averageRating;
    private List<String> popularActivities;
    private String bestTimeToVisit;
}
