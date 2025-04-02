package com.model;

import java.time.LocalDateTime;

public record Comment(
    int id,
    int userId,
    int gameResultId,
    String content,
    LocalDateTime createdAt
) {
    public Comment {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 비어있을 수 없습니다.");
        }
    }

    public String getFormattedCreatedAt() {
        return createdAt.toString().replace('T', ' ');
    }

    public String getSummary() {
        return String.format("""
            댓글 ID: %d
            작성자 ID: %d
            게임 기록 ID: %d
            내용: %s
            작성 시간: %s
            """,
            id, userId, gameResultId, content, getFormattedCreatedAt()
        );
    }

    // Convenience constructor for new comments (before ID assignment)
    public Comment(int gameResultId, String content) {
        this(0, 0, gameResultId, content, LocalDateTime.now());
    }
} 