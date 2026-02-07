package com.mose.manageemployees.controller;

import com.mose.manageemployees.model.User;
import com.mose.manageemployees.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String viewProfile(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(authentication.getName())
                .orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("role", user.getRole().name());

        // Return role-specific profile template
        switch (user.getRole()) {
            case ADMIN:
                return "admin-profile";
            case STUDENT:
                return "student-profile";
            default:
                return "staff-profile";
        }
    }

    @PostMapping("/profile/update")
    public String updateProfile(Authentication authentication,
                                @RequestParam String email,
                                @RequestParam(required = false) String newPassword,
                                @RequestParam(required = false) String confirmPassword,
                                RedirectAttributes redirectAttributes) {
        if (authentication == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(authentication.getName())
                .orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        // Update email
        user.setEmail(email);

        // Update password if provided
        if (newPassword != null && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
                return "redirect:/profile";
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            redirectAttributes.addFlashAttribute("success", "Profile and password updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        }

        userRepository.save(user);
        return "redirect:/profile";
    }
}
