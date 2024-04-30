//package com.example.socialmediaplatform.post;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PostService {
//
//    private final PostRepository postRepository;
//
//    @Autowired
//    public PostService(PostRepository postRepository) {
//        this.postRepository = postRepository;
//    }
//
//    // Method to create a new post
//    public void createPost(Post post) {
//        postRepository.save(post);
//    }
//
//    // Method to find a post by ID
//    public Post findById(Long postID) {
//        return postRepository.findById(postID).orElse(null);
//    }
//
//    // Method to update an existing post
//    public void editPost(Post post) {
//        // Find the existing post
//        Post existingPost = postRepository.findById(post.getPostID()).orElse(null);
//
//        // Check if the post exists
//        if (existingPost != null) {
//            // Update the post body
//            existingPost.setPostBody(post.getPostBody());
//            // Save the updated post
//            postRepository.save(existingPost);
//        }
//        // You may add error handling if the post doesn't exist
//    }
//
//    public void deletePost(Long postID) {
//        postRepository.deleteById(postID);
//    }
//}

package com.example.socialmediaplatform.post;

import com.example.socialmediaplatform.comment.Comment;
import com.example.socialmediaplatform.comment.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    // Method to create a new post
    public void createPost(Post post) {
        postRepository.save(post);
    }

    // Method to find a post by ID
    public Post findById(Long postID) {
        return postRepository.findById(postID).orElse(null);
    }

    // Method to update an existing post
    public void editPost(Post post) {
        // Find the existing post
        Post existingPost = postRepository.findById(post.getPostID()).orElse(null);

        // Check if the post exists
        if (existingPost != null) {
            // Update the post body
            existingPost.setPostBody(post.getPostBody());
            // Save the updated post
            postRepository.save(existingPost);
        }
        // You may add error handling if the post doesn't exist
    }

    // Method to delete a post
    public void deletePost(Long postID) {
        postRepository.deleteById(postID);
    }

    // Method to get comments for a post
    public List<Comment> getCommentsForPost(Long postID) {
        return commentRepository.findByPostID(postID);
    }

    public List<Post> getAllPosts() {
        // Implement the logic to fetch all posts from your database using the PostRepository
        return postRepository.findAll();
    }
}
