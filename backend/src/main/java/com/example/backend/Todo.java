package com.example.backend;

public record Todo(
        String id,
        String description,
        TodoStatus status
) {
}
