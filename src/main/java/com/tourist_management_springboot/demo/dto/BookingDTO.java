package com.tourist_management_springboot.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long id;

    @NotNull(message = "Tour ID is required")
    private Long tourId;

    @NotNull(message = "Tourist ID is required")
    private Long touristId;

    @NotNull(message = "Number of people is required")
    @Positive(message = "Number of people must be positive")
    @Min(value = 1, message = "Minimum number of people is 1")
    private Integer numberOfPeople;

    @NotNull(message = "Total price is required")
    @Positive(message = "Total price must be positive")
    private BigDecimal totalPrice;

    private LocalDateTime bookingDate;
    private BookingStatus status;
    
    // Additional fields for response
    private String tourName;
    private String touristName;
    private LocalDateTime tourStartDate;
    private LocalDateTime tourEndDate;
    private String destinationName;
    private String guideName;
    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED
    }
}
