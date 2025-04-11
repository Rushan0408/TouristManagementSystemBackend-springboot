package com.tourist_management_springboot.demo.service;

import com.tourist_management_springboot.demo.model.Destination;
import java.util.List;
import java.util.Optional;

public interface DestinationService {
    List<Destination> getAllDestinations();
    Optional<Destination> getDestinationById(Long id);
    Destination createDestination(Destination destination);
    Destination updateDestination(Long id, Destination destinationDetails);
    void deleteDestination(Long id);
    List<Destination> searchDestinations(String keyword);
    List<Destination> getPopularDestinations();
}
