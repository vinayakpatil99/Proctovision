package com.prctovision;

import com.prctovision.model.Admin;
import com.prctovision.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class PrctovisionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrctovisionApplication.class, args);
        System.out.println("🚀 ProctoVision backend started successfully...");
        System.out.println("🧠 AI Mock Interview module loaded at: http://localhost:9090/mock_interview.html");
    }

    // Seed default admins at application startup
    @Bean
    CommandLineRunner seedAdmins(AdminRepository adminRepo, PasswordEncoder encoder) {
        return args -> {
            // 1️⃣ Default admin
            if (!adminRepo.findByUsername("admin").isPresent()) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setEmail("admin@prctovision.local");
                admin.setPasswordHash(encoder.encode("admin123")); // hashed password
                admin.setRole("ADMIN");
                admin.setCreatedAt(LocalDateTime.now());
                admin.setActive(true);
                adminRepo.save(admin);
                System.out.println(">> Default admin created: admin / admin123");
            }

            // 2️⃣ BEC College admin
            if (!adminRepo.findByUsername("bec_admin").isPresent()) {
                Admin becAdmin = new Admin();
                becAdmin.setUsername("bec_admin");
                becAdmin.setEmail("bec@prctovision.local");
                becAdmin.setPasswordHash(encoder.encode("bec@123")); // hashed password
                becAdmin.setRole("ADMIN");
                becAdmin.setCreatedAt(LocalDateTime.now());
                becAdmin.setActive(true);
                adminRepo.save(becAdmin);
                System.out.println(">> BEC admin created: bec_admin / bec@123");
            }

            // 3️⃣ Log AI Mock Interview module status
            System.out.println("🤖 AI Mock Interview module initialized — endpoints available:");
            System.out.println("   ➤ POST /api/interview/start");
            System.out.println("   ➤ POST /api/interview/answer");
        };
    }
}
