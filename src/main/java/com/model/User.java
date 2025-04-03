package com.model;

public record User(int id, String username) {
    public User {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        username = username.trim();
    }
    
    public User(String username) {
        this(0, username);
    }
} 