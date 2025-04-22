package com.example.task.service;

import com.example.task.model.User;
import com.example.task.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    final private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        Optional<User> optionalUserByEmail = userRepository.findByEmail(user.getEmail());
        if (optionalUserByEmail.isPresent()) {
            throw new IllegalStateException("Пользователь с таким email уже существует.");
        }

        Optional<User> optionalUserByLogin = userRepository.findByLogin(user.getLogin());
        if (optionalUserByLogin.isPresent()) {
            throw new IllegalStateException("Пользователь с таким логином уже существует.");
        }

        user.setAge(Period.between(user.getBirth(), LocalDate.now()).getYears());
        return userRepository.save(user);
    }


    public void delete(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()) {
            throw new IllegalStateException("Пользователя с id: "+ id +" не существует.");
        }
        userRepository.deleteById(id);
    }
    @Transactional
    public void update(Long id, String email, String name) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("Пользователя с id: "+ id +" не существует.");
        }
        User user = optionalUser.get();

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

        //userRepository.save(user);
    }
    public User registerUser(String name, String login, String password, String email, LocalDate birth){
        if (login == null || password == null) return null;
        User user = new User();
        user.setName(name);
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setBirth(birth);
        return userRepository.save(user);
    }

    public User authentication(String login, String password){
        return userRepository.findByLoginAndPassword(login, password).orElse(null);
    }
    @Transactional
    public void updateProfile(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));

        // Проверка email на уникальность
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

        // Автоматический перерасчет возраста
        user.setAge(Period.between(updatedUser.getBirth(), LocalDate.now()).getYears());
    }
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
    }

}
