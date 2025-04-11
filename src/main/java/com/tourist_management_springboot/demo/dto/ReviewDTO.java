package com.tourist_management_springboot.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;

    @NotNull(message = "Tour ID is required")
    private Long tourId;

    @NotNull(message = "Tourist ID is required")
    private Long touristId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    @Size(min = 10, max = 500, message = "Comment must be between 10 and 500 characters")
    private String comment;

    private LocalDateTime reviewDate;
    
    // Additional fields for response
    private String touristName;
    private String tourName;
    private String guideName;
    private String destinationName;
}
