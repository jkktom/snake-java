package com.snake.ui.overlay;

import com.model.GameResult;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.time.format.DateTimeFormatter;

public class GameOverlay {
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 180);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 36);
    private static final Font INFO_FONT = new Font("Arial", Font.PLAIN, 18);
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm:ss");

    public void render(Graphics g, GameResult result) {
        // Draw semi-transparent background
        g.setColor(OVERLAY_COLOR);
        g.fillRect(0, 0, 800, 600);

        // Draw game over title
        g.setColor(TEXT_COLOR);
        g.setFont(TITLE_FONT);
        String gameOver = "Game Over!";
        int titleWidth = g.getFontMetrics().stringWidth(gameOver);
        g.drawString(gameOver, (800 - titleWidth) / 2, 200);

        // Draw game statistics
        g.setFont(INFO_FONT);
        int y = 250;
        int leftMargin = 300;

        String[] info = {
            "Score: " + result.getTotalScore(),
            "Food 1: " + result.food1Score(),
            "Food 2: " + result.food2Score(),
            "Obstacles: " + result.obstacleCount(),
            "Duration: " + result.getFormattedDuration(),
            "End Time: " + TIME_FORMATTER.format(result.endTime()),
            "AI Mode: " + (result.wasAIEnabled() ? "Enabled" : "Disabled"),
            "",
            "Press SPACE to play again"
        };

        for (String line : info) {
            g.drawString(line, leftMargin, y);
            y += 30;
        }
    }
} 