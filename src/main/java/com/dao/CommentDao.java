package com.dao;

import com.model.Comment;
import com.util.QueryUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {
    private final Connection connection;

    public CommentDao(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    public List<Comment> getAllComments() throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String query = QueryUtil.getQuery("getAllComments");

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                comments.add(mapComment(rs));
            }
        }
        return comments;
    }

    public Comment getCommentById(int id) throws SQLException {
        String query = QueryUtil.getQuery("getCommentById");

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapComment(rs);
                }
            }
        }
        return null;
    }

    public List<Comment> getAllCommentsByUserId(int userId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String query = QueryUtil.getQuery("getAllCommentsByUserId");

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(mapComment(rs));
                }
            }
        }
        return comments;
    }

    public void addComment(int userId, int gameResultId, String content) throws SQLException {
        String query = QueryUtil.getQuery("addComment");

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, gameResultId);
            stmt.setString(3, content);
            stmt.executeUpdate();
        }
    }

    private Comment mapComment(ResultSet rs) throws SQLException {
        return new Comment(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getInt("game_result_id"),
            rs.getString("content")
        );
    }
}
