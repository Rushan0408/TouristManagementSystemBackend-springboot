package com.tourist_management_springboot.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guides")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String name;
    private String contactInfo;
    private String expertise;
    private String bio;
    private Integer yearsOfExperience;
    private Double rating;
    private Boolean availability;

    @ElementCollection
    private List<String> languages = new ArrayList<>();

    @ElementCollection
    private List<String> specialties = new ArrayList<>();

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL)
    private List<Tour> tours = new ArrayList<>();
}