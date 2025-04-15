package com.example.task.controller;

import com.example.task.model.Todo;
import com.example.task.model.User;
import com.example.task.service.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TodoController{

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/personal_page")
    public String personal_page(@SessionAttribute(value = "user", required = false) User user, Model model) {
        if (user == null) {
            return "redirect:/register_page"; // или куда-то на стартовую
        }
        model.addAttribute("allTodos", todoService.getTodosByUser(user));
        model.addAttribute("newTodo", new Todo());
        return "personal_page";
    }


    @PostMapping("/add")
    public String add(@ModelAttribute Todo todoItem, @SessionAttribute("user") User user){
        todoItem.setUser(user);
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
