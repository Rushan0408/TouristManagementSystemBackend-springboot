package com.tourist_management_springboot.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private Integer maxCapacity;
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Guide guide;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
}