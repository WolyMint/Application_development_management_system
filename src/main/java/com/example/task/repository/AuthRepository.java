package com.example.task.repository;

import com.example.task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginAndPassword(String login, String password);

}
