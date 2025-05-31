package com.example.task.controller;

import com.example.task.model.Task;
import com.example.task.service.TaskService;
import com.example.task.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("/subtasks")
public class TaskController {

    private final TaskService subtaskService;
    private final UserService userService;

    public TaskController(TaskService subtaskService, UserService userService) {
        this.subtaskService = subtaskService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public String createSubtask(@RequestParam Long projectId,
                                @RequestParam String title,
                                @RequestParam String description) {
        subtaskService.createSubtask(projectId, title, description);
        return "redirect:/project_detail";
    }

    @PostMapping("/{id}/assign")
    public String assignToDeveloper(@PathVariable Long id,
                                    @RequestParam String developerLogin) {
        subtaskService.assignToDeveloper(id, developerLogin);
        return "redirect:/projects";
    }

    @PostMapping("/{id}/complete")
    public String completeSubtask(@PathVariable Long id) {
        subtaskService.completeSubtask(id);
        return "redirect:/projects";
    }

    // 4. Отклонение подзадачи
    @PostMapping("/{id}/reject")
    public ResponseEntity<Task> rejectSubtask(@PathVariable Long id) {
        Task rejected = subtaskService.rejectSubtask(id);
        return ResponseEntity.ok(rejected);
    }

    // 5. Получение подзадач по ID приложения
    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<Task>> getSubtasksByProject(@PathVariable Long projectId) {
        List<Task> subtasks = subtaskService.getSubtasksByProject(projectId);
        return ResponseEntity.ok(subtasks);
    }
    @PostMapping("/subtasks/assign")
    public String assignSubtask(@RequestParam Long subtaskId,
                                @RequestParam String developerLogin) {
        subtaskService.assignToDeveloper(subtaskId, developerLogin);
        return "redirect:/projects";
    }
}

