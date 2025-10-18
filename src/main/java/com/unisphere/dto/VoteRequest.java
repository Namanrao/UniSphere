// src/main/java/com/unisphere/dto/VoteRequest.java
package com.unisphere.dto;

public class VoteRequest {
    private String voteType; // "UPVOTE" or "DOWNVOTE"
    private Long postId;     // Either postId or commentId should be provided
    private Long commentId;

    // Constructors
    public VoteRequest() {}

    public VoteRequest(String voteType, Long postId) {
        this.voteType = voteType;
        this.postId = postId;
    }

    public VoteRequest(String voteType, Long postId, Long commentId) {
        this.voteType = voteType;
        this.postId = postId;
        this.commentId = commentId;
    }

    // Getters and Setters
    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}