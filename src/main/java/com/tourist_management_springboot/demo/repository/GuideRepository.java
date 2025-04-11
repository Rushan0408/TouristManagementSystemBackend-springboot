package com.tourist_management_springboot.demo.repository;

import com.tourist_management_springboot.demo.model.Guide;
import com.tourist_management_springboot.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
    Optional<Guide> findByUser(User user);
    List<Guide> findByExpertiseContaining(String expertise);
    List<Guide> findTop5ByOrderByRatingDesc();
    List<Guide> findByAvailabilityTrue();
}
