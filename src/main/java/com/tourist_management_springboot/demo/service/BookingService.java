package com.tourist_management_springboot.demo.service;

import com.tourist_management_springboot.demo.model.Booking;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    List<Booking> getAllBookings();
    Optional<Booking> getBookingById(Long id);
    Booking createBooking(Booking booking);
    Booking updateBooking(Long id, Booking bookingDetails);
    void deleteBooking(Long id);
    List<Booking> getBookingsByTour(Long tourId);
    List<Booking> getBookingsByTourist(Long touristId);
    boolean isBookingAvailable(Long tourId, int numberOfPeople);
}
