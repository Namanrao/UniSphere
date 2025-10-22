package com.unisphere.entity;

import com.unisphere.enums.UniversityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "universities",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
                @UniqueConstraint(columnNames = "domain")
        })
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String domain; // For email verification: e.g., "harvard.edu"

    @Size(max = 500)
    private String description;

    @Size(max = 100)
    private String location;

    @Enumerated(EnumType.STRING)
    private UniversityStatus status = UniversityStatus.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // No-argument constructor (REQUIRED by JPA)
    public University() {
    }

    // Constructor with basic fields
    public University(String name, String domain, String description, String location) {
        this.name = name;
        this.domain = domain;
        this.description = description;
        this.location = location;
    }

    // Constructor with all fields
    public University(Long id, String name, String domain, String description, String location, UniversityStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.description = description;
        this.location = location;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UniversityStatus getStatus() {
        return status;
    }

    public void setStatus(UniversityStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Optional: toString, equals, hashCode methods
    @Override
    public String toString() {
        return "University{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        University that = (University) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // Add these methods to your University.java class

    public boolean isEmailDomainValid(String email) {
        if (email == null || domain == null) return false;
        return email.toLowerCase().endsWith("@" + domain.toLowerCase());
    }

    public String extractEmailDomain(String email) {
        if (email == null || !email.contains("@")) return null;
        return email.substring(email.indexOf("@") + 1).toLowerCase();
    }
}