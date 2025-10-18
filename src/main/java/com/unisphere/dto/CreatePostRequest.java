// src/main/java/com/unisphere/dto/CreatePostRequest.java
package com.unisphere.dto;

public class CreatePostRequest {
    private String title;
    private String content;
    private Long communityId;

    // Constructors
    public CreatePostRequest() {}

    public CreatePostRequest(String title, String content, Long communityId) {
        this.title = title;
        this.content = content;
        this.communityId = communityId;
    }

    // Getters and Setters
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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
}