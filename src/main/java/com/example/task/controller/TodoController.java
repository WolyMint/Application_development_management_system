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

import java.security.Principal;
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
    public String showPersonalPage(Model model,
                                   @AuthenticationPrincipal com.example.task.security.CustomUserDetails userDetails) {

        User currentUser = userDetails.getUser(); // 🔥 теперь достаём напрямую

        List<Todo> assignedToMe = todoService.getAssignedToMe(currentUser.getId());
        List<Todo> assignedByMe = todoService.getAssignedByMe(currentUser.getId());

        model.addAttribute("assignedToMe", assignedToMe);
        model.addAttribute("assignedByMe", assignedByMe);
        model.addAttribute("newTodo", new Todo());
        model.addAttribute("allUsers", userService.findAll());

        return "personal_page";
    }

    @PostMapping("/add")
    public String addTodo(@ModelAttribute Todo newTodo,
                          @RequestParam String assignedUserLogin,
                          Principal principal,
                          Model model) {
        User author = userService.findByLogin(principal.getName());
        User assignedUser = userService.findByLogin(assignedUserLogin);

        if (assignedUser == null) {
            model.addAttribute("error", "Пользователь с таким логином не найден.");
            model.addAttribute("allUsers", userService.findAll()); // если нужно
            return "todoList"; // или твоя главная страница
        }

        newTodo.setAuthor(author);
        newTodo.setAssignedUser(assignedUser);
        todoService.save(newTodo);

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
    @PostMapping("/toggle/{id}")
    public String toggleCompleted(@PathVariable Long id, Principal principal) {
        todoService.toggleCompleted(id, principal.getName());
        return "redirect:/";
    }

}
