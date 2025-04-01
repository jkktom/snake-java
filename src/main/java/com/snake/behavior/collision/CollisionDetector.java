package com.snake.behavior.collision;

import com.model.Snake;
import com.snake.behavior.obstacle.ObstacleManager;
import java.awt.Point;

public class CollisionDetector {
    private final ObstacleManager obstacleManager;

    public CollisionDetector(ObstacleManager obstacleManager) {
        this.obstacleManager = obstacleManager;
    }

    public boolean checkCollision(Point position, Snake snake) {
        return checkSelfCollision(position, snake) ||
               checkObstacleCollision(position);
    }

    private boolean checkSelfCollision(Point position, Snake snake) {
        return snake.getBody().contains(position);
    }

    private boolean checkObstacleCollision(Point position) {
        return obstacleManager.checkCollision(position);
    }
} 