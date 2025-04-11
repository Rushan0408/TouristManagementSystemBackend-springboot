package com.tourist_management_springboot.demo.controller;

import com.tourist_management_springboot.demo.model.Guide;
import com.tourist_management_springboot.demo.repository.GuideRepository;
import com.tourist_management_springboot.demo.repository.UserRepository;
import com.tourist_management_springboot.demo.security.services.UserDetailsImpl;
import org.apache.catalina.User;
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
@RequestMapping("/api/guides")
public class GuideController {
    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Guide>> getAllGuides() {
        List<Guide> guides = guideRepository.findAll();
        return new ResponseEntity<>(guides, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guide> getGuideById(@PathVariable Long id) {
        Optional<Guide> guideData = guideRepository.findById(id);

        return guideData.map(guide -> new ResponseEntity<>(guide, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('GUIDE')")
    public ResponseEntity<Guide> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<com.tourist_management_springboot.demo.model.User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = (User) userOptional.get();
        Optional<Guide> guideOptional = guideRepository.findByUser((com.tourist_management_springboot.demo.model.User) user);

        return guideOptional.map(guide -> new ResponseEntity<>(guide, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @PreAuthorize("hasRole('GUIDE')")
    public ResponseEntity<Guide> createGuide(@RequestBody Guide guide) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Optional<com.tourist_management_springboot.demo.model.User> userOptional = userRepository.findById(userDetails.getId());
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            User user = (User) userOptional.get();

            // Check if guide profile already exists
            Optional<Guide> existingGuide = guideRepository.findByUser((com.tourist_management_springboot.demo.model.User) user);
            if (existingGuide.isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            guide.setUser((com.tourist_management_springboot.demo.model.User) user);
            Guide newGuide = guideRepository.save(guide);
            return new ResponseEntity<Guide>(newGuide, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GUIDE') or hasRole('ADMIN')")
    public ResponseEntity<Guide> updateGuide(@PathVariable Long id, @RequestBody Guide guide) {
        Optional<Guide> guideData = guideRepository.findById(id);

        if (guideData.isPresent()) {
            Guide existingGuide = guideData.get();

            // Security check
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                    existingGuide.getUser().getId().equals(userDetails.getId())) {

                // Update fields, but don't change the user
                existingGuide.setBio(guide.getBio());
                existingGuide.setYearsOfExperience(guide.getYearsOfExperience());
                existingGuide.setLanguages(guide.getLanguages());
                existingGuide.setSpecialties(guide.getSpecialties());

                return new ResponseEntity<>(guideRepository.save(existingGuide), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteGuide(@PathVariable Long id) {
        try {
            guideRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}