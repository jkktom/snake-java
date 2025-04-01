package com.model;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public class Snake {
    private final List<Point> body;
    private final Direction currentDirection;
    private final boolean isAI;
    
    public Snake() {
        this.body = new ArrayList<>();
        this.body.add(new Point(5, 5)); // Starting position
        this.currentDirection = Direction.RIGHT;
        this.isAI = false;
    }
    
    private Snake(List<Point> body, Direction direction, boolean isAI) {
        this.body = new ArrayList<>(body);
        this.currentDirection = direction;
        this.isAI = isAI;
    }
    
    public List<Point> getBody() {
        return new ArrayList<>(body);
    }
    
    public Point getHead() {
        return new Point(body.get(0));
    }
    
    public Direction getDirection() {
        return currentDirection;
    }
    
    public Snake setDirection(Direction direction) {
        if (direction.isOpposite(currentDirection)) {
            return this; // Can't move in opposite direction
        }
        return new Snake(body, direction, isAI);
    }
    
    public boolean isAI() {
        return isAI;
    }
    
    public Snake setAI(boolean ai) {
        return new Snake(body, currentDirection, ai);
    }
    
    public Snake move(Point newHead) {
        List<Point> newBody = new ArrayList<>();
        newBody.add(newHead);
        // Add all segments except the last one (tail)
        for (int i = 0; i < body.size() - 1; i++) {
            newBody.add(new Point(body.get(i)));
        }
        return new Snake(newBody, currentDirection, isAI);
    }
    
    public Snake grow(Point newHead) {
        List<Point> newBody = new ArrayList<>();
        newBody.add(newHead);
        // Add all existing segments
        for (Point segment : body) {
            newBody.add(new Point(segment));
        }
        return new Snake(newBody, currentDirection, isAI);
    }
} 