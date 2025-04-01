package com.model;

public enum Direction {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);
    
    private final int dx;
    private final int dy;
    
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    public int getDx() { return dx; }
    public int getDy() { return dy; }
    
    public Direction opposite() {
        return switch(this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
    
    public boolean isOpposite(Direction other) {
        return this.opposite() == other;
    }
} 