package com.unisphere.entity;

import com.unisphere.enums.CommunityStatus;
import com.unisphere.enums.CommunityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "communities",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "university_id"})
        })
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String displayName;

    @Size(max = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private CommunityType type = CommunityType.PUBLIC;

    @Enumerated(EnumType.STRING)
    private CommunityStatus status = CommunityStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Long memberCount = 0L;

    // Constructors
    public Community() {}

    public Community(String name, String displayName, String description, University university, User createdBy) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.university = university;
        this.createdBy = createdBy;
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

    public CommunityType getType() { return type; }
    public void setType(CommunityType type) { this.type = type; }

    public CommunityStatus getStatus() { return status; }
    public void setStatus(CommunityStatus status) { this.status = status; }

    public University getUniversity() { return university; }
    public void setUniversity(University university) { this.university = university; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getMemberCount() { return memberCount; }
    public void setMemberCount(Long memberCount) { this.memberCount = memberCount; }

    // Helper methods
    public void incrementMemberCount() { this.memberCount++; }
    public void decrementMemberCount() { this.memberCount = Math.max(0, this.memberCount - 1); }

    @Override
    public String toString() {
        return "Community{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", university=" + (university != null ? university.getName() : "null") +
                ", memberCount=" + memberCount +
                '}';
    }
}