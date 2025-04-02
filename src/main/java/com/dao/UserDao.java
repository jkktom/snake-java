package com.dao;

import com.model.User;
import com.model.GameResult;
import com.util.QueryUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final Connection connection;
    
    public UserDao(Connection connection) {
        this.connection = connection;
    }
    
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = QueryUtil.getQuery("getAllUsers");
        
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        }
        return users;
    }
    
    public User getUserById(int id) throws SQLException {
        String query = QueryUtil.getQuery("getUserById");
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }
    
    public User getUserByUsername(String username) throws SQLException {
        String query = QueryUtil.getQuery("getUserByUsername");
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }
    
    private User mapUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        List<GameResult> gameHistory = getGameHistory(id);
        return new User(id, username, gameHistory);
    }
    
    private List<GameResult> getGameHistory(int userId) throws SQLException {
        List<GameResult> gameHistory = new ArrayList<>();
        String query = QueryUtil.getQuery("getAllGameResultsByUserId");
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    gameHistory.add(mapGameResult(rs));
                }
            }
        }
        return gameHistory;
    }
    
    private GameResult mapGameResult(ResultSet rs) throws SQLException {
        return new GameResult(
            rs.getInt("user_id"),
            rs.getInt("food1Score"),
            rs.getInt("food2Score"),
            rs.getInt("obstacleCount"),
            rs.getLong("gameDuration"),
            rs.getTimestamp("endTime").toLocalDateTime(),
            rs.getBoolean("wasAIEnabled")
        );
    }
}
