// src/main/java/com/unisphere/service/SearchService.java
package com.unisphere.service;

import com.unisphere.dto.SearchResultDTO;
import com.unisphere.dto.CommunityDTO;
import com.unisphere.dto.PostDTO;
import com.unisphere.dto.UserDTO;
import com.unisphere.entity.Community;
import com.unisphere.entity.Post;
import com.unisphere.entity.User;
import com.unisphere.enums.PostStatus;
import com.unisphere.repository.CommunityRepository;
import com.unisphere.repository.PostRepository;
import com.unisphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommunityService communityService;

    // Search posts across all communities (within user's universities)
    public List<PostDTO> searchPosts(String query, Long userId, Pageable pageable) {
        Page<Post> posts = postRepository.searchPosts(query, PostStatus.ACTIVE, pageable);

        // Filter posts to only include communities from user's universities
        return posts.stream()
                .filter(post -> isUserMemberOfPostUniversity(userId, post))
                .map(postService::convertToDTO)
                .collect(Collectors.toList());
    }

    // Search communities within user's universities
    public List<CommunityDTO.Response> searchCommunities(String query, Long userId, Pageable pageable) {
        Page<Community> communities = communityRepository.searchCommunities(query, pageable);

        // Get current user for membership info
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Filter communities to only include user's universities
        return communities.stream()
                .filter(community -> isUserMemberOfCommunityUniversity(userId, community.getId()))
                .map(community -> convertCommunityToResponseDTO(community, currentUser))
                .collect(Collectors.toList());
    }

    // Search users (only within same universities for privacy)
    public List<UserDTO> searchUsers(String query, Long userId, Pageable pageable) {
        Page<User> users = userRepository.searchUsers(query, pageable);

        // Filter users to only include same university members
        return users.stream()
                .filter(user -> isUserInSameUniversity(userId, user.getId()))
                .map(this::convertUserToDTO)
                .collect(Collectors.toList());
    }

    // Global search across posts, communities, and users
    public SearchResultDTO globalSearch(String query, Long userId, Pageable pageable) {
        SearchResultDTO result = new SearchResultDTO();

        // Search posts
        List<PostDTO> posts = searchPosts(query, userId,
                PageRequest.of(0, 5, Sort.by("createdAt").descending()));
        result.setPosts(posts);

        // Search communities
        List<CommunityDTO.Response> communities = searchCommunities(query, userId,
                PageRequest.of(0, 5, Sort.by("name").ascending()));
        result.setCommunities(communities);

        // Search users
        List<UserDTO> users = searchUsers(query, userId,
                PageRequest.of(0, 5, Sort.by("username").ascending()));
        result.setUsers(users);

        return result;
    }

    // Helper method to check if user is member of post's university
    private boolean isUserMemberOfPostUniversity(Long userId, Post post) {
        return communityService.isUserMemberOfCommunityUniversity(userId, post.getCommunity().getId());
    }

    // Helper method to check if user is member of community's university
    private boolean isUserMemberOfCommunityUniversity(Long userId, Long communityId) {
        return communityService.isUserMemberOfCommunityUniversity(userId, communityId);
    }

    // Helper method to check if two users are in the same university
    private boolean isUserInSameUniversity(Long userId1, Long userId2) {
        // Implementation depends on your university verification system
        // For now, return true (you'll need to implement this properly)
        return true;
    }

    // Convert Community to CommunityDTO.Response (using your existing structure)
    private CommunityDTO.Response convertCommunityToResponseDTO(Community community, User currentUser) {
        CommunityDTO.Response dto = new CommunityDTO.Response();
        dto.setId(community.getId());
        dto.setName(community.getName());
        dto.setDisplayName(community.getDisplayName());
        dto.setDescription(community.getDescription());
        dto.setType(community.getType().name());
        dto.setStatus(community.getStatus().name());
        dto.setUniversityId(community.getUniversity().getId());
        dto.setUniversityName(community.getUniversity().getName());
        dto.setCreatedBy(community.getCreatedBy() != null ? community.getCreatedBy().getUsername() : "Unknown");
        dto.setMemberCount(community.getMemberCount());
        dto.setCreatedAt(community.getCreatedAt());
        dto.setUpdatedAt(community.getUpdatedAt());

        // Set user-specific information - you can use communityService methods if available
        // For search results, we might not need detailed membership info
        dto.setMember(false); // Default to false, or implement logic to check membership
        dto.setUserRole(null); // Default to null for search results

        return dto;
    }

    // Convert User to UserDTO
    private UserDTO convertUserToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setStatus(user.getStatus().name());
        return dto;
    }
}