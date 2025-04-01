package com.model;

public enum Tile {
    EMPTY,
    SNAKE_BODY,
    SNAKE_HEAD,
    FOOD,
    OBSTACLE;
    
    public boolean isCollision() {
        return this == SNAKE_BODY || this == OBSTACLE;
    }
    
    public boolean isFood() {
        return this == FOOD;
    }
} 