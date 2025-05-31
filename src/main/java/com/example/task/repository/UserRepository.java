package com.example.task.repository;

import com.example.task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users where email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

}
