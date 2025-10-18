// src/main/java/com/unisphere/entity/Comment.java
package com.unisphere.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "upvote_count")
    private Integer upvoteCount = 0;

    @Column(name = "downvote_count")
    private Integer downvoteCount = 0;

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
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentVote> votes = new ArrayList<>();

    // Constructors
    public Comment() {}

    public Comment(String content, User author, Post post) {
        this.content = content;
        this.author = author;
        this.post = post;
    }

    public Comment(String content, User author, Post post, Comment parentComment) {
        this.content = content;
        this.author = author;
        this.post = post;
        this.parentComment = parentComment;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public List<CommentVote> getVotes() {
        return votes;
    }

    public void setVotes(List<CommentVote> votes) {
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

    public boolean isRootComment() {
        return parentComment == null;
    }

    public Integer getVoteScore() {
        int up = (upvoteCount != null) ? upvoteCount : 0;
        int down = (downvoteCount != null) ? downvoteCount : 0;
        return up - down;
    }
}