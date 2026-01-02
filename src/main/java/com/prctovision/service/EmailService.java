package com.prctovision.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a formal exam/test invitation email to external student
     *
     * @param toEmail      Recipient email
     * @param candidateName Candidate name
     * @param examName     Exam title
     * @param accessCode   Unique invite code
     * @param tempUsername Temporary username
     * @param tempPassword Temporary password
     * @param testDate     Scheduled test date
     * @param reportingTime Reporting time
     * @param venue        Exam venue
     */
    public void sendExamInvite(String toEmail,
                               String candidateName,
                               String examName,
                               String accessCode,
                               String tempUsername,
                               String tempPassword,
                               String testDate,
                               String reportingTime,
                               String venue) {

        String subject = "Congratulations! You Have Been Shortlisted for " + examName;

        // Setup link for first-time login
        String setupUrl = "http://localhost:9090/invite/setup?code=" + accessCode;

        StringBuilder text = new StringBuilder();
        text.append("Dear ").append(candidateName != null ? candidateName : "Candidate").append(",\n\n")
            .append("Congratulations! You have been shortlisted for the ").append(examName).append(".\n\n")
            .append("Please find your exam details below:\n")
            .append("Test Date: ").append(testDate).append("\n")
            .append("Reporting Time: ").append(reportingTime).append("\n")
            .append("Venue: ").append(venue).append("\n\n")
            .append("Temporary login credentials for accessing the test portal:\n")
            .append("Username: ").append(tempUsername).append("\n")
            .append("Password: ").append(tempPassword).append("\n\n")
            .append("Click the link below to set up your account and access the test:\n")
            .append(setupUrl).append("\n\n")
            .append("Please complete the test within the scheduled window.\n\n")
            .append("We wish you all the best!\n\n")
            .append("Regards,\n")
            .append("Talent Acquisition Team");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text.toString());

        mailSender.send(message);
    }
}
