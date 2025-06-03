package com.example.task.service;

import com.example.task.model.Project;
import com.example.task.model.User;
import com.example.task.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.userService = userService;
        this.projectRepository = projectRepository;
    }

    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }

    public void deleteAll() {
        projectRepository.deleteAll();
    }

    public List<Project> search(String searchTerm) {
        return projectRepository.findByTitleContainingIgnoreCase(searchTerm);
    }

    public void save(Project project) {
        projectRepository.save(project);
    }
    public List<Project> getAssignedToMe(Long userId) {
        return projectRepository.findByAssignedUserId(userId);
    }

    public List<Project> getAssignedByMe(Long userId) {
        return projectRepository.findByOwnerId(userId);
    }

    public void toggleCompleted(Long id, String username) {
        Project project = projectRepository.findById(id).orElseThrow();
        if (project.getOwner().getLogin().equals(username)) {
            project.setCompleted(!project.isCompleted());
            projectRepository.save(project);
        }
    }
    public void saveProject(Project project, Principal principal) {
        User user = userService.findByLogin(principal.getName());
        project.setOwner(user);
        projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow();
    }

    public Object findByOwner(User currentUser) {
        return projectRepository.findByOwnerId(currentUser.getId());
    }
}
