package com.example.socialmediaplatform.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Add custom query methods if needed
    List<Comment> findByPostID(Long postId);

}
