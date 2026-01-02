package com.prctovision.controller;

import com.prctovision.dto.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*") // allow frontend requests
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping
    public String sendMail(@RequestBody ContactRequest request) {
        try {
            // Create email message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("vinayak9731463492@gmail.com"); // your email to receive messages
            message.setSubject("New Contact Form Submission");
            message.setText(
                "Name: " + request.getName() + "\n" +
                "Email: " + request.getEmail() + "\n" +
                "Message:\n" + request.getMessage()
            );

            // Send the email
            mailSender.send(message);
            return "Message sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending message: " + e.getMessage();
        }
    }
}
