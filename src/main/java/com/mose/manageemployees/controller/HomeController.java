package com.mose.manageemployees.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login/admin")
    public String adminLogin() {
        return "admin-login";
    }

    @GetMapping("/login/staff")
    public String staffLogin() {
        return "staff-login";
    }

    @GetMapping("/login/student")
    public String studentLogin() {
        return "student-login";
    }

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", authentication.getName());

        // Redirect to role-specific dashboard
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
            return "student-dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "admin-dashboard";
        } else {
            return "staff-dashboard";
        }
    }
}
