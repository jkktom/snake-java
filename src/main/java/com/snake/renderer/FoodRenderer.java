package com.snake.renderer;

import com.model.Food;
import java.awt.Graphics;
import java.awt.Color;

public class FoodRenderer {
    private static final Color FOOD1_COLOR = Color.RED;
    private static final Color FOOD2_COLOR = Color.ORANGE;
    private static final int TILE_SIZE = 20;

    public void render(Graphics g, Food food1, Food food2) {
        // Draw first food
        g.setColor(FOOD1_COLOR);
        g.fillOval(food1.getPosition().x * TILE_SIZE,
                   food1.getPosition().y * TILE_SIZE,
                   TILE_SIZE - 1,
                   TILE_SIZE - 1);

        // Draw second food
        g.setColor(FOOD2_COLOR);
        g.fillOval(food2.getPosition().x * TILE_SIZE,
                   food2.getPosition().y * TILE_SIZE,
                   TILE_SIZE - 1,
                   TILE_SIZE - 1);
    }
} 