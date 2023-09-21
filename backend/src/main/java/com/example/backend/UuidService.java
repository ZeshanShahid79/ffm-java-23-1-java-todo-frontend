package com.example.backend;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidService {
    String getRandomId() {
        return UUID.randomUUID().toString();
    }
}
