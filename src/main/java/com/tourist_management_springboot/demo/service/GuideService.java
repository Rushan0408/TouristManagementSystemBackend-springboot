package com.tourist_management_springboot.demo.service;

import com.tourist_management_springboot.demo.model.Guide;
import java.util.List;
import java.util.Optional;

public interface GuideService {
    List<Guide> getAllGuides();
    Optional<Guide> getGuideById(Long id);
    Guide createGuide(Guide guide);
    Guide updateGuide(Long id, Guide guideDetails);
    void deleteGuide(Long id);
    List<Guide> getAvailableGuides();
    List<Guide> searchGuidesByExpertise(String expertise);
    List<Guide> getTopRatedGuides();
}
