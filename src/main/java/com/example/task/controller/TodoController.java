package com.example.task.controller;

import com.example.task.model.Todo;
import com.example.task.repository.TodoRepository;
import com.example.task.service.TodoService;
import org.springframework.boot.CommandLineRunner;
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

    @GetMapping("/")
    public String personalPage(Model model) {
        model.addAttribute("allTodos", todoService.getAllTodos());
        model.addAttribute("newTodo", new Todo());
        return "personal_page";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Todo todoItem){
        todoService.addTodo(todoItem);
        //Добавили дело и обратно перешли на страницу
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    //@PathVariable - добавляет переменную как путь ({id} = Long id)
    public String deleteTodoItem(@PathVariable("id") Long id){
        todoService.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/removeAll")
    public String removeAll(){
        todoService.deleteAll();
        return "redirect:/";
    }

    @PostMapping("/search")
    public String search(@RequestParam String searchTerm, Model model) {
        model.addAttribute("allTodos", todoService.search(searchTerm));
        model.addAttribute("newTodo", new Todo());
        model.addAttribute("searchTerm", searchTerm);
        return "personal_page";
    }
}
