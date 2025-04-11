package com.tourist_management_springboot.demo.repository;

import com.tourist_management_springboot.demo.model.Booking;
import com.tourist_management_springboot.demo.model.Tourist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTouristId(Long touristId);
    List<Booking> findByTourId(Long tourId);
    List<Booking> findByStatus(Booking.BookingStatus status);
    List<Booking> findByTourist(Tourist tourist);
}
