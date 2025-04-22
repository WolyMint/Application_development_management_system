package com.example.task.controller;

import com.example.task.model.Todo;
import com.example.task.model.User;
import com.example.task.service.TodoService;
import com.example.task.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TodoController{

    private final TodoService todoService;
    private final UserService userService;

    public TodoController(TodoService todoService, UserService userService) {
        this.todoService = todoService;
        this.userService = userService;
    }

    @GetMapping("/personal_page")
    public String personal_page(@SessionAttribute(value = "user", required = false) User user, Model model) {
        if (user == null) {
            return "redirect:/register_page";
        }
        model.addAttribute("allTodos", todoService.getTodosByUser(user));
        model.addAttribute("newTodo", new Todo());
        model.addAttribute("allUsers", userService.findAll()); // 🔥 добавим список всех пользователей
        return "personal_page";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Todo todoItem,
                      @RequestParam("assignedUserId") Long assignedUserId,
                      @SessionAttribute("user") User user){

        todoItem.setUser(user); // кто создал задачу
        User assignedUser = userService.findById(assignedUserId); // кому назначили
        todoItem.setAssignedUser(assignedUser); // сохраняем
        todoService.addTodo(todoItem);
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
