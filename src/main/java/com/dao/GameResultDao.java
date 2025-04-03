package com.dao;

import com.model.GameResult;
import com.util.QueryUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameResultDao {
    private final Connection connection;
    
    public GameResultDao(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }
    
    public void insert(GameResult result) throws SQLException {
        String sql = QueryUtil.getQuery("addGameResult");
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, result.userId());
            stmt.setInt(2, result.food1Score());
            stmt.setInt(3, result.food2Score());
            stmt.setInt(4, result.obstacleCount());
            stmt.setLong(5, result.gameDuration());
            stmt.setTimestamp(6, Timestamp.valueOf(result.endTime()));
            stmt.setBoolean(7, result.wasAIEnabled());
            stmt.executeUpdate();
        }
    }
    
    public List<GameResult> findByUserId(int userId) throws SQLException {
        String sql = QueryUtil.getQuery("getGameResultsByUserId");
        List<GameResult> results = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSet(rs));
                }
            }
        }
        return results;
    }
    
    public List<GameResult> findAll() throws SQLException {
        String sql = QueryUtil.getQuery("getAllGameResults");
        List<GameResult> results = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(mapResultSet(rs));
            }
        }
        return results;
    }
    
    private GameResult mapResultSet(ResultSet rs) throws SQLException {
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
