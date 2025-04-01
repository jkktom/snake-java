package com.model;

import java.awt.Point;

public class Food {
    private Point position;
    private int value;
    
    public Food(Point position) {
        this.position = position;
        this.value = 1; // Default food value
    }
    
    public Point getPosition() {
        return new Point(position);
    }
    
    public int getValue() {
        return value;
    }
    
    public void setPosition(Point position) {
        this.position = new Point(position);
    }
} 