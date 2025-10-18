// src/main/java/com/unisphere/controller/SearchController.java
package com.unisphere.controller;

import com.unisphere.dto.SearchResultDTO;
import com.unisphere.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/global")
    public ResponseEntity<?> globalSearch(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Long userId = getCurrentUserId();
            Pageable pageable = PageRequest.of(page, size);
            SearchResultDTO results = searchService.globalSearch(q, userId, pageable);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<?> searchPosts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        try {
            Long userId = getCurrentUserId();
            Sort sort = direction.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            return ResponseEntity.ok(searchService.searchPosts(q, userId, pageable));
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/communities")
    public ResponseEntity<?> searchCommunities(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        try {
            Long userId = getCurrentUserId();
            Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
            return ResponseEntity.ok(searchService.searchCommunities(q, userId, pageable));
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> searchUsers(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        try {
            Long userId = getCurrentUserId();
            Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
            return ResponseEntity.ok(searchService.searchUsers(q, userId, pageable));
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/suggestions")
    public ResponseEntity<?> getSearchSuggestions(@RequestParam String q) {
        try {
            Long userId = getCurrentUserId();
            Pageable pageable = PageRequest.of(0, 5);

            SearchResultDTO suggestions = new SearchResultDTO();

            // Get top 3 suggestions for each type
            suggestions.setPosts(searchService.searchPosts(q, userId,
                    PageRequest.of(0, 3, Sort.by("createdAt").descending())));
            suggestions.setCommunities(searchService.searchCommunities(q, userId,
                    PageRequest.of(0, 3, Sort.by("name").ascending())));
            suggestions.setUsers(searchService.searchUsers(q, userId,
                    PageRequest.of(0, 3, Sort.by("username").ascending())));

            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            // You'll need to implement this method to get user ID from username
            return getUserIdFromUsername(username);
        }
        throw new RuntimeException("User not authenticated");
    }

    private Long getUserIdFromUsername(String username) {
        // Placeholder - implement based on your UserService
        return 1L; // Replace with actual implementation
    }
}