package com.prctovision.service;

import com.prctovision.model.Notification;
import com.prctovision.model.Student;
import com.prctovision.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications(Student student) {
        return notificationRepository.findByStudentOrderByCreatedAtDesc(student);
    }

    public List<Notification> getUnreadNotifications(Student student) {
        return notificationRepository.findByStudentAndReadIsFalseOrderByCreatedAtDesc(student);
    }

    public Notification markAsRead(Long id) {
        Optional<Notification> optional = notificationRepository.findById(id);
        if(optional.isPresent()) {
            Notification notif = optional.get();
            notif.setRead(true);
            return notificationRepository.save(notif);
        }
        return null;
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
