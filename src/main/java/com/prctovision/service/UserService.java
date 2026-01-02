package com.prctovision.service;

import com.prctovision.model.Student;
import com.prctovision.model.Admin;
import com.prctovision.repository.StudentRepository;
import com.prctovision.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final StudentRepository studentRepo;
    private final AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(StudentRepository studentRepo,
                       AdminRepository adminRepo,
                       PasswordEncoder passwordEncoder) {
        this.studentRepo = studentRepo;
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // Student registration
    public Student registerStudent(String username, String email, String password) {
        if (studentRepo.findByUsername(username).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        if (studentRepo.findByEmail(email).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already taken");

        Student student = new Student();
        student.setUsername(username);
        student.setEmail(email);
        student.setPasswordHash(passwordEncoder.encode(password));
        student.setRole("STUDENT");
        student.setCreatedAt(LocalDateTime.now());
        student.setActive(true);
        student.setProfilePic("default.png"); // default pic

        return studentRepo.save(student);
    }

    // Login and return user info
    public Map<String,Object> loginUserInfo(String usernameOrEmail, String password) {

        // Admin check
        Optional<Admin> adminOpt = adminRepo.findByUsername(usernameOrEmail);
        if (adminOpt.isEmpty()) adminOpt = adminRepo.findByEmail(usernameOrEmail);

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (!passwordEncoder.matches(password, admin.getPasswordHash()))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

            Map<String,Object> map = new HashMap<>();
            map.put("id", admin.getId());
            map.put("username", admin.getUsername());
            map.put("role", "ADMIN");
            map.put("redirectUrl", "/admin/loginsuc.html");
            return map;
        }

        // Student check
        Optional<Student> studentOpt = studentRepo.findByUsername(usernameOrEmail);
        if (studentOpt.isEmpty()) studentOpt = studentRepo.findByEmail(usernameOrEmail);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (!passwordEncoder.matches(password, student.getPasswordHash()))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

            Map<String,Object> map = new HashMap<>();
            map.put("id", student.getId());
            map.put("username", student.getUsername());
            map.put("role", "STUDENT");
            map.put("redirectUrl", "/student/dashboard.html");
            return map;
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
    }
}
