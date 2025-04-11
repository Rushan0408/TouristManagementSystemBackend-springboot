package com.tourist_management_springboot.demo.controller;

import com.tourist_management_springboot.demo.model.Booking;
import com.tourist_management_springboot.demo.model.Tour;
import com.tourist_management_springboot.demo.model.Tourist;
import com.tourist_management_springboot.demo.model.User;
import com.tourist_management_springboot.demo.repository.BookingRepository;
import com.tourist_management_springboot.demo.repository.TourRepository;
import com.tourist_management_springboot.demo.repository.TouristRepository;
import com.tourist_management_springboot.demo.repository.UserRepository;
import com.tourist_management_springboot.demo.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TouristRepository touristRepository;

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GUIDE')")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GUIDE') or hasRole('USER')")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> bookingData = bookingRepository.findById(id);

        if (bookingData.isPresent()) {
            Booking booking = bookingData.get();

            // Check if current user is admin, guide or owner
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isGuide = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_GUIDE"));
            boolean isOwner = booking.getTourist().getUser().getId().equals(userDetails.getId());

            if (isAdmin || isGuide || isOwner) {
                return new ResponseEntity<>(booking, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Booking>> getMyBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        Optional<Tourist> touristOptional = touristRepository.findByUser(user);


        if (touristOptional.isPresent()) {
            List<Booking> bookings = bookingRepository.findByTourist(touristOptional.get());
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        try {
            // Get current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Optional<User> userOptional = userRepository.findById(userDetails.getId());
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            User user = userOptional.get();
            Optional<Tourist> touristOptional = touristRepository.findByUser(user);

            if (touristOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Tourist tourist = touristOptional.get();

            // Validate tour
            if (booking.getTour() == null || booking.getTour().getId() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Optional<Tour> tourOptional = tourRepository.findById(booking.getTour().getId());
            if (tourOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Tour tour = tourOptional.get();

            // Set booking details
            booking.setTourist(tourist);
            booking.setTour(tour);
            booking.setBookingDate(LocalDateTime.now());
            if (booking.getStatus() == null) {
                booking.setStatus(Booking.BookingStatus.PENDING);
            }

            // Calculate total amount
            if (booking.getNumberOfPeople() == null || booking.getNumberOfPeople() <= 0) {
                booking.setNumberOfPeople(1);
            }

            booking.setTotalPrice(tour.getPrice().multiply(java.math.BigDecimal.valueOf(booking.getNumberOfPeople())));

            Booking newBooking = bookingRepository.save(booking);
            return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        Optional<Booking> bookingData = bookingRepository.findById(id);

        if (bookingData.isPresent()) {
            Booking existingBooking = bookingData.get();

            // Security check
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isOwner = existingBooking.getTourist().getUser().getId().equals(userDetails.getId());

            if (isAdmin || isOwner) {
                // Update allowed fields
                if (booking.getNumberOfPeople() != null) {
                    existingBooking.setNumberOfPeople(booking.getNumberOfPeople());
                    // Recalculate total amount
                    existingBooking.setTotalPrice(existingBooking.getTour().getPrice()
                            .multiply(java.math.BigDecimal.valueOf(existingBooking.getNumberOfPeople())));
                }

                if (booking.getSpecialRequests() != null) {
                    existingBooking.setSpecialRequests(booking.getSpecialRequests());
                }

                // Only admins can change status
                if (isAdmin && booking.getStatus() != null) {
                    existingBooking.setStatus(booking.getStatus());
                }

                return new ResponseEntity<>(bookingRepository.save(existingBooking), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<HttpStatus> deleteBooking(@PathVariable Long id) {
        try {
            Optional<Booking> bookingData = bookingRepository.findById(id);

            if (bookingData.isPresent()) {
                Booking booking = bookingData.get();

                // Security check
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                boolean isAdmin = userDetails.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                boolean isOwner = booking.getTourist().getUser().getId().equals(userDetails.getId());

                if (isAdmin || isOwner) {
                    bookingRepository.deleteById(id);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        Optional<Booking> bookingData = bookingRepository.findById(id);

        if (bookingData.isPresent()) {
            Booking existingBooking = bookingData.get();

            // Security check
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isOwner = existingBooking.getTourist().getUser().getId().equals(userDetails.getId());

            if (isAdmin || isOwner) {
                existingBooking.setStatus(Booking.BookingStatus.CANCELLED);
                return new ResponseEntity<>(bookingRepository.save(existingBooking), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
