package com.unisphere.entity;

import com.unisphere.enums.MemberRole;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "community_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "community_id"})
        })
public class CommunityMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.MEMBER;

    @CreationTimestamp
    private LocalDateTime joinedAt;

    // Constructors
    public CommunityMember() {}

    public CommunityMember(User user, Community community, MemberRole role) {
        this.user = user;
        this.community = community;
        this.role = role;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Community getCommunity() { return community; }
    public void setCommunity(Community community) { this.community = community; }

    public MemberRole getRole() { return role; }
    public void setRole(MemberRole role) { this.role = role; }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

    // Helper methods
    public boolean isAdmin() {
        return role == MemberRole.ADMIN;
    }

    public boolean isModerator() {
        return role == MemberRole.MODERATOR || role == MemberRole.ADMIN;
    }

    @Override
    public String toString() {
        return "CommunityMember{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", community=" + (community != null ? community.getName() : "null") +
                ", role=" + role +
                ", joinedAt=" + joinedAt +
                '}';
    }
}