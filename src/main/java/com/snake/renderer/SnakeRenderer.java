package com.snake.renderer;

import com.model.Snake;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

public class SnakeRenderer {
    private static final Color SNAKE_COLOR = Color.GREEN;
    private static final Color HEAD_COLOR = new Color(0, 180, 0);
    private static final int TILE_SIZE = 20;

    public void render(Graphics g, Snake snake) {
        var body = snake.getBody();
        
        // Draw body
        g.setColor(SNAKE_COLOR);
        for (int i = 1; i < body.size(); i++) {
            Point segment = body.get(i);
            g.fillRect(segment.x * TILE_SIZE, 
                      segment.y * TILE_SIZE, 
                      TILE_SIZE - 1, 
                      TILE_SIZE - 1);
        }
        
        // Draw head
        if (!body.isEmpty()) {
            Point head = body.get(0);
            g.setColor(HEAD_COLOR);
            g.fillRect(head.x * TILE_SIZE, 
                      head.y * TILE_SIZE, 
                      TILE_SIZE - 1, 
                      TILE_SIZE - 1);
        }
    }
} 