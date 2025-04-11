package com.tourist_management_springboot.demo.service.impl;

import com.tourist_management_springboot.demo.model.Tour;
import com.tourist_management_springboot.demo.repository.TourRepository;
import com.tourist_management_springboot.demo.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TourServiceImpl implements TourService {

    @Autowired
    private TourRepository tourRepository;

    @Override
    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    @Override
    public Optional<Tour> getTourById(Long id) {
        return tourRepository.findById(id);
    }

    @Override
    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }

    @Override
    public Tour updateTour(Long id, Tour tourDetails) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + id));

        tour.setName(tourDetails.getName());
        tour.setDescription(tourDetails.getDescription());
        tour.setPrice(tourDetails.getPrice());
        tour.setDuration(tourDetails.getDuration());
        tour.setStartDate(tourDetails.getStartDate());
        tour.setEndDate(tourDetails.getEndDate());
        tour.setDestination(tourDetails.getDestination());
        tour.setGuide(tourDetails.getGuide());
        tour.setMaxCapacity(tourDetails.getMaxCapacity());

        return tourRepository.save(tour);
    }

    @Override
    public void deleteTour(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + id));
        tourRepository.delete(tour);
    }

    @Override
    public List<Tour> getToursByDestination(Long destinationId) {
        return tourRepository.findByDestinationId(destinationId);
    }

    @Override
    public List<Tour> getToursByGuide(Long guideId) {
        return tourRepository.findByGuideId(guideId);
    }

    @Override
    public List<Tour> getAvailableTours() {
        return tourRepository.findByEndDateAfter(java.time.LocalDate.now());
    }
}
