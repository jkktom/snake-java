package com.model;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private User author;
    private String content;
    private LocalDateTime createdAt;
    private GameResult gameResult;  // Optional: comment can be associated with a specific game
    
    public Comment(User author, String content) {
        this.author = author;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
    
    public Comment(User author, String content, GameResult gameResult) {
        this(author, content);
        this.gameResult = gameResult;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public GameResult getGameResult() { return gameResult; }
    public void setGameResult(GameResult gameResult) { this.gameResult = gameResult; }
} 