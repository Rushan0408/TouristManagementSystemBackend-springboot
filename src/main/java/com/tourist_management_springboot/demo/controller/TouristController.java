package com.tourist_management_springboot.demo.controller;

import com.tourist_management_springboot.demo.model.Tourist;
import com.tourist_management_springboot.demo.model.User;
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

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tourists")
public class TouristController {
    @Autowired
    private TouristRepository touristRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Tourist>> getAllTourists() {
        List<Tourist> tourists = touristRepository.findAll();
        return new ResponseEntity<>(tourists, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Tourist> getTouristById(@PathVariable Long id) {
        Optional<Tourist> touristData = touristRepository.findById(id);

        if (touristData.isPresent()) {
            Tourist tourist = touristData.get();

            // Check if current user is the owner or an admin
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                    tourist.getUser().getId().equals(userDetails.getId())) {
                return new ResponseEntity<>(tourist, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Tourist> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        Optional<Tourist> touristOptional = touristRepository.findByUser(user);

        return touristOptional.map(tourist -> new ResponseEntity<>(tourist, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Tourist> createTourist(@RequestBody Tourist tourist) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Optional<User> userOptional = userRepository.findById(userDetails.getId());
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            User user = userOptional.get();

            // Check if tourist profile already exists
            Optional<Tourist> existingTourist = touristRepository.findByUser(user);
            if (existingTourist.isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            tourist.setUser(user);
            Tourist newTourist = touristRepository.save(tourist);
            return new ResponseEntity<>(newTourist, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Tourist> updateTourist(@PathVariable Long id, @RequestBody Tourist tourist) {
        Optional<Tourist> touristData = touristRepository.findById(id);

        if (touristData.isPresent()) {
            Tourist existingTourist = touristData.get();

            // Security check
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                    existingTourist.getUser().getId().equals(userDetails.getId())) {

                // Update fields, but don't change the user
                existingTourist.setNationality(tourist.getNationality());
                existingTourist.setPassportNumber(tourist.getPassportNumber());
                existingTourist.setPreferences(tourist.getPreferences());

                return new ResponseEntity<>(touristRepository.save(existingTourist), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteTourist(@PathVariable Long id) {
        try {
            touristRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}