package com.snake;

import com.model.Direction;
import com.model.Food;
import com.model.Obstacle;
import java.awt.Point;

public class SnakeAI {
    private final SnakeGame game;
    private boolean enabled;

    public SnakeAI(SnakeGame game) {
        this.game = game;
        this.enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enableAI() {
        enabled = true;
    }

    public void disableAI() {
        enabled = false;
    }

    public void makeMove() {
        if (!enabled) return;

        // Get current state
        Point head = game.getSnake().getHead();
        Point food1Pos = game.getFood1().getPosition();
        Point food2Pos = game.getFood2().getPosition();

        // Find closest food
        Point targetFood = getClosestFood(head, food1Pos, food2Pos);

        // Calculate direction to move
        Direction currentDir = game.getSnake().getDirection();
        Direction bestDir = getBestDirection(head, targetFood, currentDir);

        // Update snake direction
        if (bestDir != null) {
            game.setSnakeDirection(bestDir);
        }
    }

    private Point getClosestFood(Point head, Point food1, Point food2) {
        int dist1 = Math.abs(head.x - food1.x) + Math.abs(head.y - food1.y);
        int dist2 = Math.abs(head.x - food2.x) + Math.abs(head.y - food2.y);
        return dist1 <= dist2 ? food1 : food2;
    }

    private Direction getBestDirection(Point head, Point target, Direction currentDir) {
        // Try each direction
        Direction[] directions = Direction.values();
        Direction bestDir = null;
        int minDist = Integer.MAX_VALUE;

        for (Direction dir : directions) {
            // Skip opposite direction
            if (dir.isOpposite(currentDir)) continue;

            // Calculate new position
            Point newPos = new Point(
                head.x + dir.getDx(),
                head.y + dir.getDy()
            );

            // Skip if would hit wall
            if (!game.getFrame().contains(newPos.x, newPos.y)) continue;

            // Skip if would hit obstacle
            boolean hitObstacle = false;
            for (Obstacle obs : game.getObstacles()) {
                if (obs.getPosition().equals(newPos)) {
                    hitObstacle = true;
                    break;
                }
            }
            if (hitObstacle) continue;

            // Skip if would hit self
            boolean hitSelf = false;
            for (Point bodyPart : game.getSnake().getBody()) {
                if (bodyPart.equals(newPos)) {
                    hitSelf = true;
                    break;
                }
            }
            if (hitSelf) continue;

            // Calculate manhattan distance to target
            int dist = Math.abs(newPos.x - target.x) + Math.abs(newPos.y - target.y);
            if (dist < minDist) {
                minDist = dist;
                bestDir = dir;
            }
        }

        return bestDir;
    }
} 