package com.snake.behavior.obstacle;

import com.model.Obstacle;
import com.model.Snake;
import com.model.Food;
import com.model.Frame;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public class ObstacleManager {
    private static final int TILE_SIZE = 20;
    private final ObstacleSpawner spawner;
    private final List<Obstacle> obstacles;
    private final Frame frame;

    public ObstacleManager(ObstacleSpawner spawner) {
        this.spawner = spawner;
        this.obstacles = new ArrayList<>();
        this.frame = spawner.getFrame();
        spawnBorderObstacles();
    }

    private void spawnBorderObstacles() {
        int width = getWidth();
        int height = getHeight();

        // Add top and bottom borders
        for (int x = 0; x < width; x++) {
            obstacles.add(new Obstacle(new Point(x, 0)));  // Top border
            obstacles.add(new Obstacle(new Point(x, height - 1)));  // Bottom border
        }

        // Add left and right borders (excluding corners already added)
        for (int y = 1; y < height - 1; y++) {
            obstacles.add(new Obstacle(new Point(0, y)));  // Left border
            obstacles.add(new Obstacle(new Point(width - 1, y)));  // Right border
        }
    }

    public void trySpawnObstacle(Snake snake, Food food1, Food food2) {
        // Always try to spawn an obstacle
        Obstacle newObstacle = spawner.spawnObstacle(snake, obstacles, food1, food2);
        if (newObstacle != null) {
            obstacles.add(newObstacle);
        }
    }

    public boolean checkCollision(Point position) {
        return obstacles.stream().anyMatch(o -> o.getPosition().equals(position));
    }

    public List<Obstacle> getObstacles() {
        return new ArrayList<>(obstacles);
    }

    public void clear() {
        obstacles.clear();
        spawnBorderObstacles();
    }

    public int getWidth() {
        return frame.width() / TILE_SIZE;
    }

    public int getHeight() {
        return frame.height() / TILE_SIZE;
    }
} 