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

    public List<Todo> getTodosAssignedTo(User user) {
        return todoRepository.findByAssignedUser(user);
    }

    public void markAsCompleted(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Задача не найдена"));
        todo.setCompleted(true);
        todoRepository.save(todo);
    }
    public List<Todo> getTodosForUser(Long userId) {
        return todoRepository.findByAuthorIdOrAssignedUserId(userId, userId);
    }
    public void save(Todo todo) {
        todoRepository.save(todo);
    }
    public List<Todo> getAssignedToMe(Long userId) {
        return todoRepository.findByAssignedUserId(userId);
    }

    public List<Todo> getAssignedByMe(Long userId) {
        return todoRepository.findByAuthorId(userId);
    }

    public void toggleCompleted(Long id, String username) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        if (todo.getAuthor().getLogin().equals(username)) {
            todo.setCompleted(!todo.isCompleted());
            todoRepository.save(todo);
        }
    }
}
