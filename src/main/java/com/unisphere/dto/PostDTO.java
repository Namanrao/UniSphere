// src/main/java/com/unisphere/dto/PostDTO.java
package com.unisphere.dto;

import java.time.LocalDateTime;

public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String status;
    private Integer upvoteCount;
    private Integer downvoteCount;
    private Integer commentCount;
    private Integer voteScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long authorId;
    private String authorName;
    private Long communityId;
    private String communityName;

    // Constructors
    public PostDTO() {}

    public PostDTO(Long id, String title, String content, String status,
                   Integer upvoteCount, Integer downvoteCount, Integer commentCount,
                   LocalDateTime createdAt, LocalDateTime updatedAt,
                   Long authorId, String authorName, Long communityId, String communityName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.upvoteCount = upvoteCount;
        this.downvoteCount = downvoteCount;
        this.commentCount = commentCount;
        this.voteScore = (upvoteCount != null ? upvoteCount : 0) - (downvoteCount != null ? downvoteCount : 0);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authorId = authorId;
        this.authorName = authorName;
        this.communityId = communityId;
        this.communityName = communityName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public Integer getVoteScore() {
        return voteScore;
    }

    public void setVoteScore(Integer voteScore) {
        this.voteScore = voteScore;
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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}