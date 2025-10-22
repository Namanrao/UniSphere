package com.unisphere.controller;

import com.unisphere.dto.UniversityDTO;
import com.unisphere.entity.User;
import com.unisphere.entity.University;
import com.unisphere.repository.UserRepository;
import com.unisphere.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/universities")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @Autowired
    private UserRepository userRepository;

    // Get all universities (Public)
    @GetMapping
    public ResponseEntity<List<UniversityDTO.Response>> getAllUniversities() {
        List<UniversityDTO.Response> universities = universityService.getAllUniversities();
        return ResponseEntity.ok(universities);
    }

    // Get university by ID (Public)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUniversityById(@PathVariable Long id) {
        Optional<UniversityDTO.Response> university = universityService.getUniversityById(id);
        if (university.isPresent()) {
            return ResponseEntity.ok(university.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create university (Admin only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUniversity(@Valid @RequestBody UniversityDTO.CreateRequest request) {
        try {
            UniversityDTO.Response university = universityService.createUniversity(request);
            return ResponseEntity.ok(university);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update university (Admin only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUniversity(@PathVariable Long id,
                                              @Valid @RequestBody UniversityDTO.CreateRequest request) {
        try {
            Optional<UniversityDTO.Response> university = universityService.updateUniversity(id, request);
            if (university.isPresent()) {
                return ResponseEntity.ok(university.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete university (Admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUniversity(@PathVariable Long id) {
        try {
            boolean deleted = universityService.deleteUniversity(id);
            if (deleted) {
                return ResponseEntity.ok().body("University deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get current user's universities
    @GetMapping("/my-universities")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UniversityDTO.UserUniversityResponse>> getUserUniversities() {
        User currentUser = getCurrentUser();
        List<UniversityDTO.UserUniversityResponse> userUniversities =
                universityService.getUserUniversities(currentUser);
        return ResponseEntity.ok(userUniversities);
    }

    // Join a university (User)
    @PostMapping("/{universityId}/join")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> joinUniversity(@PathVariable Long universityId) {
        try {
            User currentUser = getCurrentUser();
            Optional<University> university = universityService.getUniversityById(universityId)
                    .map(dto -> {
                        // Convert DTO back to entity for association
                        University uni = new University();
                        uni.setId(dto.getId());
                        return uni;
                    });

            if (university.isPresent()) {
                var userUniversity = universityService.associateUserWithUniversity(currentUser, university.get());

                UniversityDTO.UserUniversityResponse response = new UniversityDTO.UserUniversityResponse();
                response.setId(userUniversity.getId());
                response.setUniversity(universityService.getUniversityById(universityId).get());
                response.setVerificationStatus(userUniversity.getVerificationStatus().name());
                response.setJoinedAt(userUniversity.getJoinedAt());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error joining university: " + e.getMessage());
        }
    }

    // Verify university association
    @PostMapping("/verify/{verificationToken}")
    public ResponseEntity<?> verifyUniversityAssociation(@PathVariable String verificationToken) {
        boolean verified = universityService.verifyUniversityAssociation(verificationToken);
        if (verified) {
            return ResponseEntity.ok("University association verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired verification token");
        }
    }

    // Helper method to get current authenticated user
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}