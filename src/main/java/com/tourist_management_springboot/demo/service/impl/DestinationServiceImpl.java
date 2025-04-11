package com.tourist_management_springboot.demo.service.impl;

import com.tourist_management_springboot.demo.model.Destination;
import com.tourist_management_springboot.demo.repository.DestinationRepository;
import com.tourist_management_springboot.demo.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DestinationServiceImpl implements DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    @Override
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    @Override
    public Optional<Destination> getDestinationById(Long id) {
        return destinationRepository.findById(id);
    }

    @Override
    public Destination createDestination(Destination destination) {
        return destinationRepository.save(destination);
    }

    @Override
    public Destination updateDestination(Long id, Destination destinationDetails) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found with id: " + id));

        destination.setName(destinationDetails.getName());
        destination.setDescription(destinationDetails.getDescription());
        destination.setLocation(destinationDetails.getLocation());
        destination.setImageUrl(destinationDetails.getImageUrl());

        return destinationRepository.save(destination);
    }

    @Override
    public void deleteDestination(Long id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found with id: " + id));
        destinationRepository.delete(destination);
    }

    @Override
    public List<Destination> searchDestinations(String keyword) {
        return destinationRepository.findByNameContainingOrDescriptionContainingOrLocationContaining(
                keyword, keyword, keyword);
    }

    @Override
    public List<Destination> getPopularDestinations() {
        // This could be implemented based on number of tours or bookings
        // For now, returning all destinations
        return destinationRepository.findAll();
    }
}
