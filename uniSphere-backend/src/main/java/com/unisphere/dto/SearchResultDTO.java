// src/main/java/com/unisphere/dto/SearchResultDTO.java
package com.unisphere.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchResultDTO {
    private List<PostDTO> posts = new ArrayList<>();
    private List<CommunityDTO.Response> communities = new ArrayList<>();
    private List<UserDTO> users = new ArrayList<>();

    // Constructors
    public SearchResultDTO() {}

    public SearchResultDTO(List<PostDTO> posts, List<CommunityDTO.Response> communities, List<UserDTO> users) {
        this.posts = posts;
        this.communities = communities;
        this.users = users;
    }

    // Getters and Setters
    public List<PostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTO> posts) {
        this.posts = posts;
    }

    public List<CommunityDTO.Response> getCommunities() {
        return communities;
    }

    public void setCommunities(List<CommunityDTO.Response> communities) {
        this.communities = communities;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}