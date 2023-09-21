package com.example.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping
    List<Todo> getTodos() {
        return todoService.getTodos();
    }

    @GetMapping("{id}")
    Todo getTodoById(@PathVariable String id) {
        return todoService.getTodoById(id);
    }

    @PostMapping
    Todo addTodo(@RequestBody TodoWithoutId todoWithoutId) {
        return todoService.addTodo(todoWithoutId);
    }

    @PutMapping("{id}")
    Todo updateTodo(@PathVariable String id, @RequestBody TodoWithoutId todoWithoutId) {
        return todoService.updateTodo(id, todoWithoutId);
    }

    @DeleteMapping("{id}")
    void deleteTodo(@PathVariable String id) {
        todoService.deleteTodo(id);
    }
}
