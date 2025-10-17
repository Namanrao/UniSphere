package com.unisphere.repository;

import com.unisphere.entity.Community;
import com.unisphere.entity.University;
import com.unisphere.enums.CommunityStatus;
import com.unisphere.enums.CommunityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    // Find by name and university (for uniqueness check)
    Optional<Community> findByNameAndUniversity(String name, University university);

    // Find all communities for a university
    List<Community> findByUniversity(University university);

    // Find all communities for a university with pagination
    Page<Community> findByUniversity(University university, Pageable pageable);

    // Find communities by university and status
    List<Community> findByUniversityAndStatus(University university, CommunityStatus status);

    // Find communities by type
    List<Community> findByType(CommunityType type);

    // Search communities by name (case insensitive)
    @Query("SELECT c FROM Community c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.displayName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Community> searchByNameOrDisplayName(@Param("query") String query);

    // Search communities within a university
    @Query("SELECT c FROM Community c WHERE c.university = :university AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.displayName) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Community> searchByUniversityAndName(@Param("university") University university, @Param("query") String query);

    // Count communities by university
    long countByUniversity(University university);

    // Find trending communities (most members)
    @Query("SELECT c FROM Community c WHERE c.status = com.unisphere.enums.CommunityStatus.ACTIVE ORDER BY c.memberCount DESC")
    List<Community> findTrendingCommunities(Pageable pageable);

    // Find communities by creator
    List<Community> findByCreatedBy_Id(Long userId);
}