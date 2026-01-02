package com.prctovision.service;

import com.prctovision.model.Student;
import com.prctovision.model.Exam;
import com.prctovision.model.Result;
import com.prctovision.model.Notification;
import com.prctovision.model.ExamInvite;
import com.prctovision.repository.StudentRepository;
import com.prctovision.repository.ExamAssignmentRepository;
import com.prctovision.repository.ResultRepository;
import com.prctovision.repository.NotificationRepository;
import com.prctovision.repository.ExamInviteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepo;
    private final ExamAssignmentRepository assignmentRepo;
    private final ResultRepository resultRepo;
    private final NotificationRepository notificationRepo;
    private final ExamInviteRepository inviteRepo;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepo,
                              ExamAssignmentRepository assignmentRepo,
                              ResultRepository resultRepo,
                              NotificationRepository notificationRepo,
                              ExamInviteRepository inviteRepo,
                              PasswordEncoder passwordEncoder) {
        this.studentRepo = studentRepo;
        this.assignmentRepo = assignmentRepo;
        this.resultRepo = resultRepo;
        this.notificationRepo = notificationRepo;
        this.inviteRepo = inviteRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ----------------------------
    // Existing Methods
    // ----------------------------

    @Override
    public Student getStudentById(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Override
    public Student updateStudent(Long id, String username, String email, MultipartFile profilePic) throws IOException {
        Student student = getStudentById(id);
        student.setUsername(username);
        student.setEmail(email);

        if (profilePic != null && !profilePic.isEmpty()) {
            String originalFilename = profilePic.getOriginalFilename();

            if (originalFilename == null || !originalFilename.matches(".*\\.(png|jpg|jpeg)$")) {
                throw new RuntimeException("Only PNG, JPG, JPEG files are allowed");
            }

            String uploadDirPath = System.getProperty("user.dir") + "/uploads/";
            File uploadDir = new File(uploadDirPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String newFileName = id + "_" + System.currentTimeMillis() + "_" + originalFilename;
            File dest = new File(uploadDir, newFileName);
            profilePic.transferTo(dest);

            student.setProfilePic(newFileName);
        }

        return studentRepo.save(student);
    }

    @Override
    public List<Exam> getUpcomingExams(Long studentId) {
        return assignmentRepo.findUpcomingExamsByStudentId(studentId);
    }

    @Override
    public List<Result> getPastResults(Long studentId) {
        return resultRepo.findByStudentId(studentId);
    }

    @Override
    public List<Notification> getNotifications(Long studentId) {
        Student student = getStudentById(studentId);
        return notificationRepo.findByStudentOrderByCreatedAtDesc(student);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long studentId) {
        Student student = getStudentById(studentId);
        return notificationRepo.findByStudentAndReadIsFalseOrderByCreatedAtDesc(student);
    }

    // ----------------------------
    // Invite Setup with Confirm Password
    // ----------------------------
    @Override
    @Transactional
    public Student completeInviteSetup(String accessCode, String username, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        ExamInvite invite = inviteRepo.findByAccessCode(accessCode)
                .orElseThrow(() -> new RuntimeException("Invalid or expired invite code"));

        if (invite.getUsed() != null && invite.getUsed()) {
            throw new RuntimeException("Invite already used");
        }

        // Create permanent student
        Student student = new Student();
        student.setUsername(username);
        student.setEmail(invite.getEmail());
        student.setPasswordHash(passwordEncoder.encode(password));
        student.setRole("STUDENT");
        student.setActive(true);
        student.setCreatedAt(LocalDateTime.now());

        studentRepo.save(student);

        // Mark invite as used
        invite.setUsed(true);
        inviteRepo.save(invite);

        return student;
    }
}
