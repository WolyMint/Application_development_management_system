package com.example.task.service;

import com.example.task.model.User;
import com.example.task.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        validateUniqueUser(user.getLogin(), user.getEmail());

        user.setAge(Period.between(user.getBirth(), LocalDate.now()).getYears());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Transactional
    public void updateProfile(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));

        if (!user.getEmail().equals(updatedUser.getEmail())) {
            Optional<User> emailCheck = userRepository.findByEmail(updatedUser.getEmail());
            if (emailCheck.isPresent() && !emailCheck.get().getId().equals(id)) {
                throw new IllegalStateException("Email уже используется другим пользователем");
            }
        }
        user.setName(updatedUser.getName());
        user.setLogin(updatedUser.getLogin());
        user.setEmail(updatedUser.getEmail());
        user.setBirth(updatedUser.getBirth());
        user.setAge(Period.between(updatedUser.getBirth(), LocalDate.now()).getYears());
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + login));
    }

    private void validateUniqueUser(String login, String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Пользователь с таким email уже существует.");
        }
        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalStateException("Пользователь с таким логином уже существует.");
        }
    }
}
