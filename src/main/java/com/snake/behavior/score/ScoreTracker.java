package com.snake.behavior.score;

public class ScoreTracker {
    private int food1Score;
    private int food2Score;
    private long startTime;

    public ScoreTracker() {
        reset();
    }

    public void reset() {
        food1Score = 0;
        food2Score = 0;
        startTime = System.currentTimeMillis();
    }

    public void incrementFood1Score() {
        food1Score++;
    }

    public void incrementFood2Score() {
        food2Score++;
    }

    public int getFood1Score() {
        return food1Score;
    }

    public int getFood2Score() {
        return food2Score;
    }

    public int getTotalScore() {
        return food1Score + food2Score;
    }

    public long getGameDuration() {
        return System.currentTimeMillis() - startTime;
    }
} 