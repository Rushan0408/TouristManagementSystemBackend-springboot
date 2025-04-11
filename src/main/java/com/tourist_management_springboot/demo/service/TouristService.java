package com.tourist_management_springboot.demo.service;

import com.tourist_management_springboot.demo.model.Tourist;
import java.util.List;
import java.util.Optional;

public interface TouristService {
    List<Tourist> getAllTourists();
    Optional<Tourist> getTouristById(Long id);
    Tourist createTourist(Tourist tourist);
    Tourist updateTourist(Long id, Tourist touristDetails);
    void deleteTourist(Long id);
    List<Tourist> getTouristsByCountry(String country);
    List<Tourist> searchTourists(String keyword);
    List<Tourist> getFrequentTourists();
    Optional<Tourist> getTouristByUserId(Long userId);
    void updateTouristPreferences(Long id, List<String> preferences);
}
