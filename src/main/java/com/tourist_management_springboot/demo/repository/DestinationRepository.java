package com.tourist_management_springboot.demo.repository;

import com.tourist_management_springboot.demo.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    List<Destination> findByNameContainingIgnoreCase(String name);
    List<Destination> findByLocationContainingIgnoreCase(String location);
    List<Destination> findByNameContainingOrDescriptionContainingOrLocationContaining(String name, String description, String location);
}
