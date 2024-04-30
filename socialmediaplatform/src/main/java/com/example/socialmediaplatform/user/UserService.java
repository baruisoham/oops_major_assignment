package com.example.socialmediaplatform.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Method to save a user
    public void saveUser(User user) {
        userRepository.save(user);
    }

    // Method to find a user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Method to find a user by ID
    public User findById(Long userID) {
        return userRepository.findById(userID).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
