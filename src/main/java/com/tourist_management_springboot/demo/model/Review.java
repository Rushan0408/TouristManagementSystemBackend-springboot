package com.tourist_management_springboot.demo.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tourist_id")
    private Tourist tourist;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;
}
