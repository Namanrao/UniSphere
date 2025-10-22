// src/main/java/com/unisphere/controller/VoteController.java
package com.unisphere.controller;

import com.unisphere.dto.VoteRequest;
import com.unisphere.enums.VoteType;
import com.unisphere.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping
    public ResponseEntity<?> vote(@RequestBody VoteRequest request) {
        try {
            String username = getCurrentUsername();

            if (request.getPostId() != null) {
                VoteType voteType = VoteType.valueOf(request.getVoteType().toUpperCase());
                voteService.votePost(request.getPostId(), voteType, username);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Vote recorded successfully");
                return ResponseEntity.ok(response);
            } else if (request.getCommentId() != null) {
                VoteType voteType = VoteType.valueOf(request.getVoteType().toUpperCase());
                voteService.voteComment(request.getCommentId(), voteType, username);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Vote recorded successfully");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Either postId or commentId must be provided");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/post/{postId}/status")
    public ResponseEntity<?> getPostVoteStatus(@PathVariable Long postId) {
        try {
            String username = getCurrentUsername();
            Integer voteStatus = voteService.getPostVoteStatus(postId, username);
            Map<String, Integer> response = new HashMap<>();
            response.put("voteStatus", voteStatus);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/comment/{commentId}/status")
    public ResponseEntity<?> getCommentVoteStatus(@PathVariable Long commentId) {
        try {
            String username = getCurrentUsername();
            Integer voteStatus = voteService.getCommentVoteStatus(commentId, username);
            Map<String, Integer> response = new HashMap<>();
            response.put("voteStatus", voteStatus);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new RuntimeException("User not authenticated");
    }
}