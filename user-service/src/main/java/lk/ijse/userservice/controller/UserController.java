package lk.ijse.userservice.controller;

import lk.ijse.userservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Map<String, User> userMap = new HashMap<>();

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        userMap.put(userId, user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        User user = userMap.get(userId);
        return user != null ?
                new ResponseEntity<>(user, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get all users
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(new ArrayList<>(userMap.values()), HttpStatus.OK);
    }

    // Update a user by ID
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User updatedUser) {
        if (!userMap.containsKey(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        updatedUser.setUserId(userId);
        userMap.put(userId, updatedUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Delete a user by ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        if (!userMap.containsKey(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userMap.remove(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
