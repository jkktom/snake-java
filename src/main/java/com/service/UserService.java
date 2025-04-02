package com.service;

import com.dao.UserDao;
import com.model.User;
import com.model.GameResult;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        try {
            return userDao.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get users", e);
        }
    }

    public Optional<User> getUserById(int id) {
        try {
            return Optional.ofNullable(userDao.getUserById(id));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get user by id: " + id, e);
        }
    }

    public Optional<User> getUserByUsername(String username) {
        try {
            return Optional.ofNullable(userDao.getUserByUsername(username));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get user by username: " + username, e);
        }
    }

    public List<GameResult> getUserGameHistory(String username) {
        try {
            User user = userDao.getUserByUsername(username);
            if (user == null) {
                throw new IllegalArgumentException("User not found: " + username);
            }
            return user.getGameHistory();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get game history for user: " + username, e);
        }
    }

    public String getUserStats(String username) {
        List<GameResult> history = getUserGameHistory(username);
        int totalGames = history.size();
        if (totalGames == 0) {
            return String.format("User %s has not played any games yet.", username);
        }

        int totalScore = history.stream().mapToInt(GameResult::getTotalScore).sum();
        double avgScore = (double) totalScore / totalGames;
        GameResult bestGame = history.stream()
            .max((g1, g2) -> Integer.compare(g1.getTotalScore(), g2.getTotalScore()))
            .get();

        return String.format("""
            Stats for %s:
            Total Games: %d
            Average Score: %.2f
            Best Game: %s
            """,
            username, totalGames, avgScore, bestGame.getFormattedSummary()
        );
    }
}
