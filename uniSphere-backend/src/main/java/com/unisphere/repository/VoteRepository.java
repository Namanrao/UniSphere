// src/main/java/com/unisphere/repository/VoteRepository.java
package com.unisphere.repository;

import com.unisphere.entity.Vote;
import com.unisphere.enums.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByUserIdAndPostId(Long userId, Long postId);

    long countByPostIdAndVoteType(Long postId, VoteType voteType);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.post.id = :postId AND v.voteType = 'UPVOTE'")
    long countUpvotesByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.post.id = :postId AND v.voteType = 'DOWNVOTE'")
    long countDownvotesByPostId(@Param("postId") Long postId);

    boolean existsByUserIdAndPostId(Long userId, Long postId);
}