package com.example.task.service;

import com.example.task.model.Project;
import com.example.task.model.Task;
import com.example.task.model.TaskStatus;
import com.example.task.model.User;
import com.example.task.repository.ProjectRepository;
import com.example.task.repository.TaskRepository;
import com.example.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    // Создание новой подзадачи
    public Task createTask(Long projectId, String title, String description) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = new Task();
        task.setProject(project);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.OPEN);

        return taskRepository.save(task);
    }

    // Разработчик берёт задачу в работу
    public Task assignToDeveloper(Long taskId, String developerLogin) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User developer = userRepository.findByLogin(developerLogin)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setAssignedUser(developer);
        task.setStatus(TaskStatus.IN_PROGRESS);

        return taskRepository.save(task);
    }

    // Завершение задачи
    public Task completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    // Отклонение подзадачи
    public Task rejectTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.REJECTED);

        return taskRepository.save(task);
    }

    // Получить подзадачи по приложению
    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }
    public List<Task> findTasksByDeveloper(User developer) {
        return taskRepository.findByAssignedUser(developer);
    }




}
