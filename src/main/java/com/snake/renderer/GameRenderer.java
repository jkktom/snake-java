package com.snake.renderer;

import com.model.Snake;
import com.model.Food;
import com.model.Obstacle;
import java.awt.Graphics;
import java.awt.Color;
import java.util.List;

public class GameRenderer {
    private final SnakeRenderer snakeRenderer;
    private final BoardRenderer boardRenderer;
    private final FoodRenderer foodRenderer;
    private final UIRenderer uiRenderer;

    public GameRenderer() {
        this.snakeRenderer = new SnakeRenderer();
        this.boardRenderer = new BoardRenderer();
        this.foodRenderer = new FoodRenderer();
        this.uiRenderer = new UIRenderer();
    }

    public void render(Graphics g, Snake snake, Food food1, Food food2, 
                      List<Obstacle> obstacles, String username, 
                      boolean isAI, boolean isGameOver) {
        // Clear background
        boardRenderer.render(g);
        
        // Draw game elements
        snakeRenderer.render(g, snake);
        foodRenderer.render(g, food1, food2);
        
        // Draw obstacles
        for (Obstacle obstacle : obstacles) {
            g.setColor(Color.RED);
            g.fillRect(obstacle.getPosition().x * 20, 
                      obstacle.getPosition().y * 20, 20, 20);
        }
        
        // Draw UI elements
        uiRenderer.render(g, username, snake.getBody().size(), 
                         obstacles.size(), isAI, isGameOver);
    }
} 