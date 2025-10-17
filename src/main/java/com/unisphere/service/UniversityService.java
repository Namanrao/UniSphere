package com.unisphere.service;

import com.unisphere.dto.UniversityDTO;
import com.unisphere.entity.University;
import com.unisphere.entity.User;
import com.unisphere.entity.UserUniversity;
import com.unisphere.enums.VerificationStatus;
import com.unisphere.repository.UniversityRepository;
import com.unisphere.repository.UserUniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UserUniversityRepository userUniversityRepository;

    // Get all universities
    public List<UniversityDTO.Response> getAllUniversities() {
        List<University> universities = universityRepository.findAll();
        return universities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get university by ID
    public Optional<UniversityDTO.Response> getUniversityById(Long id) {
        return universityRepository.findById(id)
                .map(this::convertToDTO);
    }

    // Create new university
    public UniversityDTO.Response createUniversity(UniversityDTO.CreateRequest request) {
        // Check if university with same name or domain already exists
        if (universityRepository.existsByName(request.getName())) {
            throw new RuntimeException("University with name '" + request.getName() + "' already exists");
        }
        if (universityRepository.existsByDomain(request.getDomain())) {
            throw new RuntimeException("University with domain '" + request.getDomain() + "' already exists");
        }

        University university = new University();
        university.setName(request.getName());
        university.setDomain(request.getDomain());
        university.setDescription(request.getDescription());
        university.setLocation(request.getLocation());

        University savedUniversity = universityRepository.save(university);
        return convertToDTO(savedUniversity);
    }

    // Update university
    public Optional<UniversityDTO.Response> updateUniversity(Long id, UniversityDTO.CreateRequest request) {
        return universityRepository.findById(id)
                .map(university -> {
                    // Check if name is being changed and if it conflicts
                    if (!university.getName().equals(request.getName()) &&
                            universityRepository.existsByName(request.getName())) {
                        throw new RuntimeException("University with name '" + request.getName() + "' already exists");
                    }

                    // Check if domain is being changed and if it conflicts
                    if (!university.getDomain().equals(request.getDomain()) &&
                            universityRepository.existsByDomain(request.getDomain())) {
                        throw new RuntimeException("University with domain '" + request.getDomain() + "' already exists");
                    }

                    university.setName(request.getName());
                    university.setDomain(request.getDomain());
                    university.setDescription(request.getDescription());
                    university.setLocation(request.getLocation());

                    University updatedUniversity = universityRepository.save(university);
                    return convertToDTO(updatedUniversity);
                });
    }

    // Delete university
    public boolean deleteUniversity(Long id) {
        if (universityRepository.existsById(id)) {
            University university = universityRepository.findById(id).get();

            // Check if any users are associated with this university (using the correct method name)
            long userCount = userUniversityRepository.countByUniversityAndVerificationStatus(
                    university,
                    VerificationStatus.VERIFIED
            );

            if (userCount > 0) {
                throw new RuntimeException("Cannot delete university with " + userCount + " associated users");
            }

            universityRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Associate user with university
    public UserUniversity associateUserWithUniversity(User user, University university) {
        // Check if association already exists
        Optional<UserUniversity> existingAssociation =
                userUniversityRepository.findByUserAndUniversity(user, university);

        if (existingAssociation.isPresent()) {
            return existingAssociation.get();
        }

        UserUniversity userUniversity = new UserUniversity(user, university);

        // Auto-verify if email domain matches university domain
        if (university.isEmailDomainValid(user.getEmail())) {
            userUniversity.setVerificationStatus(VerificationStatus.VERIFIED);
        } else {
            // Generate verification token for manual verification
            userUniversity.setVerificationToken(generateVerificationToken());
            userUniversity.setVerificationTokenExpiry(LocalDateTime.now().plusDays(7));
        }

        return userUniversityRepository.save(userUniversity);
    }

    // Get user's universities
    public List<UniversityDTO.UserUniversityResponse> getUserUniversities(User user) {
        List<UserUniversity> userUniversities = userUniversityRepository.findByUser(user);
        return userUniversities.stream()
                .map(this::convertToUserUniversityDTO)
                .collect(Collectors.toList());
    }

    // Get user's verified university
    public Optional<University> getUserVerifiedUniversity(User user) {
        return userUniversityRepository.findVerifiedUniversityByUser(user)
                .map(UserUniversity::getUniversity);
    }

    // Verify university association
    public boolean verifyUniversityAssociation(String verificationToken) {
        return userUniversityRepository.findByVerificationToken(verificationToken)
                .map(userUniversity -> {
                    if (userUniversity.getVerificationTokenExpiry() != null &&
                            userUniversity.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
                        userUniversity.setVerificationStatus(VerificationStatus.EXPIRED);
                        userUniversityRepository.save(userUniversity);
                        return false;
                    }

                    userUniversity.setVerificationStatus(VerificationStatus.VERIFIED);
                    userUniversity.setVerificationToken(null);
                    userUniversity.setVerificationTokenExpiry(null);
                    userUniversityRepository.save(userUniversity);
                    return true;
                })
                .orElse(false);
    }

    // Helper method to convert University to DTO
    private UniversityDTO.Response convertToDTO(University university) {
        UniversityDTO.Response dto = new UniversityDTO.Response();
        dto.setId(university.getId());
        dto.setName(university.getName());
        dto.setDomain(university.getDomain());
        dto.setDescription(university.getDescription());
        dto.setLocation(university.getLocation());
        dto.setStatus(university.getStatus().name());
        dto.setCreatedAt(university.getCreatedAt());
        dto.setUpdatedAt(university.getUpdatedAt());

        // Count verified students (using the correct method name)
        long studentCount = userUniversityRepository.countByUniversityAndVerificationStatus(
                university,
                VerificationStatus.VERIFIED
        );
        dto.setStudentCount(studentCount);

        return dto;
    }

    // Helper method to convert UserUniversity to DTO
    private UniversityDTO.UserUniversityResponse convertToUserUniversityDTO(UserUniversity userUniversity) {
        UniversityDTO.UserUniversityResponse dto = new UniversityDTO.UserUniversityResponse();
        dto.setId(userUniversity.getId());
        dto.setUniversity(convertToDTO(userUniversity.getUniversity()));
        dto.setVerificationStatus(userUniversity.getVerificationStatus().name());
        dto.setJoinedAt(userUniversity.getJoinedAt());
        return dto;
    }

    // Generate verification token
    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    // Find university by email domain
    public Optional<University> findUniversityByEmailDomain(String email) {
        String domain = extractDomainFromEmail(email);
        if (domain != null) {
            return universityRepository.findByDomain(domain);
        }
        return Optional.empty();
    }

    // Extract domain from email
    private String extractDomainFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }
        return email.substring(email.indexOf("@") + 1).toLowerCase();
    }
}