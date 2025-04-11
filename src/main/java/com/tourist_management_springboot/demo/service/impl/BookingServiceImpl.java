package com.tourist_management_springboot.demo.service.impl;

import com.tourist_management_springboot.demo.model.Booking;
import com.tourist_management_springboot.demo.model.Tour;
import com.tourist_management_springboot.demo.repository.BookingRepository;
import com.tourist_management_springboot.demo.repository.TourRepository;
import com.tourist_management_springboot.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TourRepository tourRepository;

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Booking createBooking(Booking booking) {
        if (!isBookingAvailable(booking.getTour().getId(), booking.getNumberOfPeople())) {
            throw new RuntimeException("Tour is fully booked or doesn't have enough capacity");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Long id, Booking bookingDetails) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        if (booking.getNumberOfPeople() != bookingDetails.getNumberOfPeople()) {
            if (!isBookingAvailable(booking.getTour().getId(), 
                    bookingDetails.getNumberOfPeople() - booking.getNumberOfPeople())) {
                throw new RuntimeException("Cannot update booking - tour doesn't have enough capacity");
            }
        }

        booking.setNumberOfPeople(bookingDetails.getNumberOfPeople());
        booking.setBookingDate(bookingDetails.getBookingDate());
        booking.setStatus(bookingDetails.getStatus());
        booking.setTotalPrice(bookingDetails.getTotalPrice());

        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        bookingRepository.delete(booking);
    }

    @Override
    public List<Booking> getBookingsByTour(Long tourId) {
        return bookingRepository.findByTourId(tourId);
    }

    @Override
    public List<Booking> getBookingsByTourist(Long touristId) {
        return bookingRepository.findByTouristId(touristId);
    }

    @Override
    public boolean isBookingAvailable(Long tourId, int numberOfPeople) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + tourId));

        int currentBookings = bookingRepository.findById(tourId).stream()
                .mapToInt(Booking::getNumberOfPeople)
                .sum();

        return (currentBookings + numberOfPeople) <= tour.getMaxCapacity();
    }
}
