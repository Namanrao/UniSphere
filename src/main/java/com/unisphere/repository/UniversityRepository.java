package com.unisphere.repository;

import com.unisphere.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    Optional<University> findByName(String name);
    Optional<University> findByDomain(String domain);
    Boolean existsByName(String name);
    Boolean existsByDomain(String domain);
}