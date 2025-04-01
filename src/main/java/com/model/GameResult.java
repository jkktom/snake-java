package com.model;

import java.time.LocalDateTime;

public record GameResult(
    String playerName,
    int score,
    int obstacleCount,
    LocalDateTime endTime,
    boolean wasAIEnabled
) {
    public String getFormattedSummary() {
        return String.format(
            "Player: %s | Score: %d | Obstacles: %d | AI: %s",
            playerName, score, obstacleCount, wasAIEnabled ? "Yes" : "No"
        );
    }
} 