package com.prctovision.controller;

import com.prctovision.model.DemoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "*") // allow frontend requests
public class DemoRequestController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/request")
    public String requestDemo(@RequestBody DemoRequest demoRequest) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("vinupatil1728@gmail.com"); // replace with your Gmail
            message.setSubject("📩 New Demo Request from " + demoRequest.getName());
            message.setText(
                    "Name: " + demoRequest.getName() + "\n" +
                    "Email: " + demoRequest.getEmail() + "\n" +
                    "Phone: " + demoRequest.getPhone() + "\n" +
                    "College: " + demoRequest.getCollege() + "\n" +
                    "Message: " + demoRequest.getMessage()
            );

            mailSender.send(message);
            return "✅ Demo request submitted successfully!";
        } catch (Exception e) {
            return "❌ Error while sending email: " + e.getMessage();
        }
    }
}
