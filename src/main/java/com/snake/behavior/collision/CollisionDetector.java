package com.snake.behavior.collision;

import com.model.Snake;
import com.snake.behavior.obstacle.ObstacleManager;
import java.awt.Point;

public class CollisionDetector {
    private final ObstacleManager obstacleManager;

    public record CollisionResult(boolean hasCollision) {}

    public CollisionDetector(ObstacleManager obstacleManager) {
        this.obstacleManager = obstacleManager;
    }

    public CollisionResult checkCollision(Point position, Snake snake) {
        if (snake.getBody().contains(position)) {
            return new CollisionResult(true);
        } else if (obstacleManager.checkCollision(position)) {
            return new CollisionResult(true);
        }
        return new CollisionResult(false);
    }
}