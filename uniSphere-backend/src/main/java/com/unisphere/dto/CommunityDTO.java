package com.unisphere.dto;

import com.unisphere.enums.CommunityStatus;
import com.unisphere.enums.CommunityType;
import com.unisphere.enums.MemberRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CommunityDTO {

    public static class CreateRequest {
        @NotBlank
        @Size(min = 3, max = 50)
        private String name;

        @NotBlank
        @Size(max = 100)
        private String displayName;

        @Size(max = 500)
        private String description;

        private CommunityType type = CommunityType.PUBLIC;

        private Long universityId;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public CommunityType getType() { return type; }
        public void setType(CommunityType type) { this.type = type; }

        public Long getUniversityId() { return universityId; }
        public void setUniversityId(Long universityId) { this.universityId = universityId; }
    }

    public static class UpdateRequest {
        @Size(max = 100)
        private String displayName;

        @Size(max = 500)
        private String description;

        private CommunityType type;
        private CommunityStatus status;

        // Getters and setters
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public CommunityType getType() { return type; }
        public void setType(CommunityType type) { this.type = type; }

        public CommunityStatus getStatus() { return status; }
        public void setStatus(CommunityStatus status) { this.status = status; }
    }

    public static class Response {
        private Long id;
        private String name;
        private String displayName;
        private String description;
        private String type;
        private String status;
        private Long universityId;
        private String universityName;
        private String createdBy;
        private Long memberCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private MemberRole userRole; // Current user's role in this community
        private boolean isMember; // Whether current user is a member

        // Constructors
        public Response() {}

        public Response(Long id, String name, String displayName, String description, String type,
                        String status, Long universityId, String universityName, String createdBy,
                        Long memberCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.type = type;
            this.status = status;
            this.universityId = universityId;
            this.universityName = universityName;
            this.createdBy = createdBy;
            this.memberCount = memberCount;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public Long getUniversityId() { return universityId; }
        public void setUniversityId(Long universityId) { this.universityId = universityId; }

        public String getUniversityName() { return universityName; }
        public void setUniversityName(String universityName) { this.universityName = universityName; }

        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

        public Long getMemberCount() { return memberCount; }
        public void setMemberCount(Long memberCount) { this.memberCount = memberCount; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

        public MemberRole getUserRole() { return userRole; }
        public void setUserRole(MemberRole userRole) { this.userRole = userRole; }

        public boolean isMember() { return isMember; }
        public void setMember(boolean member) { isMember = member; }
    }

    public static class MemberResponse {
        private Long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String role;
        private LocalDateTime joinedAt;

        // Constructors
        public MemberResponse() {}

        public MemberResponse(Long id, String username, String email, String firstName,
                              String lastName, String role, LocalDateTime joinedAt) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.role = role;
            this.joinedAt = joinedAt;
        }

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public LocalDateTime getJoinedAt() { return joinedAt; }
        public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
    }

    public static class JoinRequest {
        private Long communityId;

        // Getters and setters
        public Long getCommunityId() { return communityId; }
        public void setCommunityId(Long communityId) { this.communityId = communityId; }
    }

    public static class RoleUpdateRequest {
        @NotBlank
        private String username;

        @NotBlank
        private MemberRole role;

        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public MemberRole getRole() { return role; }
        public void setRole(MemberRole role) { this.role = role; }
    }
}