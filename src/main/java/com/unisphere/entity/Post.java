// src/main/java/com/unisphere/entity/Post.java
package com.unisphere.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.unisphere.enums.PostStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.ACTIVE;

    @Column(name = "upvote_count")
    private Integer upvoteCount = 0;

    @Column(name = "downvote_count")
    private Integer downvoteCount = 0;

    @Column(name = "comment_count")
    private Integer commentCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    // Constructors
    public Post() {}

    public Post(String title, String content, User author, Community community) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.community = community;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public Integer getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(Integer upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public Integer getDownvoteCount() {
        return downvoteCount;
    }

    public void setDownvoteCount(Integer downvoteCount) {
        this.downvoteCount = downvoteCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    // Helper methods
    public void incrementUpvoteCount() {
        this.upvoteCount = (this.upvoteCount == null) ? 1 : this.upvoteCount + 1;
    }

    public void incrementDownvoteCount() {
        this.downvoteCount = (this.downvoteCount == null) ? 1 : this.downvoteCount + 1;
    }

    public void decrementUpvoteCount() {
        this.upvoteCount = (this.upvoteCount == null || this.upvoteCount <= 0) ? 0 : this.upvoteCount - 1;
    }

    public void decrementDownvoteCount() {
        this.downvoteCount = (this.downvoteCount == null || this.downvoteCount <= 0) ? 0 : this.downvoteCount - 1;
    }

    public void incrementCommentCount() {
        this.commentCount = (this.commentCount == null) ? 1 : this.commentCount + 1;
    }

    public void decrementCommentCount() {
        this.commentCount = (this.commentCount == null || this.commentCount <= 0) ? 0 : this.commentCount - 1;
    }

    public Integer getVoteScore() {
        int up = (upvoteCount != null) ? upvoteCount : 0;
        int down = (downvoteCount != null) ? downvoteCount : 0;
        return up - down;
    }
}