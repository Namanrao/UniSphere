package com.unisphere.repository;

import com.unisphere.entity.Community;
import com.unisphere.entity.CommunityMember;
import com.unisphere.entity.User;
import com.unisphere.enums.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {

    // Find by user and community
    Optional<CommunityMember> findByUserAndCommunity(User user, Community community);

    // Find all communities for a user
    List<CommunityMember> findByUser(User user);

    // Find all members for a community
    List<CommunityMember> findByCommunity(Community community);

    // Find all members for a community with specific role
    List<CommunityMember> findByCommunityAndRole(Community community, MemberRole role);

    // Check if user is member of community
    boolean existsByUserAndCommunity(User user, Community community);

    // Check if user has specific role in community
    boolean existsByUserAndCommunityAndRole(User user, Community community, MemberRole role);

    // Count members for a community
    long countByCommunity(Community community);

    // Count communities for a user
    long countByUser(User user);

    // Find user's role in community
    @Query("SELECT cm.role FROM CommunityMember cm WHERE cm.user = :user AND cm.community = :community")
    Optional<MemberRole> findRoleByUserAndCommunity(@Param("user") User user, @Param("community") Community community);

    // Find community admins
    @Query("SELECT cm.user FROM CommunityMember cm WHERE cm.community = :community AND cm.role = com.unisphere.enums.MemberRole.ADMIN")
    List<User> findAdminsByCommunity(@Param("community") Community community);

    // Find communities where user has specific role
    List<CommunityMember> findByUserAndRole(User user, MemberRole role);
}