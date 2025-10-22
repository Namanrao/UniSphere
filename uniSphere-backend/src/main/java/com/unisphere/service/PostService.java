// src/main/java/com/unisphere/service/PostService.java
package com.unisphere.service;

import com.unisphere.dto.CreatePostRequest;
import com.unisphere.dto.PostDTO;
import com.unisphere.entity.Community;
import com.unisphere.entity.Post;
import com.unisphere.entity.User;
import com.unisphere.enums.PostStatus;
import com.unisphere.repository.CommunityRepository;
import com.unisphere.repository.PostRepository;
import com.unisphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityService communityService;

    public PostDTO createPost(CreatePostRequest request, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Community community = communityRepository.findById(request.getCommunityId())
                .orElseThrow(() -> new RuntimeException("Community not found: " + request.getCommunityId()));

        // Verify user is member of the community's university
        if (!communityService.isUserMemberOfCommunityUniversity(author.getId(), community.getId())) {
            throw new RuntimeException("User is not a member of this university");
        }

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(author);
        post.setCommunity(community);
        post.setStatus(PostStatus.ACTIVE);
        post.setUpvoteCount(0);
        post.setDownvoteCount(0);
        post.setCommentCount(0);

        Post savedPost = postRepository.save(post);
        return convertToDTO(savedPost);
    }

    public PostDTO getPostById(Long postId) {
        Post post = postRepository.findByIdAndStatus(postId, PostStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));
        return convertToDTO(post);
    }

    public List<PostDTO> getPostsByCommunity(Long communityId, Pageable pageable) {
        Page<Post> posts = postRepository.findByCommunityIdAndStatus(communityId, PostStatus.ACTIVE, pageable);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByUser(Long userId, Pageable pageable) {
        Page<Post> posts = postRepository.findByAuthorIdAndStatus(userId, PostStatus.ACTIVE, pageable);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getTopPostsByCommunity(Long communityId, Pageable pageable) {
        List<Post> posts = postRepository.findTopPostsByCommunity(communityId, pageable);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getHotPostsByCommunity(Long communityId, Pageable pageable) {
        List<Post> posts = postRepository.findHotPostsByCommunity(communityId, pageable);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PostDTO updatePost(Long postId, CreatePostRequest request, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("User is not the author of this post");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        Post updatedPost = postRepository.save(post);
        return convertToDTO(updatedPost);
    }

    public void deletePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("User is not the author of this post");
        }

        post.setStatus(PostStatus.DELETED);
        postRepository.save(post);
    }

    public void incrementCommentCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));
        post.incrementCommentCount();
        postRepository.save(post);
    }

    public void decrementCommentCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));
        post.decrementCommentCount();
        postRepository.save(post);
    }

    public PostDTO convertToDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setStatus(post.getStatus().name());
        dto.setUpvoteCount(post.getUpvoteCount());
        dto.setDownvoteCount(post.getDownvoteCount());
        dto.setCommentCount(post.getCommentCount());
        dto.setVoteScore(post.getVoteScore());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setAuthorId(post.getAuthor().getId());
        dto.setAuthorName(post.getAuthor().getUsername());
        dto.setCommunityId(post.getCommunity().getId());
        dto.setCommunityName(post.getCommunity().getName());

        return dto;
    }

    public long getPostCountByCommunity(Long communityId) {
        return postRepository.countByCommunityIdAndStatus(communityId, PostStatus.ACTIVE);
    }

    public long getPostCountByUser(Long userId) {
        return postRepository.countByAuthorIdAndStatus(userId, PostStatus.ACTIVE);
    }
}