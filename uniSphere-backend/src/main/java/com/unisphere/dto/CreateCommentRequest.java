// src/main/java/com/unisphere/dto/CreateCommentRequest.java
package com.unisphere.dto;

public class CreateCommentRequest {
    private String content;
    private Long postId;
    private Long parentCommentId; // Optional: for replies

    // Constructors
    public CreateCommentRequest() {}

    public CreateCommentRequest(String content, Long postId) {
        this.content = content;
        this.postId = postId;
    }

    public CreateCommentRequest(String content, Long postId, Long parentCommentId) {
        this.content = content;
        this.postId = postId;
        this.parentCommentId = parentCommentId;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}