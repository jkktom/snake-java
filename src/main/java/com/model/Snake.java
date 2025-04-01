package com.model;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public class Snake {
    private List<Point> body;
    private Direction currentDirection;
    private boolean isAI;
    
    public Snake() {
        this.body = new ArrayList<>();
        this.body.add(new Point(5, 5)); // Starting position
        this.currentDirection = Direction.RIGHT;
        this.isAI = false;
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
    
    public void setDirection(Direction direction) {
        this.currentDirection = direction;
    }
    
    public boolean isAI() {
        return isAI;
    }
    
    public void setAI(boolean ai) {
        isAI = ai;
    }
} 