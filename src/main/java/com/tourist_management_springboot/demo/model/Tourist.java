package com.tourist_management_springboot.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tourists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tourist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String nationality;
    private String passportNumber;
    private LocalDate dateOfBirth;
    private String emergencyContact;
    private String specialRequirements;

    @ElementCollection
    private List<String> preferences = new ArrayList<>();

    @OneToMany(mappedBy = "tourist", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
}
