package com.example.task.service;

import com.example.task.model.Todo;
import com.example.task.model.User;
import com.example.task.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAllTodos(){
        return todoRepository.findAll();
    }

    public void addTodo(Todo todo) {
        todoRepository.save(todo);
    }

    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }

    public void deleteAll() {
        todoRepository.deleteAll();
    }

    public List<Todo> search(String searchTerm) {
        return todoRepository.findAll().stream()
                .filter(todo -> todo.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();
    }

    public List<Todo> getTodosByUser(User user) {
        return todoRepository.findByUser(user);
    }
}
