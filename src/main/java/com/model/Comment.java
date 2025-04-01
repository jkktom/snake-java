package com.model;

public record Comment(
    int commentId,
    int userId,
    int gameResultId,
    String content
) {
    public Comment {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be null or empty");
        }
    }
    
    // Convenience constructor for new comments (before ID assignment)
    public Comment(int userId, int gameResultId, String content) {
        this(0, userId, gameResultId, content);
    }
} 