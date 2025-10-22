package com.unisphere.service;

import com.unisphere.dto.CommunityDTO;
import com.unisphere.entity.*;
import com.unisphere.enums.CommunityStatus;
import com.unisphere.enums.CommunityType;
import com.unisphere.enums.MemberRole;
import com.unisphere.enums.VerificationStatus;
import com.unisphere.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommunityService {

    @Autowired
    private UserUniversityRepository userUniversityRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityMemberRepository communityMemberRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityService universityService;

    // Get all communities for a university
    public List<CommunityDTO.Response> getCommunitiesByUniversity(Long universityId, User currentUser) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new RuntimeException("University not found"));

        List<Community> communities = communityRepository.findByUniversityAndStatus(university, CommunityStatus.ACTIVE);
        return communities.stream()
                .map(community -> convertToDTO(community, currentUser))
                .collect(Collectors.toList());
    }

    // Get community by ID
    public Optional<CommunityDTO.Response> getCommunityById(Long communityId, User currentUser) {
        return communityRepository.findById(communityId)
                .map(community -> convertToDTO(community, currentUser));
    }

    // Create new community
    public CommunityDTO.Response createCommunity(CommunityDTO.CreateRequest request, User currentUser) {
        // Verify user belongs to the university
        University university = universityRepository.findById(request.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found"));

        if (!isUserInUniversity(currentUser, university)) {
            throw new RuntimeException("You must be a verified member of " + university.getName() + " to create communities");
        }

        // Check if community name already exists in this university
        if (communityRepository.findByNameAndUniversity(request.getName(), university).isPresent()) {
            throw new RuntimeException("Community name '" + request.getName() + "' already exists in " + university.getName());
        }

        // Create community
        Community community = new Community();
        community.setName(request.getName().toLowerCase());
        community.setDisplayName(request.getDisplayName());
        community.setDescription(request.getDescription());
        community.setType(request.getType());
        community.setUniversity(university);
        community.setCreatedBy(currentUser);
        community.setMemberCount(1L); // Creator is the first member

        Community savedCommunity = communityRepository.save(community);

        // Add creator as admin
        CommunityMember creatorMember = new CommunityMember(currentUser, savedCommunity, MemberRole.ADMIN);
        communityMemberRepository.save(creatorMember);

        return convertToDTO(savedCommunity, currentUser);
    }

    // Update community
    public Optional<CommunityDTO.Response> updateCommunity(Long communityId, CommunityDTO.UpdateRequest request, User currentUser) {
        return communityRepository.findById(communityId)
                .map(community -> {
                    // Check if user is admin of this community
                    if (!isUserAdmin(community, currentUser)) {
                        throw new RuntimeException("Only community admins can update community details");
                    }

                    if (request.getDisplayName() != null) {
                        community.setDisplayName(request.getDisplayName());
                    }
                    if (request.getDescription() != null) {
                        community.setDescription(request.getDescription());
                    }
                    if (request.getType() != null) {
                        community.setType(request.getType());
                    }
                    if (request.getStatus() != null) {
                        community.setStatus(request.getStatus());
                    }

                    Community updatedCommunity = communityRepository.save(community);
                    return convertToDTO(updatedCommunity, currentUser);
                });
    }

    // Delete community
    public boolean deleteCommunity(Long communityId, User currentUser) {
        return communityRepository.findById(communityId)
                .map(community -> {
                    // Check if user is admin of this community
                    if (!isUserAdmin(community, currentUser)) {
                        throw new RuntimeException("Only community admins can delete the community");
                    }

                    communityRepository.delete(community);
                    return true;
                })
                .orElse(false);
    }

    // Join community
    public CommunityDTO.Response joinCommunity(Long communityId, User currentUser) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        // Check if user belongs to the same university
        if (!isUserInUniversity(currentUser, community.getUniversity())) {
            throw new RuntimeException("You must be a verified member of " + community.getUniversity().getName() + " to join this community");
        }

        // Check if user is already a member
        if (communityMemberRepository.existsByUserAndCommunity(currentUser, community)) {
            throw new RuntimeException("You are already a member of this community");
        }

        // Check community type and apply appropriate joining logic
        if (community.getType() == CommunityType.PRIVATE) {
            throw new RuntimeException("This is a private community. You need an invitation to join.");
        }

        // Add user as member
        CommunityMember member = new CommunityMember(currentUser, community, MemberRole.MEMBER);
        communityMemberRepository.save(member);

        // Update member count
        community.incrementMemberCount();
        communityRepository.save(community);

        return convertToDTO(community, currentUser);
    }

    // Leave community
    public boolean leaveCommunity(Long communityId, User currentUser) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        CommunityMember member = communityMemberRepository.findByUserAndCommunity(currentUser, community)
                .orElseThrow(() -> new RuntimeException("You are not a member of this community"));

        // Check if user is the last admin
        if (member.getRole() == MemberRole.ADMIN) {
            long adminCount = communityMemberRepository.findByCommunityAndRole(community, MemberRole.ADMIN).size();
            if (adminCount <= 1) {
                throw new RuntimeException("Cannot leave community as the last admin. Transfer admin role first or delete the community.");
            }
        }

        communityMemberRepository.delete(member);

        // Update member count
        community.decrementMemberCount();
        communityRepository.save(community);

        return true;
    }

    // Get community members
    public List<CommunityDTO.MemberResponse> getCommunityMembers(Long communityId, User currentUser) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        // Check if user is member of the community
        if (!communityMemberRepository.existsByUserAndCommunity(currentUser, community)) {
            throw new RuntimeException("You must be a member to view community members");
        }

        List<CommunityMember> members = communityMemberRepository.findByCommunity(community);
        return members.stream()
                .map(this::convertToMemberDTO)
                .collect(Collectors.toList());
    }

    // Add to src/main/java/com/unisphere/service/CommunityService.java
    public boolean isUserMemberOfCommunityUniversity(Long userId, Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        return userUniversityRepository.existsByUserIdAndUniversityIdAndVerificationStatus(
                userId, community.getUniversity().getId(), VerificationStatus.VERIFIED);
    }

    // Update member role (admin only)
    public CommunityDTO.MemberResponse updateMemberRole(Long communityId, CommunityDTO.RoleUpdateRequest request, User currentUser) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        // Check if current user is admin
        if (!isUserAdmin(community, currentUser)) {
            throw new RuntimeException("Only community admins can update member roles");
        }

        User targetUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CommunityMember member = communityMemberRepository.findByUserAndCommunity(targetUser, community)
                .orElseThrow(() -> new RuntimeException("User is not a member of this community"));

        member.setRole(request.getRole());
        CommunityMember updatedMember = communityMemberRepository.save(member);

        return convertToMemberDTO(updatedMember);
    }

    // Get user's communities
    public List<CommunityDTO.Response> getUserCommunities(User user) {
        List<CommunityMember> userMemberships = communityMemberRepository.findByUser(user);
        return userMemberships.stream()
                .map(CommunityMember::getCommunity)
                .map(community -> convertToDTO(community, user))
                .collect(Collectors.toList());
    }

    // Search communities
    public List<CommunityDTO.Response> searchCommunities(String query, User currentUser) {
        List<Community> communities = communityRepository.searchByNameOrDisplayName(query);
        return communities.stream()
                .filter(community -> community.getStatus() == CommunityStatus.ACTIVE)
                .map(community -> convertToDTO(community, currentUser))
                .collect(Collectors.toList());
    }

    // Get trending communities
    public List<CommunityDTO.Response> getTrendingCommunities(Pageable pageable, User currentUser) {
        List<Community> communities = communityRepository.findTrendingCommunities(pageable);
        return communities.stream()
                .map(community -> convertToDTO(community, currentUser))
                .collect(Collectors.toList());
    }

    // Helper methods
    private boolean isUserInUniversity(User user, University university) {
        // Get user's university associations
        List<UserUniversity> userUniversities = userUniversityRepository.findByUser(user);

        // Check if user has a verified association with the target university
        return userUniversities.stream()
                .anyMatch(userUniversity ->
                        userUniversity.getUniversity().getId().equals(university.getId()) &&
                                userUniversity.getVerificationStatus() == VerificationStatus.VERIFIED
                );
    }

    private boolean isUserAdmin(Community community, User user) {
        Optional<CommunityMember> member = communityMemberRepository.findByUserAndCommunity(user, community);
        return member.isPresent() && member.get().isAdmin();
    }

    private boolean isUserModerator(Community community, User user) {
        Optional<CommunityMember> member = communityMemberRepository.findByUserAndCommunity(user, community);
        return member.isPresent() && member.get().isModerator();
    }

    // Convert Community to DTO
    public CommunityDTO.Response convertToDTO(Community community, User currentUser) {
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

        // Set user-specific information
        Optional<CommunityMember> userMembership = communityMemberRepository.findByUserAndCommunity(currentUser, community);
        dto.setMember(userMembership.isPresent());
        dto.setUserRole(userMembership.map(CommunityMember::getRole).orElse(null));

        return dto;
    }

    // Convert CommunityMember to Member DTO
    private CommunityDTO.MemberResponse convertToMemberDTO(CommunityMember member) {
        CommunityDTO.MemberResponse dto = new CommunityDTO.MemberResponse();
        dto.setId(member.getUser().getId());
        dto.setUsername(member.getUser().getUsername());
        dto.setEmail(member.getUser().getEmail());
        dto.setFirstName(member.getUser().getFirstName());
        dto.setLastName(member.getUser().getLastName());
        dto.setRole(member.getRole().name());
        dto.setJoinedAt(member.getJoinedAt());
        return dto;
    }

}