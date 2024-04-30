//package com.example.socialmediaplatform.user;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/")
//public class UserController {
//
//    private final UserService userService;
//
//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    // Signup endpoint
//    @PostMapping("/signup")
//    public ResponseEntity<String> signUp(@RequestBody User user) {
//        // Check if user with the provided email already exists
//        if (userService.findByEmail(user.getEmail()) != null) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden, Account already exists");
//        }
//
//        // Save the new user
//        userService.saveUser(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Account Creation Successful");
//    }
//
//    // Login endpoint
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User user) {
//        User existingUser = userService.findByEmail(user.getEmail());
//
//        // Check if user exists
//        if (existingUser == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
//        }
//
//        // Check if credentials are correct
//        if (!existingUser.getPassword().equals(user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username/Password Incorrect");
//        }
//
//        return ResponseEntity.ok("Login Successful");
//    }
//
//    // User detail endpoint
//    @GetMapping("/user")
//    public ResponseEntity<?> getUserDetails(@RequestParam Long userID) {
//        // Find the user by userID
//        User user = userService.findById(userID);
//
//        // If user not found, return error message
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
//        }
//
//        // If user found, return user details
//        return ResponseEntity.ok(user);
//    }
//
//
//}

package com.example.socialmediaplatform.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        // Check if user with the provided email already exists
        if (userService.findByEmail(user.getEmail()) != null) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden, Account already exists");
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Forbidden, Account already exists");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Save the new user
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account Creation Successful");
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());

        // Check if user exists
        if (existingUser == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Check if credentials are correct
        if (!existingUser.getPassword().equals(user.getPassword())) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "Username/Password Incorrect");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok("Login Successful");
    }

    // User detail endpoint
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@RequestParam Long userID) {
        // Find the user by userID
        User user = userService.findById(userID);

        // If user not found, return error message
        if (user == null) {
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("Error", "User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Map the User object to a UserDTO and return the DTO
        UserDTO userDTO = mapUserToUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    private UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setUserID(user.getUserID());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    // Endpoint to retrieve details about all existing users
    @GetMapping("/users")
    public ResponseEntity<List<UserListDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserListDTO> userListDTOs = users.stream()
                .map(this::mapUserToUserListDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userListDTOs);
    }

    private UserListDTO mapUserToUserListDTO(User user) {
        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setUserID(user.getUserID());
        userListDTO.setName(user.getName());
        userListDTO.setEmail(user.getEmail());
        return userListDTO;
    }

}
