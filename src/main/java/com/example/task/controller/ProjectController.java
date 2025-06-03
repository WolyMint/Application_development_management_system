package com.example.task.controller;

import com.example.task.model.Project;
import com.example.task.model.User;
import com.example.task.service.ProjectService;
import com.example.task.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }
    @GetMapping("/personal_page")
    public String showPersonalPage(Model model,
                                   @AuthenticationPrincipal com.example.task.security.CustomUserDetails userDetails) {
        
        User currentUser = userDetails.getUser();

        List<Project> assignedToMe = projectService.getAssignedToMe(currentUser.getId());
        List<Project> assignedByMe = projectService.getAssignedByMe(currentUser.getId());

        model.addAttribute("assignedToMe", assignedToMe);
        model.addAttribute("assignedByMe", assignedByMe);
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
        model.addAttribute("project", new Project());
        return "project_form";
    }
    @PostMapping("project_form")
    public String createProject(@ModelAttribute("project") Project project,
                                    Principal principal) {
        User user = userService.findByLogin(principal.getName());
        project.setOwner(user);
        projectService.save(project);

        return "project_form";
    }

}
