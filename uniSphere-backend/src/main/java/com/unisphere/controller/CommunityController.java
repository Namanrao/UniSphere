package com.unisphere.controller;

import com.unisphere.dto.CommunityDTO;
import com.unisphere.entity.User;
import com.unisphere.repository.UserRepository;
import com.unisphere.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/communities")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserRepository userRepository;

    // Get all communities for a university (Public)
    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<CommunityDTO.Response>> getUniversityCommunities(@PathVariable Long universityId) {
        User currentUser = getCurrentUser();
        List<CommunityDTO.Response> communities = communityService.getCommunitiesByUniversity(universityId, currentUser);
        return ResponseEntity.ok(communities);
    }

    // Get community by ID (Public)
    @GetMapping("/{communityId}")
    public ResponseEntity<?> getCommunity(@PathVariable Long communityId) {
        User currentUser = getCurrentUser();
        return communityService.getCommunityById(communityId, currentUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create community (Authenticated users from the university)
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createCommunity(@Valid @RequestBody CommunityDTO.CreateRequest request) {
        try {
            User currentUser = getCurrentUser();
            CommunityDTO.Response community = communityService.createCommunity(request, currentUser);
            return ResponseEntity.ok(community);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update community (Community admins only)
    @PutMapping("/{communityId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateCommunity(@PathVariable Long communityId,
                                             @Valid @RequestBody CommunityDTO.UpdateRequest request) {
        try {
            User currentUser = getCurrentUser();
            return communityService.updateCommunity(communityId, request, currentUser)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete community (Community admins only)
    @DeleteMapping("/{communityId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteCommunity(@PathVariable Long communityId) {
        try {
            User currentUser = getCurrentUser();
            boolean deleted = communityService.deleteCommunity(communityId, currentUser);
            if (deleted) {
                return ResponseEntity.ok().body("Community deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Join community (Authenticated users from the university)
    @PostMapping("/{communityId}/join")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> joinCommunity(@PathVariable Long communityId) {
        try {
            User currentUser = getCurrentUser();
            CommunityDTO.Response community = communityService.joinCommunity(communityId, currentUser);
            return ResponseEntity.ok(community);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Leave community (Community members)
    @PostMapping("/{communityId}/leave")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> leaveCommunity(@PathVariable Long communityId) {
        try {
            User currentUser = getCurrentUser();
            boolean left = communityService.leaveCommunity(communityId, currentUser);
            if (left) {
                return ResponseEntity.ok().body("Successfully left the community");
            } else {
                return ResponseEntity.badRequest().body("Failed to leave community");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get community members (Community members only)
    @GetMapping("/{communityId}/members")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCommunityMembers(@PathVariable Long communityId) {
        try {
            User currentUser = getCurrentUser();
            List<CommunityDTO.MemberResponse> members = communityService.getCommunityMembers(communityId, currentUser);
            return ResponseEntity.ok(members);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update member role (Community admins only)
    @PutMapping("/{communityId}/members/role")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateMemberRole(@PathVariable Long communityId,
                                              @Valid @RequestBody CommunityDTO.RoleUpdateRequest request) {
        try {
            User currentUser = getCurrentUser();
            CommunityDTO.MemberResponse member = communityService.updateMemberRole(communityId, request, currentUser);
            return ResponseEntity.ok(member);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get user's communities
    @GetMapping("/my-communities")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CommunityDTO.Response>> getUserCommunities() {
        User currentUser = getCurrentUser();
        List<CommunityDTO.Response> communities = communityService.getUserCommunities(currentUser);
        return ResponseEntity.ok(communities);
    }

    // Search communities (Public)
    @GetMapping("/search")
    public ResponseEntity<List<CommunityDTO.Response>> searchCommunities(@RequestParam String q) {
        User currentUser = getCurrentUser();
        List<CommunityDTO.Response> communities = communityService.searchCommunities(q, currentUser);
        return ResponseEntity.ok(communities);
    }

    // Get trending communities (Public)
    @GetMapping("/trending")
    public ResponseEntity<List<CommunityDTO.Response>> getTrendingCommunities(
            @RequestParam(defaultValue = "10") int limit) {
        User currentUser = getCurrentUser();
        Pageable pageable = PageRequest.of(0, limit);
        List<CommunityDTO.Response> communities = communityService.getTrendingCommunities(pageable, currentUser);
        return ResponseEntity.ok(communities);
    }

    // Helper method to get current authenticated user
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}