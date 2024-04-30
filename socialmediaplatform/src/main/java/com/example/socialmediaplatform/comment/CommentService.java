package com.example.socialmediaplatform.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // Method to create a new comment
    public void createComment(String commentBody, Long postID, Long userID) {
        Comment comment = new Comment();
        comment.setCommentBody(commentBody);
        comment.setPostID(postID);
        comment.setUserID(userID);
        // Save the comment
        commentRepository.save(comment);
    }

    // Method to find a comment by ID
    public Comment findById(Long commentID) {
        return commentRepository.findById(commentID).orElse(null);
    }

    // Method to update an existing comment
    public void updateComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentID) {
        commentRepository.deleteById(commentID);
    }

//    public void updateComment(Comment comment) {
//        commentRepository.save(comment);
//    }
}
