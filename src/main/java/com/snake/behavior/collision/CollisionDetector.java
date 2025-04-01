package com.snake.behavior.collision;

import com.model.Snake;
import com.model.Frame;
import com.snake.behavior.obstacle.ObstacleManager;
import java.awt.Point;

public class CollisionDetector {
    private final Frame frame;
    private final ObstacleManager obstacleManager;

    public CollisionDetector(Frame frame, ObstacleManager obstacleManager) {
        this.frame = frame;
        this.obstacleManager = obstacleManager;
    }

    public boolean checkCollision(Point position, Snake snake) {
        return checkWallCollision(position) ||
               checkSelfCollision(position, snake) ||
               checkObstacleCollision(position);
    }

    private boolean checkWallCollision(Point position) {
        return position.x < 0 || position.x >= frame.width() ||
               position.y < 0 || position.y >= frame.height();
    }

    private boolean checkSelfCollision(Point position, Snake snake) {
        return snake.getBody().contains(position);
    }

    private boolean checkObstacleCollision(Point position) {
        return obstacleManager.checkCollision(position);
    }
} 