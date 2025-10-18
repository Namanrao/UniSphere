// src/main/java/com/unisphere/repository/CommentRepository.java
package com.unisphere.repository;

import com.unisphere.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdAndParentCommentIsNullOrderByCreatedAtDesc(Long postId);

    List<Comment> findByParentCommentIdOrderByCreatedAtAsc(Long parentCommentId);

    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    long countByPostId(Long postId);

    long countByAuthorId(Long authorId);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parentComment IS NULL ORDER BY (c.upvoteCount - c.downvoteCount) DESC, c.createdAt DESC")
    List<Comment> findTopLevelCommentsByPostSortedByVotes(@Param("postId") Long postId);

    Optional<Comment> findByIdAndPostId(Long id, Long postId);
}