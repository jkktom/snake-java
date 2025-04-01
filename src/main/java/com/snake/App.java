package com.snake;

import com.model.Frame;
import com.model.User;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Get player name
            String username = getPlayerName();
            if (username == null) {
                System.exit(0);
            }
            
            // Create user and game frame
            User user = new User(username);
            Frame gameFrame = new Frame(); // Uses default 600x600 size
            
            // Create game window
            JFrame window = new JFrame("Snake Game - " + username);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            
            // Create and add game panel
            SnakeGame snakeGame = new SnakeGame(gameFrame, user);
            window.add(snakeGame);
            window.pack();
            
            // Center window on screen
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
    
    private static String getPlayerName() {
        String username = null;
        while (username == null || username.trim().isEmpty()) {
            username = JOptionPane.showInputDialog(null,
                "Enter your name to start the game:",
                "Snake Game",
                JOptionPane.QUESTION_MESSAGE);
            
            if (username == null) {
                JOptionPane.showMessageDialog(null,
                    "Game cannot start without a player name.\nExiting game.",
                    "Game Exit",
                    JOptionPane.WARNING_MESSAGE);
                return null;
            }
            
            username = username.trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "Please enter a valid name.",
                    "Invalid Name",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
        return username;
    }
} 