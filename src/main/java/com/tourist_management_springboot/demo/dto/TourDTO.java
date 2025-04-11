package com.tourist_management_springboot.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TourDTO {
    private Long id;

    @NotBlank(message = "Tour name is required")
    @Size(min = 3, max = 100, message = "Tour name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer duration;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Maximum capacity is required")
    @Positive(message = "Maximum capacity must be positive")
    private Integer maxCapacity;

    @NotNull(message = "Destination ID is required")
    private Long destinationId;

    @NotNull(message = "Guide ID is required")
    private Long guideId;

    private String destinationName;
    private String guideName;
    private Integer availableSpots;
    private Double averageRating;
}
