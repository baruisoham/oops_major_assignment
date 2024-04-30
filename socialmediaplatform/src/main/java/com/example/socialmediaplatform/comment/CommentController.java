package com.example.socialmediaplatform.comment;

import com.example.socialmediaplatform.post.PostService;
import com.example.socialmediaplatform.user.User;
import com.example.socialmediaplatform.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }



    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest request) {

        // Check if the user exists
        if (userService.findById(request.getUserID()) == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }


        // Check if the associated post exists
        if (postService.findById(request.getPostID()) == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }



        // Create the comment
        commentService.createComment(request.getCommentBody(), request.getPostID(), request.getUserID());

        return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully");
    }

    @PatchMapping("/comment")
    public ResponseEntity<?> editComment(@RequestBody EditCommentRequest request) {
        Long commentID = request.getCommentID(); // Get commentID from the request body
        // Check if the comment exists
        Comment existingComment = commentService.findById(commentID);
        if (existingComment == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Update the comment body
        existingComment.setCommentBody(request.getCommentBody());

        // Save the updated comment
        commentService.updateComment(existingComment);

        return ResponseEntity.ok("Comment edited successfully");
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(@RequestParam Long commentID) {
        // Check if the comment exists
        Comment existingComment = commentService.findById(commentID);
        if (existingComment == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Delete the comment
        commentService.deleteComment(commentID);

        return ResponseEntity.status(HttpStatus.OK).body("Comment deleted");
    }

//    @GetMapping("/comment")
//    public ResponseEntity<?> getCommentDetails(@RequestParam Long commentID) {
//        // Find the comment by commentID
//        Comment comment = commentService.findById(commentID);
//
//        // If comment not found, return error message
//        if (comment == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment does not exist");
//        }
//
//        // Find the user details for the comment creator
//        User commentCreator = userService.findById(comment.getUserID());
//
//        // Create response object
//        CommentResponse commentResponse = new CommentResponse();
//        commentResponse.setCommentID(comment.getCommentID());
//        commentResponse.setCommentBody(comment.getCommentBody());
//        commentResponse.setUserID(commentCreator.getUserID());
//        commentResponse.setName(commentCreator.getName());
//
//        return ResponseEntity.ok(commentResponse);
//    }

    @GetMapping("/comment")
    public ResponseEntity<?> getCommentDetails(@RequestParam Long commentID) {
        // Find the comment by commentID
        Comment comment = commentService.findById(commentID);

        // If comment not found, return error message
        if (comment == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Find the user details for the comment creator
        User commentCreator = userService.findById(comment.getUserID());

        // Create response object
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setCommentID(comment.getCommentID());
        commentResponse.setCommentBody(comment.getCommentBody());

        // Create and set CommentCreator object
        CommentResponse.CommentCreator commentCreatorResponse = new CommentResponse.CommentCreator();
        commentCreatorResponse.setUserID(commentCreator.getUserID());
        commentCreatorResponse.setName(commentCreator.getName());
        commentResponse.setCommentCreator(commentCreatorResponse);

        return ResponseEntity.ok(commentResponse);
    }


    static class EditCommentRequest {
        private Long commentID; // Change the type to Long
        private String commentBody;

        // Getters and setters

        public Long getCommentID() {
            return commentID;
        }

        public void setCommentID(Long commentID) {
            this.commentID = commentID;
        }

        public String getCommentBody() {
            return commentBody;
        }

        public void setCommentBody(String commentBody) {
            this.commentBody = commentBody;
        }
    }

    static class CreateCommentRequest {
        private String commentBody;
        private Long postID;
        private Long userID;

        // Getters and setters

        public String getCommentBody() {
            return commentBody;
        }

        public void setCommentBody(String commentBody) {
            this.commentBody = commentBody;
        }

        public Long getPostID() {
            return postID;
        }

        public void setPostID(Long postID) {
            this.postID = postID;
        }

        public Long getUserID() {
            return userID;
        }

        public void setUserID(Long userID) {
            this.userID = userID;
        }
    }
//    static class CommentResponse {
//        private Long commentID;
//        private String commentBody;
//        private User commentCreator; // Keep User type for commentCreator
//
//        // Getters and setters
//
//        public Long getCommentID() {
//            return commentID;
//        }
//
//        public void setCommentID(Long commentID) {
//            this.commentID = commentID;
//        }
//
//        public String getCommentBody() {
//            return commentBody;
//        }
//
//        public void setCommentBody(String commentBody) {
//            this.commentBody = commentBody;
//        }
//
//        public User getCommentCreator() {
//            return commentCreator;
//        }
//
//        public void setCommentCreator(User commentCreator) {
//            this.commentCreator = commentCreator;
//        }
//
//        // Additional method to create a simplified representation of commentCreator
//        // Additional method to create a simplified representation of commentCreator without email
//        public void setCommentCreatorWithoutEmail(User commentCreator) {
//            // Create a new User object with only required fields
//            this.commentCreator = new User();
//            this.commentCreator.setUserID(commentCreator.getUserID());
//            this.commentCreator.setName(commentCreator.getName());
//            this.commentCreator.setEmail(null); // Set email to null to exclude it from the response
//        }
//
//    }

    static class CommentResponse {
        private Long commentID;
        private String commentBody;
        private CommentCreator commentCreator;

        // Constructors, getters, and setters

        public Long getCommentID() {
            return commentID;
        }

        public void setCommentID(Long commentID) {
            this.commentID = commentID;
        }

        public String getCommentBody() {
            return commentBody;
        }

        public void setCommentBody(String commentBody) {
            this.commentBody = commentBody;
        }

        public CommentCreator getCommentCreator() {
            return commentCreator;
        }

        public void setCommentCreator(CommentCreator commentCreator) {
            this.commentCreator = commentCreator;
        }

        static class CommentCreator {
            private Long userID;
            private String name;

            // Constructors, getters, and setters

            public Long getUserID() {
                return userID;
            }

            public void setUserID(Long userID) {
                this.userID = userID;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }



}
