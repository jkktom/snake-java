package com.dao;

import com.model.GameResult;
import com.util.QueryUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class GameResultDao {
    private final Connection connection;
    
    public GameResultDao(Connection connection) {
        this.connection = connection;
    }
    
    public List<GameResult> getAllGameResults() throws SQLException {
        List<GameResult> gameResults = new ArrayList<>();
        String query = QueryUtil.getQuery("getAllGameResults");
        
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                gameResults.add(mapGameResult(rs));
            }
        }
        return gameResults;
    }

    public GameResult getGameResultById(int id) throws SQLException {
        String query = QueryUtil.getQuery("getGameResultById");
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapGameResult(rs);
                }
            }
        }
        return null;
    }

    public List<GameResult> getAllGameResultsByUserId(int userId) throws SQLException {
        List<GameResult> gameResults = new ArrayList<>();
        String query = QueryUtil.getQuery("getAllGameResultsByUserId");
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    gameResults.add(mapGameResult(rs));
                }
            }
        }
        return gameResults;
    }

    private GameResult mapGameResult(ResultSet rs) throws SQLException {
        return new GameResult(
            rs.getInt("user_id"),
            rs.getInt("food1_score"),
            rs.getInt("food2_score"),
            rs.getInt("obstacle_count"),
            rs.getLong("game_duration"),
            rs.getTimestamp("end_time").toLocalDateTime(),
            rs.getBoolean("was_ai_enabled")
        );
    }
}
