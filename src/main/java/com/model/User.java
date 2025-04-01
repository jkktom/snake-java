package com.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record User(
    int id,
    String username,
    List<GameResult> gameHistory
) {
    public User {
        gameHistory = new ArrayList<>(gameHistory); // Defensive copy
    }
    
    public User(String username) {
        this(0, username, new ArrayList<>());
    }
    
    public List<GameResult> getGameHistory() {
        return Collections.unmodifiableList(gameHistory);
    }
    
    public User addGameResult(GameResult result) {
        List<GameResult> newHistory = new ArrayList<>(gameHistory);
        newHistory.add(result);
        return new User(id, username, newHistory);
    }
} 