package com.model;

public record Frame(
    int width,
    int height
) {
    public static final int DEFAULT_SIZE = 600;
    
    public Frame {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Frame dimensions must be positive");
        }
    }
    
    // Default square frame constructor
    public Frame() {
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }
    
    // Convenience constructor for square frame
    public static Frame square(int size) {
        return new Frame(size, size);
    }
    
    // Get the center point of the frame
    public int getCenterX() {
        return width / 2;
    }
    
    public int getCenterY() {
        return height / 2;
    }
    
    // Check if a position is within the frame bounds
    public boolean contains(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
} 