package com.snake.renderer;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class UIRenderer {
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font SCORE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final int TILE_SIZE = 20;

    public void render(Graphics g, String username, int score, 
                      int obstacleCount, boolean isAI, boolean isGameOver) {
        g.setColor(TEXT_COLOR);
        g.setFont(SCORE_FONT);

        // Draw player info
        g.drawString(username + "'s Score: " + score, 
                    TILE_SIZE - 16, TILE_SIZE);
        g.drawString("Mode: " + (isAI ? "AI" : "Manual"), 
                    TILE_SIZE - 16, TILE_SIZE * 2);
        g.drawString("Obstacles: " + obstacleCount, 
                    TILE_SIZE - 16, TILE_SIZE * 3);

        // Draw game over message
        if (isGameOver) {
            String gameOver = "Game Over! Press SPACE to restart";
            g.setFont(new Font("Arial", Font.BOLD, 24));
            int width = g.getFontMetrics().stringWidth(gameOver);
            g.drawString(gameOver, (800 - width) / 2, 300);
        }
    }
} 