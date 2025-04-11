package com.tourist_management_springboot.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "destinations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String location;
    private String imageUrl;

    @ElementCollection
    private List<String> activities = new ArrayList<>();

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
    private List<Tour> tours = new ArrayList<>();
}
