package com.example.task.controller;

import com.example.task.model.User;
import com.example.task.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @DeleteMapping(path= "{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PutMapping(path = "{id}")
    public void update(@PathVariable Long id,
                       @RequestParam(required = false) String email,
                       @RequestParam(required = false) String name
    ) {
        userService.update(id, email, name);
    }
}
