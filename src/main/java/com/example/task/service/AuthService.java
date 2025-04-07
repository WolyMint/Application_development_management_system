package com.example.task.service;

import com.example.task.model.User;
import com.example.task.repository.AuthRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    public User registerUser(String login, String password, String email){
        if (login == null || password == null) {
            return null;
        } else {
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            user.setEmail(email);
            return authRepository.save(user);
        }
    }
    public User authentication(String login, String password){
        return authRepository.findByLoginAndPassword(login,password).orElse(null);
    }


}
