package com.tourist_management_springboot.demo.service;

import com.tourist_management_springboot.demo.model.Tour;
import java.util.List;
import java.util.Optional;

public interface TourService {
    List<Tour> getAllTours();
    Optional<Tour> getTourById(Long id);
    Tour createTour(Tour tour);
    Tour updateTour(Long id, Tour tourDetails);
    void deleteTour(Long id);
    List<Tour> getToursByDestination(Long destinationId);
    List<Tour> getToursByGuide(Long guideId);
    List<Tour> getAvailableTours();
}
