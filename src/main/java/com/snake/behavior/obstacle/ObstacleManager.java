package com.snake.behavior.obstacle;

import com.model.Obstacle;
import com.model.Snake;
import com.model.Food;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public class ObstacleManager {
    private final ObstacleSpawner spawner;
    private final List<Obstacle> obstacles;

    public ObstacleManager(ObstacleSpawner spawner) {
        this.spawner = spawner;
        this.obstacles = new ArrayList<>();
    }

    public void trySpawnObstacle(Snake snake, Food food1, Food food2) {
        if (spawner.shouldSpawnObstacle()) {
            Obstacle newObstacle = spawner.spawnObstacle(snake, obstacles, food1, food2);
            if (newObstacle != null) {
                obstacles.add(newObstacle);
            }
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
    }
} 