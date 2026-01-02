package com.prctovision.controller;

import com.prctovision.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Student registration
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username,
                                           @RequestParam String email,
                                           @RequestParam String password) {
        try {
            userService.registerStudent(username, email, password);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login with user info
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String usernameOrEmail,
                                   @RequestParam String password) {
        try {
            Map<String,Object> userInfo = userService.loginUserInfo(usernameOrEmail, password);
            return ResponseEntity.ok(userInfo); // return JSON with id, username, role, redirectUrl
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
