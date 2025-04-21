package com.example.task.controller;

import com.example.task.model.User;
import com.example.task.service.UserService;
import jakarta.servlet.http.HttpSession;
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
            userService.create(user); // 🔄 Используем главный метод регистрации с расчётом возраста и проверками
            return "redirect:/login";
        } catch (IllegalStateException e) {
            System.out.println("Ошибка регистрации: " + e.getMessage());
            return "error_page"; // можно передавать сообщение об ошибке в модель, если нужно
        }
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginRequest") User user, HttpSession session) {
        User authenticated = userService.authentication(user.getLogin(), user.getPassword());

        if (authenticated != null) {
            session.setAttribute("user", authenticated);
            return "redirect:/personal_page"; // ✅ редирект на личную страницу
        } else {
            return "error_page";
        }
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") User updatedUser, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Передаём в сервис все обновления
        userService.updateProfile(currentUser.getId(), updatedUser);

        // Обновляем сессию
        session.setAttribute("user", userService.findById(currentUser.getId()));
        return "redirect:/profile";
    }

}
