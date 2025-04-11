package com.tourist_management_springboot.demo.repository;

import com.tourist_management_springboot.demo.model.Destination;
import com.tourist_management_springboot.demo.model.Guide;
import com.tourist_management_springboot.demo.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByDestination(Destination destination);
    List<Tour> findByGuide(Guide guide);
    List<Tour> findByStartDateAfter(LocalDate date);
    List<Tour> findByPriceLessThanEqual(Double price);
    List<Tour> findByDestinationId(Long destinationId);
    List<Tour> findByGuideId(Long guideId);
    List<Tour> findByEndDateAfter(LocalDate now);
}