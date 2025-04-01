package com.snake.ui.message;

import javax.swing.JOptionPane;

public class MessageDisplay {
    public static String getPlayerName() {
        String name;
        do {
            name = JOptionPane.showInputDialog(
                null,
                "Enter your name:",
                "Snake Game",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (name == null) {
                int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to exit?",
                    "Snake Game",
                    JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        } while (name == null || name.trim().isEmpty());
        
        return name.trim();
    }

    public static void showWelcomeMessage() {
        JOptionPane.showMessageDialog(
            null,
            """
            Welcome to Snake!
            
            Controls:
            - Arrow keys to move
            - 'A' to toggle AI mode
            - SPACE to restart after game over
            
            Collect food to grow and avoid obstacles!
            """,
            "Snake Game",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
} 