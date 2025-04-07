package com.example.task.controller;

import com.example.task.model.Todo;
import com.example.task.repository.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TodoController implements CommandLineRunner {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping
    public String index(Model model) {
        List<Todo> allTodos = todoRepository.findAll();
        model.addAttribute("allTodos",allTodos);
        model.addAttribute("newTodo", new Todo());
        return "index";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Todo todoItem){
        todoRepository.save(todoItem);
        //Добавили дело и обратно перешли на страницу
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    //@PathVariable - добавляет переменную как путь ({id} = Long id)
    public String deleteTodoItem(@PathVariable("id") Long id){
        todoRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/removeAll")
    public String removeAll(){
        todoRepository.deleteAll();
        return "redirect:/";
    }

    @PostMapping("/search")
    //@RequestParam - принимает в переменную которую отправили
    public String searchTodoItems(@RequestParam("searchTerm") String searchTerm, Model model){
        List<Todo> allItems = todoRepository.findAll();
        ArrayList<Todo> searchResults = new ArrayList<>();

        for(Todo item: allItems){
            if(item.getTitle().toLowerCase().contains(searchTerm.toLowerCase())){
                searchResults.add(item);
            }
        }
        model.addAttribute("allTodos",searchResults);
        model.addAttribute("newTodo", new Todo());
        model.addAttribute("searchTerm",searchTerm);
        return "index";
    }

    @Override
    public void run(String... args) throws Exception {
        todoRepository.save(new Todo("Item 1"));
        todoRepository.save(new Todo("Item 2"));
    }
}
