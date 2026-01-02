package com.prctovision.service;

import com.prctovision.model.Student;
import com.prctovision.model.Exam;
import com.prctovision.model.Result;
import com.prctovision.model.Notification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StudentService {

    // Get student by ID
    Student getStudentById(Long id);

    // Update student profile + picture
    Student updateStudent(Long id, String username, String email, MultipartFile profilePic) throws IOException;

    // Get exams assigned to student
    List<Exam> getUpcomingExams(Long studentId);

    // Get student's past results
    List<Result> getPastResults(Long studentId);

    // Get all notifications
    List<Notification> getNotifications(Long studentId);

    // Get unread notifications
    List<Notification> getUnreadNotifications(Long studentId);

    // Complete invite setup for external student
    Student completeInviteSetup(String accessCode, String username, String password, String confirmPassword);
}
