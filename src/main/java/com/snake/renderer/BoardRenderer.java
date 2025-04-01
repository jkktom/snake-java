package com.snake.renderer;

import java.awt.Graphics;
import java.awt.Color;

public class BoardRenderer {
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color GRID_COLOR = new Color(20, 20, 20);
    private static final int TILE_SIZE = 20;

    public void render(Graphics g) {
        // Fill background
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, 800, 600);

        // Draw grid
        g.setColor(GRID_COLOR);
        for (int x = 0; x < 800; x += TILE_SIZE) {
            g.drawLine(x, 0, x, 600);
        }
        for (int y = 0; y < 600; y += TILE_SIZE) {
            g.drawLine(0, y, 800, y);
        }
    }
} 