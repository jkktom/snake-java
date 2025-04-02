package com.service;

import com.dao.GameResultDao;
import com.model.GameResult;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GameResultService {
    private final GameResultDao gameResultDao;

    public GameResultService(GameResultDao gameResultDao) {
        this.gameResultDao = gameResultDao;
    }

    public List<GameResult> getAllGameResults() {
        try {
            return gameResultDao.getAllGameResults();
        } catch (SQLException e) {
            throw new RuntimeException("게임 기록 조회 중 오류 발생", e);
        }
    }

    public Optional<GameResult> getGameResultById(int id) {
        try {
            return Optional.ofNullable(gameResultDao.getGameResultById(id));
        } catch (SQLException e) {
            throw new RuntimeException("게임 기록 ID 조회 중 오류 발생: " + id, e);
        }
    }

    public List<GameResult> getAllGameResultsByUserId(int userId) {
        try {
            return gameResultDao.getAllGameResultsByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException("유저의 게임 기록 조회 중 오류 발생: " + userId, e);
        }
    }

    public String getGameResultSummary(GameResult result) {
        return String.format("""
            총점: %d점 (Food1: %d, Food2: %d)
            장애물 수: %d개
            게임 시간: %s
            AI 사용: %s
            종료 시간: %s
            """,
            result.getTotalScore(),
            result.food1Score(),
            result.food2Score(),
            result.obstacleCount(),
            result.getFormattedDuration(),
            result.wasAIEnabled() ? "예" : "아니오",
            result.endTime()
        );
    }
}
