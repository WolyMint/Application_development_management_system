package com.example.task.controller;

import com.example.task.model.*;
import com.example.task.service.ProjectService;
import com.example.task.service.TaskService;
import com.example.task.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final TaskService taskService;

    public ProjectController(ProjectService projectService, UserService userService, TaskService taskService) {
        this.projectService = projectService;
        this.userService = userService;
        this.taskService = taskService;
    }
    @GetMapping("/personal_page")
    public String showPersonalPage(Model model,
                                   @AuthenticationPrincipal com.example.task.security.CustomUserDetails userDetails) {

        User currentUser = userDetails.getUser();

        // Проекты, в которых пользователь — исполнитель или заказчик
        List<Project> assignedToMe = projectService.getAssignedToMe(currentUser.getId());
        List<Project> assignedByMe = projectService.getAssignedByMe(currentUser.getId());

        // Задачи, которые пользователь взял
        List<Task> tasksTakenByUser = taskService.findTasksByDeveloper(currentUser);
        Set<Project> projectsWithTakenTasks = tasksTakenByUser.stream()
                .map(Task::getProject)
                .collect(Collectors.toSet());

        model.addAttribute("assignedToMe", assignedToMe);
        model.addAttribute("assignedByMe", assignedByMe);
        model.addAttribute("myProjects", projectService.findByOwner(currentUser));
        model.addAttribute("takenProjects", projectsWithTakenTasks);
        model.addAttribute("tasksTaken", tasksTakenByUser);
        model.addAttribute("project", new Project());
        model.addAttribute("allUsers", userService.findAll());

        return "personal_page";
    }


    @PostMapping("/delete/{id}")
    //@PathVariable - добавляет переменную как путь ({id} = Long id)
    public String deleteProjectItem(@PathVariable("id") Long id){
        projectService.deleteById(id);
        return "redirect:/personal_page";
    }

    @PostMapping("/removeAll")
    public String removeAll(){
        projectService.deleteAll();
        return "redirect:/personal_page";
    }

    @PostMapping("/search")
    public String search(@RequestParam String searchTerm, Model model, Principal principal) {
        User currentUser = userService.findByLogin(principal.getName());

        model.addAttribute("assignedToMe", projectService.getAssignedToMe(currentUser.getId()));
        model.addAttribute("assignedByMe", projectService.getAssignedByMe(currentUser.getId()));
        model.addAttribute("newProject", new Project());
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("allProjects", projectService.search(searchTerm));

        return "personal_page";
    }

    @PostMapping("/toggle/{id}") // отмечает, что выполнено задание или нет
    public String toggleCompleted(@PathVariable Long id, Principal principal) {
        projectService.toggleCompleted(id, principal.getName());
        return "redirect:/personal_page";
    }
    @GetMapping("project_form")
    public String showProjectForm(Model model) {
        Project project = new Project();
        project.setStatus(ProjectStatus.OPEN); // Устанавливаем статус по умолчанию
        model.addAttribute("project", project);

        return "project_form";
    }

    @GetMapping("/projects")
    public String showProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "projects"; // имя шаблона
    }
    @PostMapping("/project_form")
    public String createProject(@ModelAttribute Project project, Principal principal) {
        User creator = userService.findByLogin(principal.getName());
        project.setOwner(creator);
        projectService.save(project);
        return "redirect:/projects";
    }
    @GetMapping("/project_detail/{id}")
    public String showProjectDetail(@PathVariable Long id, Model model) {
        Project project = projectService.findById(id);
        model.addAttribute("project", project);
        model.addAttribute("task", new Task()); // для формы
        return "project_detail";
    }



}
