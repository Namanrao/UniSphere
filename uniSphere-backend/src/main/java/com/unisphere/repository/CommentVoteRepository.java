// src/main/java/com/unisphere/repository/CommentVoteRepository.java
package com.unisphere.repository;

import com.unisphere.entity.CommentVote;
import com.unisphere.enums.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {

    Optional<CommentVote> findByUserIdAndCommentId(Long userId, Long commentId);

    long countByCommentIdAndVoteType(Long commentId, VoteType voteType);

    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
}