package com.example.task.repository;

import com.example.task.model.Project;
import com.example.task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByAssignedUserId(Long id);
    List<Project> findByTitleContainingIgnoreCase(String title);
    List<Project> findByOwnerId(Long id);
}
