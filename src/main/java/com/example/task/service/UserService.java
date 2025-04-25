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

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalStateException("Пользователя с id: " + id + " не существует.");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, String email, String name) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Пользователя с id: " + id + " не существует."));

        if (email != null && !email.equals(user.getEmail())) {
            Optional<User> foundByEmail = userRepository.findByEmail(email);
            if (foundByEmail.isPresent()) {
                throw new IllegalStateException("Пользователь с таким email уже существует.");
            }
            user.setEmail(email);
        }
        if (name != null && !name.equals(user.getName())) {
            user.setName(name);
        }
    }

    public User registerUser(String name, String login, String password, String email, LocalDate birth) {
        validateUniqueUser(login, email);

        User user = new User();
        user.setName(name);
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setBirth(birth);
        user.setAge(Period.between(birth, LocalDate.now()).getYears());

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
