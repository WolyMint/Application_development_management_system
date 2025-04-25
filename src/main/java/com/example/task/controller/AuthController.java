package com.example.task.controller;

import com.example.task.model.User;
import com.example.task.security.CustomUserDetails;
import com.example.task.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("registerRequest", new User());
        return "register_page";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new User());
        return "login_page";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerRequest") User user){
        System.out.println("Register request: " + user);

        try {
            userService.create(user);
            return "redirect:/login";
        } catch (IllegalStateException e) {
            System.out.println("Ошибка регистрации: " + e.getMessage());
            return "error_page";
        }
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", userDetails.getUser());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentUser = userDetails.getUser();
        userService.updateProfile(currentUser.getId(), updatedUser);
        return "redirect:/profile";
    }
}
