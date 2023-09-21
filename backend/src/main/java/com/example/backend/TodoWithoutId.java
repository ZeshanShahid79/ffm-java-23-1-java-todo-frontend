package com.example.backend;

public record TodoWithoutId(
        String description,
        TodoStatus status
) {
}
