package com.unisphere.repository;

import com.unisphere.entity.University;
import com.unisphere.entity.User;
import com.unisphere.entity.UserUniversity;
import com.unisphere.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserUniversityRepository extends JpaRepository<UserUniversity, Long> {

    // Find by user and university
    Optional<UserUniversity> findByUserAndUniversity(User user, University university);

    // Find all universities for a user
    List<UserUniversity> findByUser(User user);

    // Find all users for a university
    List<UserUniversity> findByUniversity(University university);

    // Find by verification status for a university
    List<UserUniversity> findByUniversityAndVerificationStatus(University university, VerificationStatus status);

    // Find by verification token
    Optional<UserUniversity> findByVerificationToken(String verificationToken);

    // Check if user is associated with any university
    boolean existsByUser(User user);

    // Add to src/main/java/com/unisphere/repository/UserUniversityRepository.java
    boolean existsByUserIdAndUniversityIdAndVerificationStatus(Long userId, Long universityId, VerificationStatus verificationStatus);

    // Count verified users for a university
    long countByUniversityAndVerificationStatus(University university, VerificationStatus status);

    // Find user's primary/verified university
    @Query("SELECT uu FROM UserUniversity uu WHERE uu.user = :user AND uu.verificationStatus = com.unisphere.enums.VerificationStatus.VERIFIED")
    Optional<UserUniversity> findVerifiedUniversityByUser(@Param("user") User user);
}