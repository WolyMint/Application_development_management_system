package com.example.task.controller;

import com.example.task.model.User;
import com.example.task.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String register(@ModelAttribute User user){
        System.out.println("register request: "+user);
        User registeredUser = userService.registerUser(user.getLogin(), user.getName(), user.getPassword(), user.getEmail(), user.getBirth());
        return registeredUser == null ? "error_page" : "redirect:/login     ";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session, Model model) {
        User authenticated = userService.authentication(user.getLogin(), user.getPassword());
        if (authenticated != null) {
            session.setAttribute("user", authenticated);
            return "redirect:/personal_page"; // редирект на главную страницу
        } else {
            return "error_page"; // страница ошибки
        }
    }
}
