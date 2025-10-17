package com.unisphere.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class UniversityDTO {

    public static class CreateRequest {
        @NotBlank
        @Size(max = 200)
        private String name;

        @NotBlank
        @Size(max = 100)
        private String domain;

        @Size(max = 500)
        private String description;

        @Size(max = 100)
        private String location;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDomain() { return domain; }
        public void setDomain(String domain) { this.domain = domain; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
    }

    public static class Response {
        private Long id;
        private String name;
        private String domain;
        private String description;
        private String location;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private long studentCount;

        // Constructors
        public Response() {}

        public Response(Long id, String name, String domain, String description, String location,
                        String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
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
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDomain() { return domain; }
        public void setDomain(String domain) { this.domain = domain; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

        public long getStudentCount() { return studentCount; }
        public void setStudentCount(long studentCount) { this.studentCount = studentCount; }
    }

    public static class UserUniversityResponse {
        private Long id;
        private UniversityDTO.Response university;
        private String verificationStatus;
        private LocalDateTime joinedAt;

        // Constructors
        public UserUniversityResponse() {}

        public UserUniversityResponse(Long id, UniversityDTO.Response university, String verificationStatus, LocalDateTime joinedAt) {
            this.id = id;
            this.university = university;
            this.verificationStatus = verificationStatus;
            this.joinedAt = joinedAt;
        }

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public UniversityDTO.Response getUniversity() { return university; }
        public void setUniversity(UniversityDTO.Response university) { this.university = university; }

        public String getVerificationStatus() { return verificationStatus; }
        public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

        public LocalDateTime getJoinedAt() { return joinedAt; }
        public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
    }
}