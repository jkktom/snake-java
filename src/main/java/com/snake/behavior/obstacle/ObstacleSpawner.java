package com.snake.behavior.obstacle;

import com.model.Obstacle;
import com.model.Snake;
import com.model.Food;
import com.model.Frame;
import java.awt.Point;
import java.util.List;
import java.util.Random;

public class ObstacleSpawner {
    private static final int TILE_SIZE = 20;
    
    private final Random random;
    private final Frame frame;

    public ObstacleSpawner(Frame frame) {
        this.frame = frame;
        this.random = new Random();
    }

    public Frame getFrame() {
        return frame;
    }

    public Obstacle spawnObstacle(Snake snake, List<Obstacle> obstacles, Food food1, Food food2) {
        Point position;
        do {
            position = new Point(
                random.nextInt(frame.width() / TILE_SIZE),
                random.nextInt(frame.height() / TILE_SIZE)
            );
        } while (isInvalidPosition(position, snake, obstacles, food1, food2));

        return new Obstacle(position);
    }

    private boolean isInvalidPosition(Point position, Snake snake, List<Obstacle> obstacles, Food food1, Food food2) {
        // Check if position overlaps with snake
        if (snake.getBody().contains(position)) {
            return true;
        }

        // Check if position overlaps with existing obstacles
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPosition().equals(position)) {
                return true;
            }
        }

        // Check if position overlaps with food
        if (food1.getPosition().equals(position) || food2.getPosition().equals(position)) {
            return true;
        }

        return false;
    }
} 