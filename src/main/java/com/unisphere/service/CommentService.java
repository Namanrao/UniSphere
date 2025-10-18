// src/main/java/com/unisphere/service/CommentService.java
package com.unisphere.service;

import com.unisphere.dto.CommentDTO;
import com.unisphere.dto.CreateCommentRequest;
import com.unisphere.entity.Comment;
import com.unisphere.entity.Post;
import com.unisphere.entity.User;
import com.unisphere.repository.CommentRepository;
import com.unisphere.repository.PostRepository;
import com.unisphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommunityService communityService;

    public CommentDTO createComment(CreateCommentRequest request, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found: " + request.getPostId()));

        // Verify user is member of the community's university
        if (!communityService.isUserMemberOfCommunityUniversity(author.getId(), post.getCommunity().getId())) {
            throw new RuntimeException("User is not a member of this university");
        }

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setUpvoteCount(0);
        comment.setDownvoteCount(0);

        // Handle reply to another comment
        if (request.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found: " + request.getParentCommentId()));
            comment.setParentComment(parentComment);
        }

        Comment savedComment = commentRepository.save(comment);

        // Increment post comment count
        postService.incrementCommentCount(post.getId());

        return convertToDTO(savedComment);
    }

    public List<CommentDTO> getCommentsByPost(Long postId) {
        List<Comment> rootComments = commentRepository.findByPostIdAndParentCommentIsNullOrderByCreatedAtDesc(postId);
        return rootComments.stream()
                .map(this::convertToDTOWithReplies)
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getRepliesByComment(Long commentId) {
        List<Comment> replies = commentRepository.findByParentCommentIdOrderByCreatedAtAsc(commentId);
        return replies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CommentDTO updateComment(Long commentId, String content, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + commentId));

        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("User is not the author of this comment");
        }

        comment.setContent(content);
        Comment updatedComment = commentRepository.save(comment);
        return convertToDTO(updatedComment);
    }

    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + commentId));

        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("User is not the author of this comment");
        }

        // Decrement post comment count
        postService.decrementCommentCount(comment.getPost().getId());

        commentRepository.delete(comment);
    }

    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUpvoteCount(comment.getUpvoteCount());
        dto.setDownvoteCount(comment.getDownvoteCount());
        dto.setVoteScore(comment.getVoteScore());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setAuthorId(comment.getAuthor().getId());
        dto.setAuthorName(comment.getAuthor().getUsername());
        dto.setPostId(comment.getPost().getId());
        dto.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null);

        return dto;
    }

    private CommentDTO convertToDTOWithReplies(Comment comment) {
        CommentDTO dto = convertToDTO(comment);

        // Recursively add replies
        List<CommentDTO> replies = comment.getReplies().stream()
                .map(this::convertToDTOWithReplies)
                .collect(Collectors.toList());
        dto.setReplies(replies);

        return dto;
    }

    public long getCommentCountByPost(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    public long getCommentCountByUser(Long userId) {
        return commentRepository.countByAuthorId(userId);
    }

}