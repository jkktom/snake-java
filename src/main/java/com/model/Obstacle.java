package com.model;

import java.awt.Point;

public record Obstacle(
    Point position,
    boolean isDestructible
) {
    public Obstacle {
        position = new Point(position); // Defensive copy
    }
    
    public Obstacle(Point position) {
        this(position, false); // Default to indestructible obstacle
    }
    
    public Point getPosition() {
        return new Point(position); // Return defensive copy
    }
} 