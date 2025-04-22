package com.example.task.repository;

import com.example.task.model.Todo;
import com.example.task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByAssignedUser(User user);
    List<Todo> findByAuthorIdOrAssignedUserId(Long authorId, Long assignedUserId);
    List<Todo> findByAssignedUserId(Long id);
    List<Todo> findByAuthorId(Long id);
}
