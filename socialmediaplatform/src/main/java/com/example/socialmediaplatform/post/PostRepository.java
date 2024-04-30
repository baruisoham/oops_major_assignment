package com.example.socialmediaplatform.post;

import com.example.socialmediaplatform.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Define custom methods if needed
}
