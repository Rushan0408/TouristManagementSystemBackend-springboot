package com.tourist_management_springboot.demo.controller;

import com.tourist_management_springboot.demo.model.Destination;
import com.tourist_management_springboot.demo.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/destinations")
public class DestinationController {
    @Autowired
    private DestinationRepository destinationRepository;

    @GetMapping
    public ResponseEntity<List<Destination>> getAllDestinations() {
        List<Destination> destinations = destinationRepository.findAll();
        return new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) {
        Optional<Destination> destinationData = destinationRepository.findById(id);

        return destinationData.map(destination -> new ResponseEntity<>(destination, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Destination>> searchDestinations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location) {

        if (name != null && !name.isEmpty()) {
            return new ResponseEntity<>(destinationRepository.findByNameContainingIgnoreCase(name), HttpStatus.OK);
        } else if (location != null && !location.isEmpty()) {
            return new ResponseEntity<>(destinationRepository.findByLocationContainingIgnoreCase(location), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(destinationRepository.findAll(), HttpStatus.OK);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Destination> createDestination(@RequestBody Destination destination) {
        try {
            Destination newDestination = destinationRepository.save(destination);
            return new ResponseEntity<>(newDestination, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Destination> updateDestination(@PathVariable Long id, @RequestBody Destination destination) {
        Optional<Destination> destinationData = destinationRepository.findById(id);

        if (destinationData.isPresent()) {
            Destination existingDestination = destinationData.get();
            existingDestination.setName(destination.getName());
            existingDestination.setDescription(destination.getDescription());
            existingDestination.setLocation(destination.getLocation());
            existingDestination.setImageUrl(destination.getImageUrl());
            existingDestination.setActivities(destination.getActivities());

            return new ResponseEntity<>(destinationRepository.save(existingDestination), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteDestination(@PathVariable Long id) {
        try {
            destinationRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}