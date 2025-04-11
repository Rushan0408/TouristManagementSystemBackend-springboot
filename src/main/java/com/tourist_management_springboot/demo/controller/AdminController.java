package com.tourist_management_springboot.demo.controller;

import com.tourist_management_springboot.demo.model.Booking;
import com.tourist_management_springboot.demo.model.User;
import com.tourist_management_springboot.demo.repository.BookingRepository;
import com.tourist_management_springboot.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();

        // Remove passwords before returning
        users.forEach(user -> user.setPassword(""));

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/bookings/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable String status) {
        try {
            Booking.BookingStatus bookingStatus = Booking.BookingStatus.valueOf(status.toUpperCase());
            List<Booking> bookings = bookingRepository.findByStatus(bookingStatus);
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();

        // Count users
        dashboardData.put("userCount", userRepository.count());

        // Count bookings by status
        dashboardData.put("pendingBookings", bookingRepository.findByStatus(Booking.BookingStatus.PENDING).size());
        dashboardData.put("confirmedBookings", bookingRepository.findByStatus(Booking.BookingStatus.CONFIRMED).size());
        dashboardData.put("completedBookings", bookingRepository.findByStatus(Booking.BookingStatus.COMPLETED).size());
        dashboardData.put("cancelledBookings", bookingRepository.findByStatus(Booking.BookingStatus.CANCELLED).size());

        return new ResponseEntity<>(dashboardData, HttpStatus.OK);
    }
}

