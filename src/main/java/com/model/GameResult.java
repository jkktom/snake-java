package com.model;

import java.time.Duration;
import java.time.LocalDateTime;

public record GameResult(
    int playerId,
    int food1Score,
    int food2Score,
    int obstacleCount,
    Duration gameDuration,
    LocalDateTime endTime,
    boolean wasAIEnabled
) {
    public GameResult {
        if (food1Score < 0 || food2Score < 0) {
            throw new IllegalArgumentException("Scores cannot be negative");
        }
        if (obstacleCount < 0) {
            throw new IllegalArgumentException("Obstacle count cannot be negative");
        }
        if (gameDuration.isNegative()) {
            throw new IllegalArgumentException("Game duration cannot be negative");
        }
    }
    
    public int getTotalScore() {
        return food1Score + food2Score;
    }
    
    public String getFormattedDuration() {
        long minutes = gameDuration.toMinutes();
        long seconds = gameDuration.minusMinutes(minutes).getSeconds();
        return String.format("%d:%02d", minutes, seconds);
    }
    
    public String getFormattedSummary() {
        return String.format(
            "Player ID: %d | Score (Food1: %d, Food2: %d) | Obstacles: %d | Time: %s | AI: %s",
            playerId, food1Score, food2Score, obstacleCount, getFormattedDuration(), 
            wasAIEnabled ? "Yes" : "No"
        );
    }
} 