package com.prctovision.service;

import com.prctovision.model.Exam;
import com.prctovision.model.ExamInvite;
import com.prctovision.model.Student;
import com.prctovision.repository.ExamInviteRepository;
import com.prctovision.repository.StudentRepository;
import com.prctovision.repository.ExamRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ExamInviteService {

    private final ExamInviteRepository inviteRepo;
    private final StudentRepository studentRepo;
    private final ExamRepository examRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public ExamInviteService(ExamInviteRepository inviteRepo,
                             StudentRepository studentRepo,
                             ExamRepository examRepo,
                             PasswordEncoder passwordEncoder,
                             JavaMailSender mailSender) {
        this.inviteRepo = inviteRepo;
        this.studentRepo = studentRepo;
        this.examRepo = examRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    /**
     * Send exam invite to an external student (temporary credentials)
     */
    public ExamInvite sendExamInvite(Long examId, String email) {
        Exam exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        // Generate temporary credentials
        String tempUsername = email.split("@")[0] + UUID.randomUUID().toString().substring(0, 4);
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        String accessCode = UUID.randomUUID().toString();

        // Create exam invite
        ExamInvite invite = new ExamInvite();
        invite.setExam(exam);
        invite.setEmail(email);
        invite.setTempUsername(tempUsername);
        invite.setTempPasswordHash(passwordEncoder.encode(tempPassword));
        invite.setAccessCode(accessCode);
        invite.setStatus(ExamInvite.Status.PENDING);
        invite.setSentAt(LocalDateTime.now());

        inviteRepo.save(invite);

        // Send branded HTML mail
        sendExamInviteEmail(email, exam.getTitle(), accessCode, tempUsername, tempPassword);

        return invite;
    }

    /**
     * Send ProctoVision.AI Advanced HTML-based Exam Invitation with Instructions
     */
    private void sendExamInviteEmail(String toEmail, String examName, String accessCode,
                                     String tempUsername, String tempPassword) {
        try {
        	String subject = "Congratulations! You’ve Been Shortlisted – Online Assessment Invitation";
            String setupUrl = "http://localhost:9090/invite/setup?code=" + accessCode;

            String htmlContent = """
                <div style="font-family:'Segoe UI',Arial,sans-serif; background:#f4f6fb; padding:30px;">
                  <div style="max-width:700px; margin:auto; background:#fff; border-radius:12px; 
                              box-shadow:0 4px 20px rgba(0,0,0,0.1); overflow:hidden;">
                    
                    <!-- Header -->
                    <div style="background:linear-gradient(90deg,#4e54c8,#8f94fb); padding:25px; text-align:center;">
                      <img src="https://drive.google.com/uc?export=view&id=1EN6xn6iEbw7Kxo6P1PLFIRyPcIGKhjyq" alt="ProctoVision.AI" 
                           style="height:70px; margin-bottom:10px;">
                      <h2 style="color:#fff; margin:10px 0 0;">Welcome to ProctoVision.AI</h2>
                      <p style="color:#e0e0ff; font-size:14px;">AI-Powered Smart Proctoring Platform</p>
                    </div>

                    <!-- Body -->
                    <div style="padding:35px; color:#333;">
                      <p>Dear Candidate,</p>

                      <p>🎉 <b>Congratulations!</b> You have been shortlisted to participate in the online assessment for 
                      <b>%s</b>.</p>

                      <h3 style="margin-top:25px; color:#4e54c8;">Temporary Login Credentials</h3>
                      <table style="width:100%%; border-collapse:collapse; margin-top:10px;">
                        <tr>
                          <td style="padding:10px; border:1px solid #ddd; background:#fafafa;">Username</td>
                          <td style="padding:10px; border:1px solid #ddd;">%s</td>
                        </tr>
                        <tr>
                          <td style="padding:10px; border:1px solid #ddd; background:#fafafa;">Password</td>
                          <td style="padding:10px; border:1px solid #ddd;">%s</td>
                        </tr>
                        <tr>
                          <td style="padding:10px; border:1px solid #ddd; background:#fafafa;">Invite Code</td>
                          <td style="padding:10px; border:1px solid #ddd;">%s</td>
                        </tr>
                      </table>

                      <div style="text-align:center; margin:30px 0;">
                        <a href="%s" style="background:#4e54c8; color:#fff; padding:14px 28px; 
                           border-radius:8px; text-decoration:none; font-weight:bold; font-size:15px;">
                           🚀 Setup Temporary Login
                        </a>
                      </div>

                      <h3 style="color:#4e54c8;">Important Exam Instructions</h3>
                      <ul style="background:#f8f8ff; padding:18px 25px; border-radius:10px; list-style-type:disc; line-height:1.6;">
                        <li>The test will be conducted in <b>full-screen mode only</b>.</li>
                        <li>If you navigate away from the test window, your test will be <b>automatically submitted</b> and marked as completed.</li>
                        <li>Once you move to the <b>next question</b>, you cannot return to the previous one.</li>
                        <li>Once an answer is recorded, it <b>cannot be re-recorded or changed</b>.</li>
                        <li>The test will be automatically submitted when <b>time expires</b>.</li>
                        <li>If you exit the test prematurely, it will still be considered <b>completed</b>.</li>
                        <li>Each section will show its <b>duration, number of questions</b>, and related details before starting.</li>
                      </ul>

                      <p style="margin-top:25px;">Please ensure a stable internet connection, a quiet environment, and allow camera access for AI proctoring.</p>
                      <p style="color:#555;">We wish you the best of luck for your upcoming assessment! 💪</p>
                    </div>

                    <!-- Footer -->
                    <div style="background:#f0f0ff; text-align:center; padding:15px;">
                      <p style="margin:0; color:#555; font-size:14px;">
                        Regards,<br><b>ProctoVision.AI Team</b><br>
                        <a href="https://proctovision.ai" style="color:#4e54c8;">www.proctovision.ai</a>
                      </p>
                    </div>

                  </div>
                </div>
                """.formatted(examName, tempUsername, tempPassword, accessCode, setupUrl);

            // Send Email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send exam invite email: " + e.getMessage(), e);
        }
    }

    /** Get invite by access code */
    public ExamInvite getInviteByCode(String code) {
        return inviteRepo.findByAccessCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid or expired invite code"));
    }

    /** Validate temporary login credentials */
    public boolean validateTempLogin(String inviteCode, String username, String password) {
        ExamInvite invite = getInviteByCode(inviteCode);
        if (invite.getStatus() != ExamInvite.Status.PENDING) return false;
        return invite.getTempUsername().equals(username)
                && passwordEncoder.matches(password, invite.getTempPasswordHash());
    }

    /** Complete invite setup */
    public Student completeInviteSetup(String accessCode, String newUsername, String newPassword) {
        ExamInvite invite = getInviteByCode(accessCode);

        if (invite.getStatus() != ExamInvite.Status.PENDING)
            throw new RuntimeException("Invite already used or expired");

        if (studentRepo.findByUsername(newUsername).isPresent())
            throw new RuntimeException("Username already taken");

        Student student = new Student();
        student.setUsername(newUsername);
        student.setEmail(invite.getEmail());
        student.setPasswordHash(passwordEncoder.encode(newPassword));
        student.setRole("STUDENT");
        student.setActive(true);
        student.setCreatedAt(LocalDateTime.now());

        studentRepo.save(student);

        invite.setStatus(ExamInvite.Status.USED);
        inviteRepo.save(invite);

        return student;
    }
}
