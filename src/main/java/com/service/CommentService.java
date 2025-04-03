package com.service;

import com.dao.CommentDao;
import com.model.Comment;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CommentService {
    private final CommentDao commentDao;

    public CommentService(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public List<Comment> getAllComments() throws SQLException {
        List<Comment> comments = commentDao.getAllComments();
        if (comments.isEmpty()) {
            System.out.println("등록된 댓글이 없습니다.");
        }
        return comments;
    }

    public Optional<Comment> getCommentById(int id) throws SQLException {
        Comment comment = commentDao.getCommentById(id);
        if (comment == null) {
            System.out.println("해당 ID의 댓글을 찾을 수 없습니다: " + id);
            return Optional.empty();
        }
        return Optional.of(comment);
    }

    public List<Comment> getAllCommentsByUserId(int userId) throws SQLException {
        List<Comment> comments = commentDao.getAllCommentsByUserId(userId);
        if (comments.isEmpty()) {
            System.out.println("해당 사용자의 댓글이 없습니다: " + userId);
        }
        return comments;
    }

    public void addComment(int userId, int gameResultId, String content) throws SQLException {
        try {
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("댓글 내용은 비어있을 수 없습니다.");
            }
            commentDao.addComment(userId, gameResultId, content);
            System.out.println("댓글이 성공적으로 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("오류: " + e.getMessage());
            throw e;
        }
    }

}
