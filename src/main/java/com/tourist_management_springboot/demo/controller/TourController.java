package com.tourist_management_springboot.demo.controller;

import com.tourist_management_springboot.demo.dto.TourDTO;
import com.tourist_management_springboot.demo.mapper.EntityMapper;

import com.tourist_management_springboot.demo.model.Tour;
import com.tourist_management_springboot.demo.service.TourService;
import com.tourist_management_springboot.demo.service.DestinationService;
import com.tourist_management_springboot.demo.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tours")
@Validated
public class TourController {
    @Autowired
    private TourService tourService;

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private GuideService guideService;

    @Autowired
    private EntityMapper entityMapper;

    @GetMapping
    public ResponseEntity<List<TourDTO>> getAllTours() {
        List<Tour> tours = tourService.getAllTours();
        List<TourDTO> tourDTOs = tours.stream()
                .map(entityMapper::toTourDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(tourDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourDTO> getTourById(@PathVariable Long id) {
        return tourService.getTourById(id)
                .map(tour -> new ResponseEntity<>(entityMapper.toTourDTO(tour), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/future")
    public ResponseEntity<List<TourDTO>> getFutureTours() {
        List<Tour> tours = tourService.getAvailableTours();
        List<TourDTO> tourDTOs = tours.stream()
                .map(entityMapper::toTourDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(tourDTOs, HttpStatus.OK);
    }

    @GetMapping("/destination/{destinationId}")
    public ResponseEntity<List<TourDTO>> getToursByDestination(@PathVariable Long destinationId) {
        List<Tour> tours = tourService.getToursByDestination(destinationId);
        List<TourDTO> tourDTOs = tours.stream()
                .map(entityMapper::toTourDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(tourDTOs, HttpStatus.OK);
    }

    @GetMapping("/guide/{guideId}")
    public ResponseEntity<List<TourDTO>> getToursByGuide(@PathVariable Long guideId) {
        List<Tour> tours = tourService.getToursByGuide(guideId);
        List<TourDTO> tourDTOs = tours.stream()
                .map(entityMapper::toTourDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(tourDTOs, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GUIDE')")
    public ResponseEntity<TourDTO> createTour(@Valid @RequestBody TourDTO tourDTO) {
        try {
            // Convert DTO to entity
            Tour tour = entityMapper.toTourEntity(tourDTO);
            
            // Set relationships
            destinationService.getDestinationById(tourDTO.getDestinationId())
                    .ifPresent(tour::setDestination);
            guideService.getGuideById(tourDTO.getGuideId())
                    .ifPresent(tour::setGuide);

            // Create tour and convert back to DTO
            Tour newTour = tourService.createTour(tour);
            return new ResponseEntity<>(entityMapper.toTourDTO(newTour), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GUIDE')")
    public ResponseEntity<TourDTO> updateTour(@PathVariable Long id, @Valid @RequestBody TourDTO tourDTO) {
        try {
            Tour tour = entityMapper.toTourEntity(tourDTO);
            
            // Set relationships
            if (tourDTO.getDestinationId() != null) {
                destinationService.getDestinationById(tourDTO.getDestinationId())
                        .ifPresent(tour::setDestination);
            }
            if (tourDTO.getGuideId() != null) {
                guideService.getGuideById(tourDTO.getGuideId())
                        .ifPresent(tour::setGuide);
            }

            Tour updatedTour = tourService.updateTour(id, tour);
            return new ResponseEntity<>(entityMapper.toTourDTO(updatedTour), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteTour(@PathVariable Long id) {
        try {
            tourService.deleteTour(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}