// src/main/java/com/unisphere/service/VoteService.java
package com.unisphere.service;

import com.unisphere.entity.*;
import com.unisphere.enums.VoteType;
import com.unisphere.repository.CommentRepository;
import com.unisphere.repository.CommentVoteRepository;
import com.unisphere.repository.PostRepository;
import com.unisphere.repository.VoteRepository;
import com.unisphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CommentVoteRepository commentVoteRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityService communityService;

    public void votePost(Long postId, VoteType voteType, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));

        // Verify user is member of the community's university
        if (!communityService.isUserMemberOfCommunityUniversity(user.getId(), post.getCommunity().getId())) {
            throw new RuntimeException("User is not a member of this university");
        }

        Vote existingVote = voteRepository.findByUserIdAndPostId(user.getId(), postId).orElse(null);

        if (existingVote != null) {
            if (existingVote.getVoteType() == voteType) {
                // Remove vote if same type clicked again
                removePostVote(existingVote, post);
            } else {
                // Change vote type
                changePostVote(existingVote, post, voteType);
            }
        } else {
            // Create new vote
            createPostVote(user, post, voteType);
        }
    }

    public void voteComment(Long commentId, VoteType voteType, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + commentId));

        // Verify user is member of the community's university
        if (!communityService.isUserMemberOfCommunityUniversity(user.getId(), comment.getPost().getCommunity().getId())) {
            throw new RuntimeException("User is not a member of this university");
        }

        CommentVote existingVote = commentVoteRepository.findByUserIdAndCommentId(user.getId(), commentId).orElse(null);

        if (existingVote != null) {
            if (existingVote.getVoteType() == voteType) {
                // Remove vote if same type clicked again
                removeCommentVote(existingVote, comment);
            } else {
                // Change vote type
                changeCommentVote(existingVote, comment, voteType);
            }
        } else {
            // Create new vote
            createCommentVote(user, comment, voteType);
        }
    }

    private void createPostVote(User user, Post post, VoteType voteType) {
        Vote vote = new Vote(voteType, user, post);
        voteRepository.save(vote);

        if (voteType == VoteType.UPVOTE) {
            post.incrementUpvoteCount();
        } else {
            post.incrementDownvoteCount();
        }
        postRepository.save(post);
    }

    private void changePostVote(Vote existingVote, Post post, VoteType newVoteType) {
        // Remove old vote count
        if (existingVote.getVoteType() == VoteType.UPVOTE) {
            post.decrementUpvoteCount();
        } else {
            post.decrementDownvoteCount();
        }

        // Add new vote count
        if (newVoteType == VoteType.UPVOTE) {
            post.incrementUpvoteCount();
        } else {
            post.incrementDownvoteCount();
        }

        existingVote.setVoteType(newVoteType);
        voteRepository.save(existingVote);
        postRepository.save(post);
    }

    private void removePostVote(Vote existingVote, Post post) {
        if (existingVote.getVoteType() == VoteType.UPVOTE) {
            post.decrementUpvoteCount();
        } else {
            post.decrementDownvoteCount();
        }

        voteRepository.delete(existingVote);
        postRepository.save(post);
    }

    private void createCommentVote(User user, Comment comment, VoteType voteType) {
        CommentVote vote = new CommentVote(voteType, user, comment);
        commentVoteRepository.save(vote);

        if (voteType == VoteType.UPVOTE) {
            comment.incrementUpvoteCount();
        } else {
            comment.incrementDownvoteCount();
        }
        commentRepository.save(comment);
    }

    private void changeCommentVote(CommentVote existingVote, Comment comment, VoteType newVoteType) {
        // Remove old vote count
        if (existingVote.getVoteType() == VoteType.UPVOTE) {
            comment.decrementUpvoteCount();
        } else {
            comment.decrementDownvoteCount();
        }

        // Add new vote count
        if (newVoteType == VoteType.UPVOTE) {
            comment.incrementUpvoteCount();
        } else {
            comment.incrementDownvoteCount();
        }

        existingVote.setVoteType(newVoteType);
        commentVoteRepository.save(existingVote);
        commentRepository.save(comment);
    }

    private void removeCommentVote(CommentVote existingVote, Comment comment) {
        if (existingVote.getVoteType() == VoteType.UPVOTE) {
            comment.decrementUpvoteCount();
        } else {
            comment.decrementDownvoteCount();
        }

        commentVoteRepository.delete(existingVote);
        commentRepository.save(comment);
    }

    public Integer getPostVoteStatus(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Vote vote = voteRepository.findByUserIdAndPostId(user.getId(), postId).orElse(null);

        if (vote == null) return 0;
        return vote.getVoteType() == VoteType.UPVOTE ? 1 : -1;
    }

    public Integer getCommentVoteStatus(Long commentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        CommentVote vote = commentVoteRepository.findByUserIdAndCommentId(user.getId(), commentId).orElse(null);

        if (vote == null) return 0;
        return vote.getVoteType() == VoteType.UPVOTE ? 1 : -1;
    }

}