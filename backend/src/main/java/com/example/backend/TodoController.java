package com.example.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    @ResponseStatus(HttpStatus.CREATED)
    Todo addTodo(@RequestBody TodoWithoutId todoWithoutId) {
        return todoService.addTodo(todoWithoutId);
    }

    @PutMapping("{id}")
    Todo updateTodo(@PathVariable String id, @RequestBody TodoWithoutId todoWithoutId) {
        return todoService.updateTodo(id, todoWithoutId);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTodo(@PathVariable String id) {
        todoService.deleteTodo(id);
    }
}
