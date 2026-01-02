package com.prctovision.controller;

import com.prctovision.service.ExamInviteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/invite")
public class InviteController {

    private final ExamInviteService inviteService;

    public InviteController(ExamInviteService inviteService) {
        this.inviteService = inviteService;
    }

    /**
     * Send exam invite to external student
     */
    @PostMapping("/send")
    @ResponseBody
    public String sendExamInvite(@RequestParam Long examId, @RequestParam String email) {
        try {
            inviteService.sendExamInvite(examId, email);
            return "Invite sent successfully to " + email;
        } catch (Exception e) {
            return "Failed to send invite: " + e.getMessage();
        }
    }

    /**
     * Temporary login page (Thymeleaf view: invite-setup.html)
     */
    @GetMapping("/setup")
    public String showTempLoginForm(@RequestParam("code") String inviteCode, Model model) {
        model.addAttribute("inviteCode", inviteCode);
        return "invite-setup";
    }

    /**
     * Temporary login validation
     */
    @PostMapping("/setup/login")
    public String loginWithTemp(@RequestParam("inviteCode") String inviteCode,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password,
                                Model model) {
        try {
            boolean valid = inviteService.validateTempLogin(inviteCode, username, password);

            if (!valid) {
                model.addAttribute("error", "Invalid temporary credentials or invite expired");
                model.addAttribute("inviteCode", inviteCode);
                return "invite-setup";
            }

            // ✅ Redirect to static registration page
            return "redirect:/register.html?inviteCode=" + inviteCode;

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("inviteCode", inviteCode);
            return "invite-setup";
        }
    }
}
