package com.example.task.repository;

import com.example.task.model.Todo;
import com.example.task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser(User user);
}
