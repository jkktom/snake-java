package com.snake.behavior.food;

import com.model.Food;
import com.model.Snake;
import java.awt.Point;

public class FoodCollisionHandler {
    private final FoodPlacer foodPlacer;

    public FoodCollisionHandler(FoodPlacer foodPlacer) {
        this.foodPlacer = foodPlacer;
    }

    public boolean checkFoodCollision(Point newHead, Food food) {
        return newHead.equals(food.getPosition());
    }

    public Snake handleFoodCollision(Snake snake, Point newHead) {
        return snake.grow(newHead);
    }
} 