package com.example.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final UuidService uuidService;
    private final TodoRepository todoRepository;

    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    public Todo addTodo(TodoWithoutId todoWithoutId) {
        String id = uuidService.getRandomId();
        Todo todo = new Todo(id, todoWithoutId.description(), todoWithoutId.status());
        return todoRepository.insert(todo);
    }

    public Todo getTodoById(String id) {
        return todoRepository.findById(id).orElseThrow(() ->  new NoSuchElementException());
    }

    public Todo updateTodo(String id,TodoWithoutId todoWithoutId) {
        Todo updateTodo = new Todo(id, todoWithoutId.description(), todoWithoutId.status());
        return todoRepository.save(updateTodo);
    }

    public void deleteTodo(String id) {
        todoRepository.deleteById(id);
    }
}