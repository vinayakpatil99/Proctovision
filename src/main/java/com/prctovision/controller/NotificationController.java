package com.prctovision.controller;

import com.prctovision.model.Notification;
import com.prctovision.model.Student;
import com.prctovision.repository.StudentRepository;
import com.prctovision.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private StudentRepository studentRepository;

    // ✅ Send notification
    @PostMapping("/send")
    public Notification sendNotification(@RequestParam Long studentId, @RequestParam String message) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        Notification notif = new Notification();
        notif.setStudent(student);
        notif.setMessage(message);
        return notificationService.saveNotification(notif);
    }

    // ✅ Get all notifications for student
    @GetMapping("/student/{studentId}")
    public List<Notification> getNotifications(@PathVariable Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        return notificationService.getAllNotifications(student);
    }

    // ✅ Get unread notifications
    @GetMapping("/student/{studentId}/unread")
    public List<Notification> getUnread(@PathVariable Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        return notificationService.getUnreadNotifications(student);
    }

    // ✅ Mark as read
    @PostMapping("/read/{id}")
    public Notification markRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }

    // ✅ Delete notification
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }
}
