package com.example.task.controller;

import com.example.task.model.Task;
import com.example.task.service.TaskService;
import com.example.task.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String showCreateTaskForm(@RequestParam Long projectId, Model model) {
        Task task = new Task();
        model.addAttribute("task", task);
        model.addAttribute("projectId", projectId);
        return "task_form"; // создадим этот шаблон
    }
    @PostMapping("/create")
    public String createTask(@RequestParam Long projectId,
                             @RequestParam String title,
                             @RequestParam String description) {
        taskService.createTask(projectId, title, description);
        return "redirect:/project_detail/" + projectId;
    }

    @PostMapping("/{id}/assign")
    public String assignToDeveloper(@PathVariable Long id, Principal principal) {
        String developerLogin = principal.getName();
        taskService.assignToDeveloper(id, developerLogin);
        return "redirect:/personal_page";
    }



    @PostMapping("/{id}/complete")
    public String completeTask(@PathVariable Long id) {
        taskService.completeTask(id);
        return "redirect:/projects";
    }

    // 4. Отклонение подзадачи
    @PostMapping("/{id}/reject")
    public ResponseEntity<Task> rejectTask(@PathVariable Long id) {
        Task rejected = taskService.rejectTask(id);
        return ResponseEntity.ok(rejected);
    }

    // 5. Получение подзадач по ID приложения
    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProject(@PathVariable Long projectId) {
        List<Task> tasks = taskService.getTasksByProject(projectId);
        return ResponseEntity.ok(tasks);
    }
}

