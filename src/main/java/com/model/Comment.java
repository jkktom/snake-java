package com.model;

public record Comment(
    int id,
    int userId,
    int gameResultId,
    String content
) {
    public Comment {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be null or empty");
        }
        content = content.trim();
    }

    @Override
    public String toString() {
        return String.format("💬 [ID: %d] %s (작성자: %d, 게임: %d)", 
            id, content, userId, gameResultId);
    }

    // Convenience constructor for new comments (before ID assignment)
    public Comment(int gameResultId, String content) {
        this(0, 0, gameResultId, content);
    }
} 