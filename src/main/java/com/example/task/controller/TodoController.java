package com.example.task.controller;

import com.example.task.model.Todo;
import com.example.task.model.User;
import com.example.task.service.TodoService;
import com.example.task.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TodoController{

    private final TodoService todoService;
    private final UserService userService;

    public TodoController(TodoService todoService, UserService userService) {
        this.todoService = todoService;
        this.userService = userService;
    }
    @GetMapping("/personal_page")
    public String showPersonalPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByLogin(userDetails.getUsername());

        List<Todo> assignedToMe = todoService.getAssignedToMe(currentUser.getId());
        List<Todo> assignedByMe = todoService.getAssignedByMe(currentUser.getId());

        model.addAttribute("assignedToMe", assignedToMe);
        model.addAttribute("assignedByMe", assignedByMe);
        model.addAttribute("newTodo", new Todo());
        model.addAttribute("allUsers", userService.findAll());

        return "personal_page";
    }



    @PostMapping("/add")
    public String addTodo(@ModelAttribute Todo todo,
                          @RequestParam("assignedUserLogin") String assignedUserLogin,
                          @AuthenticationPrincipal UserDetails userDetails) {
        User author = userService.findByLogin(userDetails.getUsername());
        User assignedUser = userService.findByLogin(assignedUserLogin);

        todo.setAuthor(author);
        todo.setAssignedUser(assignedUser);
        todoService.save(todo);

        return "redirect:/personal_page";
    }


    @PostMapping("/delete/{id}")
    //@PathVariable - добавляет переменную как путь ({id} = Long id)
    public String deleteTodoItem(@PathVariable("id") Long id){
        todoService.deleteById(id);
        return "redirect:/personal_page";
    }

    @PostMapping("/removeAll")
    public String removeAll(){
        todoService.deleteAll();
        return "redirect:/personal_page";
    }

    @PostMapping("/search")
    public String search(@RequestParam String searchTerm, Model model) {
        model.addAttribute("allTodos", todoService.search(searchTerm));
        model.addAttribute("newTodo", new Todo());
        model.addAttribute("searchTerm", searchTerm);
        return "personal_page";
    }
}
