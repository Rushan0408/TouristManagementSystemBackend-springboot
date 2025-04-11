package com.tourist_management_springboot.demo.repository;


import com.tourist_management_springboot.demo.model.Tourist;
import com.tourist_management_springboot.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TouristRepository extends JpaRepository<Tourist, Long> {
    Optional<Tourist> findByUser(User user);

    List<Tourist> findByNationality(String country);

    List<Tourist> findByPassportNumberContainingOrNationalityContaining(String keyword, String keyword2);

    Optional<Tourist> findByUserId(Long userId);
}