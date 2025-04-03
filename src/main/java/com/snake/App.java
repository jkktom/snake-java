package com.snake;

import com.model.Frame;
import com.model.User;
import com.config.JDBCConnection;
import com.dao.UserDao;
import com.dao.GameResultDao;
import com.service.UserService;
import com.service.GameResultService;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize database connection
                Connection connection = JDBCConnection.getConnection();
                
                // Initialize DAOs and Services
                UserDao userDao = new UserDao(connection);
                GameResultDao gameResultDao = new GameResultDao(connection);
                UserService userService = new UserService(userDao);
                GameResultService gameResultService = new GameResultService(gameResultDao);
                
                // Get player name
                String username = getPlayerName();
                if (username == null) {
                    connection.close();
                    System.exit(0);
                }
                
                // Create or get existing user
                User user = userService.getUserByUsername(username)
                    .orElseGet(() -> {
                        try {
                            return userService.addUser(username);
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null,
                                "데이터베이스 오류: " + e.getMessage(),
                                "오류",
                                JOptionPane.ERROR_MESSAGE);
                            return new User(0, username); // Fallback with temporary ID
                        }
                    });
                
                // Create game frame
                Frame gameFrame = new Frame(); // Uses default 600x600 size
                
                // Create game window
                JFrame window = new JFrame("Snake Game - " + username);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setResizable(false);
                
                // Create and add game panel with services
                SnakeGame snakeGame = new SnakeGame(gameFrame, user, gameResultService);
                window.add(snakeGame);
                window.pack();
                
                // Center window on screen
                window.setLocationRelativeTo(null);
                window.setVisible(true);
                
                // Add window closing listener to clean up resources
                window.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        try {
                            if (!connection.isClosed()) {
                                connection.close();
                            }
                            JDBCConnection.close();
                        } catch (SQLException e) {
                            System.err.println("데이터베이스 연결 종료 중 오류: " + e.getMessage());
                        }
                    }
                });
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                    "데이터베이스 연결 오류: " + e.getMessage(),
                    "오류",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
    
    private static String getPlayerName() {
        String username = null;
        while (username == null || username.trim().isEmpty()) {
            username = JOptionPane.showInputDialog(null,
                "게임을 시작하려면 이름을 입력하세요:",
                "Snake Game",
                JOptionPane.QUESTION_MESSAGE);
            
            if (username == null) {
                JOptionPane.showMessageDialog(null,
                    "플레이어 이름 없이 게임을 시작할 수 없습니다.\n게임을 종료합니다.",
                    "게임 종료",
                    JOptionPane.WARNING_MESSAGE);
                return null;
            }
            
            username = username.trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "올바른 이름을 입력해주세요.",
                    "잘못된 이름",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
        return username;
    }
} 