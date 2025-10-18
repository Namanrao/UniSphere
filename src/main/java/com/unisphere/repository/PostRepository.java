// src/main/java/com/unisphere/repository/PostRepository.java
package com.unisphere.repository;

import com.unisphere.entity.Post;
import com.unisphere.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCommunityIdAndStatus(Long communityId, PostStatus status, Pageable pageable);

    Page<Post> findByAuthorIdAndStatus(Long authorId, PostStatus status, Pageable pageable);

    List<Post> findByCommunityIdAndStatusOrderByCreatedAtDesc(Long communityId, PostStatus status);

    @Query("SELECT p FROM Post p WHERE p.community.id = :communityId AND p.status = 'ACTIVE' " +
            "ORDER BY (p.upvoteCount - p.downvoteCount) DESC, p.createdAt DESC")
    List<Post> findTopPostsByCommunity(@Param("communityId") Long communityId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.community.id = :communityId AND p.status = 'ACTIVE' " +
            "ORDER BY p.commentCount DESC, p.createdAt DESC")
    List<Post> findHotPostsByCommunity(@Param("communityId") Long communityId, Pageable pageable);

    Optional<Post> findByIdAndStatus(Long id, PostStatus status);

    long countByCommunityIdAndStatus(Long communityId, PostStatus status);

    long countByAuthorIdAndStatus(Long authorId, PostStatus status);

    // FIXED: Added proper parentheses around the OR conditions
    @Query("SELECT p FROM Post p WHERE p.status = :status AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY p.createdAt DESC")
    Page<Post> searchPosts(@Param("query") String query, @Param("status") PostStatus status, Pageable pageable);
}