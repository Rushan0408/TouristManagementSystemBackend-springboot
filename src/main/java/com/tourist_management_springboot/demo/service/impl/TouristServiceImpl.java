package com.tourist_management_springboot.demo.service.impl;

import com.tourist_management_springboot.demo.model.Tourist;
import com.tourist_management_springboot.demo.repository.TouristRepository;
import com.tourist_management_springboot.demo.service.TouristService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TouristServiceImpl implements TouristService {

    @Autowired
    private TouristRepository touristRepository;

    @Override
    public List<Tourist> getAllTourists() {
        return touristRepository.findAll();
    }

    @Override
    public Optional<Tourist> getTouristById(Long id) {
        return touristRepository.findById(id);
    }

    @Override
    public Tourist createTourist(Tourist tourist) {
        return touristRepository.save(tourist);
    }

    @Override
    public Tourist updateTourist(Long id, Tourist touristDetails) {
        Tourist tourist = touristRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tourist not found with id: " + id));

        tourist.setPassportNumber(touristDetails.getPassportNumber());
        tourist.setNationality(touristDetails.getNationality());
        tourist.setDateOfBirth(touristDetails.getDateOfBirth());
        tourist.setEmergencyContact(touristDetails.getEmergencyContact());
        tourist.setPreferences(touristDetails.getPreferences());
        tourist.setSpecialRequirements(touristDetails.getSpecialRequirements());

        return touristRepository.save(tourist);
    }

    @Override
    public void deleteTourist(Long id) {
        Tourist tourist = touristRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tourist not found with id: " + id));
        touristRepository.delete(tourist);
    }

    @Override
    public List<Tourist> getTouristsByCountry(String country) {
        return touristRepository.findByNationality(country);
    }

    @Override
    public List<Tourist> searchTourists(String keyword) {
        return touristRepository.findByPassportNumberContainingOrNationalityContaining(keyword, keyword);
    }

    @Override
    public List<Tourist> getFrequentTourists() {
        // This could be implemented based on number of bookings
        // For now, returning all tourists
        return touristRepository.findAll();
    }

    @Override
    public Optional<Tourist> getTouristByUserId(Long userId) {
        return touristRepository.findByUserId(userId);
    }

    @Override
    public void updateTouristPreferences(Long id, List<String> preferences) {
        Tourist tourist = touristRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tourist not found with id: " + id));
        tourist.setPreferences(preferences);
        touristRepository.save(tourist);
    }
}
