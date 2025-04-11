package com.tourist_management_springboot.demo.controller;

import com.tourist_management_springboot.demo.model.Destination;
import com.tourist_management_springboot.demo.model.Guide;
import com.tourist_management_springboot.demo.model.Tour;
import com.tourist_management_springboot.demo.repository.DestinationRepository;
import com.tourist_management_springboot.demo.repository.GuideRepository;
import com.tourist_management_springboot.demo.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/public")
public class PublicController {
    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private GuideRepository guideRepository;

    @GetMapping("/destinations")
    public ResponseEntity<List<Destination>> getAllDestinations() {
        List<Destination> destinations = destinationRepository.findAll();
        return new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    @GetMapping("/destinations/{id}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) {
        Optional<Destination> destinationData = destinationRepository.findById(id);

        return destinationData.map(destination -> new ResponseEntity<>(destination, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/tours")
    public ResponseEntity<List<Tour>> getUpcomingTours() {
        List<Tour> tours = tourRepository.findByStartDateAfter(LocalDate.now());
        return new ResponseEntity<>(tours, HttpStatus.OK);
    }

    @GetMapping("/tours/{id}")
    public ResponseEntity<Tour> getTourById(@PathVariable Long id) {
        Optional<Tour> tourData = tourRepository.findById(id);

        return tourData.map(tour -> new ResponseEntity<>(tour, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/guides")
    public ResponseEntity<List<Guide>> getAllGuides() {
        List<Guide> guides = guideRepository.findAll();
        return new ResponseEntity<>(guides, HttpStatus.OK);
    }

    @GetMapping("/guides/{id}")
    public ResponseEntity<Guide> getGuideById(@PathVariable Long id) {
        Optional<Guide> guideData = guideRepository.findById(id);

        return guideData.map(guide -> new ResponseEntity<>(guide, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}