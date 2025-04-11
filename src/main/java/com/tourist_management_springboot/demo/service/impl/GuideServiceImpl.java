package com.tourist_management_springboot.demo.service.impl;

import com.tourist_management_springboot.demo.model.Guide;
import com.tourist_management_springboot.demo.repository.GuideRepository;
import com.tourist_management_springboot.demo.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GuideServiceImpl implements GuideService {

    @Autowired
    private GuideRepository guideRepository;

    @Override
    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }

    @Override
    public Optional<Guide> getGuideById(Long id) {
        return guideRepository.findById(id);
    }

    @Override
    public Guide createGuide(Guide guide) {
        return guideRepository.save(guide);
    }

    @Override
    public Guide updateGuide(Long id, Guide guideDetails) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide not found with id: " + id));

        guide.setName(guideDetails.getName());
        guide.setContactInfo(guideDetails.getContactInfo());
        guide.setExpertise(guideDetails.getExpertise());
        guide.setLanguages(guideDetails.getLanguages());
        guide.setYearsOfExperience(guideDetails.getYearsOfExperience());
        guide.setRating(guideDetails.getRating());
        guide.setAvailability(guideDetails.getAvailability());

        return guideRepository.save(guide);
    }

    @Override
    public void deleteGuide(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guide not found with id: " + id));
        guideRepository.delete(guide);
    }

    @Override
    public List<Guide> getAvailableGuides() {
        return guideRepository.findByAvailabilityTrue();
    }

    @Override
    public List<Guide> searchGuidesByExpertise(String expertise) {
        return guideRepository.findByExpertiseContaining(expertise);
    }

    @Override
    public List<Guide> getTopRatedGuides() {
        return guideRepository.findTop5ByOrderByRatingDesc();
    }
}
