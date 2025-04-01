package com.snake.behavior.food;

import com.model.Food;
import com.model.Snake;
import com.model.Obstacle;
import com.model.Frame;
import java.awt.Point;
import java.util.List;
import java.util.Random;

public class FoodPlacer {
    private static final int TILE_SIZE = 20;
    private final Random random;
    private final Frame frame;

    public FoodPlacer(Frame frame) {
        this.frame = frame;
        this.random = new Random();
    }

    public Food placeFood(Snake snake, List<Obstacle> obstacles, Food otherFood) {
        Point position;
        do {
            position = new Point(
                random.nextInt(frame.width() / TILE_SIZE),
                random.nextInt(frame.height() / TILE_SIZE)
            );
        } while (isInvalidPosition(position, snake, obstacles, otherFood));

        return new Food(position);
    }

    private boolean isInvalidPosition(Point position, Snake snake, List<Obstacle> obstacles, Food otherFood) {
        // Check if position overlaps with snake
        if (snake.getBody().contains(position)) {
            return true;
        }

        // Check if position overlaps with obstacles
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPosition().equals(position)) {
                return true;
            }
        }

        // Check if position overlaps with other food
        if (otherFood != null && otherFood.getPosition().equals(position)) {
            return true;
        }

        return false;
    }
} 