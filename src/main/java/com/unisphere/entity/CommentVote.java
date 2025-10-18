// src/main/java/com/unisphere/entity/CommentVote.java
package com.unisphere.entity;

import com.unisphere.enums.VoteType;
import jakarta.persistence.*;

@Entity
@Table(name = "comment_votes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "comment_id"})
})
public class CommentVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType voteType;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    // Constructors
    public CommentVote() {}

    public CommentVote(VoteType voteType, User user, Comment comment) {
        this.voteType = voteType;
        this.user = user;
        this.comment = comment;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}