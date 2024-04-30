

package com.example.socialmediaplatform.post;


import com.example.socialmediaplatform.comment.Comment;
import com.example.socialmediaplatform.user.User;
import com.example.socialmediaplatform.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    // Create new post endpoint
    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        // Check if the user exists
        User user = userService.findById(post.getUserID());
        if (user == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);        }

        // Save the post
        postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully");
    }

    // Edit post endpoint
    @PatchMapping("/post")
    public ResponseEntity<?> editPost(@RequestBody EditPostRequest editPostRequest) {
        Long postID = editPostRequest.getPostID();
        String newPostBody = editPostRequest.getPostBody();

        // Check if the post exists
        Post existingPost = postService.findById(postID);
        if (existingPost == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Update the post with new details
        existingPost.setPostBody(newPostBody);
        // Set other properties as needed

        // Save the updated post
        postService.editPost(existingPost);

        return ResponseEntity.ok("Post edited successfully");
    }

    // Delete post endpoint
    @DeleteMapping("/post")
    public ResponseEntity<?> deletePost(@RequestParam Long postID) {
        // Check if the post exists
        Post existingPost = postService.findById(postID);
        if (existingPost == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Delete the post
        postService.deletePost(postID);
        return ResponseEntity.ok("Post deleted");
    }

    // Retrieve an existing post endpoint
//    @GetMapping
//    public ResponseEntity<?> getPostDetails(@RequestParam Long postID) {
//        // Find the post by postID
//        Post post = postService.findById(postID);
//
//        // If post not found, return error message
//        if (post == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post does not exist");
//        }
//
//        // Retrieve comments for the post
//        List<Comment> comments = postService.getCommentsForPost(postID);
//
//        // Create response object
//        PostDetailsResponse postDetails = new PostDetailsResponse();
//        postDetails.setPostID(post.getPostID());
//        postDetails.setPostBody(post.getPostBody());
//        postDetails.setComments(comments);
//
//        return ResponseEntity.ok(postDetails);
//    }

    @GetMapping("/post")
    public ResponseEntity<?> getPostDetails(@RequestParam Long postID) {
        // Find the post by postID
        Post post = postService.findById(postID);

        // If post not found, return error message
        if (post == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Retrieve comments for the post
        List<Comment> comments = postService.getCommentsForPost(postID);

        // Map Comment objects to CommentDetails objects
        // Map Comment objects to CommentDetails objects
        List<CommentDetails> commentDetailsList = comments.stream()
                .map(comment -> {
                    CommentDetails commentDetails = new CommentDetails();
                    commentDetails.setCommentID(comment.getCommentID()); // Use getCommentID instead of getCommentID
                    commentDetails.setCommentBody(comment.getCommentBody());

                    // Create and set the CommentCreator
                    CommentCreator commentCreator = new CommentCreator();
                    commentCreator.setUserID(comment.getUserID());
                    // Set the name (you need to fetch the name from the user service)
                    // For now, let's assume you have a method getUserByID in UserService
                    User user = userService.findById(comment.getUserID());
                    commentCreator.setName(user != null ? user.getName() : "Unknown");
                    commentDetails.setCommentCreator(commentCreator);

                    return commentDetails;
                })
                .collect(Collectors.toList());


        // Create response object
        PostDetailsResponse postDetails = new PostDetailsResponse();
        postDetails.setPostID(post.getPostID());
        postDetails.setPostBody(post.getPostBody());
//        postDetails.setDate(new Date());

        // Get the current date
        Date currentDate = new Date();

// Convert the current date to a string in the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);

// Set the formatted date using setDate method
        postDetails.setDate(formattedDate);

        postDetails.setComments(commentDetailsList);

        return ResponseEntity.ok(postDetails);
    }


    //userFeed is in Postcontroller, here
    @GetMapping("/")
    public ResponseEntity<List<PostDetailsResponse>> getUserFeed() {
        // Fetch all posts from the database
        List<Post> allPosts = postService.getAllPosts();

        // Sort posts in reverse chronological order based on postID
        allPosts.sort(Comparator.comparing(Post::getPostID).reversed());

        // Map the list of posts to the desired response format
        List<PostDetailsResponse> userFeed = allPosts.stream()
                .map(this::mapPostToPostDetailsResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userFeed);
    }

    // Method to map Post entity to PostDetailsResponse DTO
    private PostDetailsResponse mapPostToPostDetailsResponse(Post post) {
        PostDetailsResponse postDetails = new PostDetailsResponse();
        postDetails.setPostID(post.getPostID());
        postDetails.setPostBody(post.getPostBody());
//        postDetails.setDate(new Date());

        // Get the current date
        Date currentDate = new Date();

// Convert the current date to a string in the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);

// Set the formatted date using setDate method
        postDetails.setDate(formattedDate);


        // Retrieve comments for the post
        List<Comment> comments = postService.getCommentsForPost(post.getPostID());

        // Map Comment objects to CommentDetails objects
        List<CommentDetails> commentDetailsList = comments.stream()
                .map(comment -> {
                    CommentDetails commentDetails = new CommentDetails();
                    commentDetails.setCommentID(comment.getCommentID());
                    commentDetails.setCommentBody(comment.getCommentBody());

                    // Create and set the CommentCreator
                    CommentCreator commentCreator = new CommentCreator();
                    commentCreator.setUserID(comment.getUserID());
                    // Set the name (you need to fetch the name from the user service)
                    // For now, let's assume you have a method getUserByID in UserService
                    User user = userService.findById(comment.getUserID());
                    commentCreator.setName(user != null ? user.getName() : "Unknown");
                    commentDetails.setCommentCreator(commentCreator);

                    return commentDetails;
                })
                .collect(Collectors.toList());

        postDetails.setComments(commentDetailsList);

        return postDetails;
    }

    // Inner class to represent the request body for editing a post
    static class EditPostRequest {
        private Long postID;
        private String postBody;

        // Getters and setters
        public Long getPostID() {
            return postID;
        }

        public void setPostID(Long postID) {
            this.postID = postID;
        }

        public String getPostBody() {
            return postBody;
        }

        public void setPostBody(String postBody) {
            this.postBody = postBody;
        }
    }

    // Inner class to represent the response body for post details
    static class PostDetailsResponse {
        private Long postID;
        private String postBody;
        private String date;
        private List<CommentDetails> comments;

        Date currentDate = new Date();
        // Getters and setters
        public Long getPostID() {
            return postID;
        }

        public void setPostID(Long postID) {
            this.postID = postID;
        }

        public String getPostBody() {
            return postBody;
        }

        public void setPostBody(String postBody) {
            this.postBody = postBody;
        }

        public List<CommentDetails> getComments() {
            return comments;
        }

        public void setComments(List<CommentDetails> comments) {
            this.comments = comments;
        }

        public String getDate() {
            return date;
        }

//        public void setDate(String dateString) {
//            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//            try {
//                Date parsedDate = inputDateFormat.parse(dateString);
//                String formattedDateStr = outputDateFormat.format(parsedDate);
//                this.date = outputDateFormat.parse(formattedDateStr);
//            } catch (ParseException e) {
//                e.printStackTrace(); // Or log the error
//            }
//        }

        public void setDate(String date) {
            this.date = date;
        }

    }

    // Inner class to represent the comment details
    static class CommentDetails {
        private Long commentID;
        private String commentBody;
        private CommentCreator commentCreator;

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

        public CommentCreator getCommentCreator() {
            return commentCreator;
        }

        public void setCommentCreator(CommentCreator commentCreator) {
            this.commentCreator = commentCreator;
        }
    }

    // Inner class to represent the comment creator
    static class CommentCreator {
        private Long userID;
        private String name;

        // Getters and setters
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
















