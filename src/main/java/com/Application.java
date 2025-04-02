package com;

import com.config.JDBCConnection;
import com.dao.CommentDao;
import com.dao.GameResultDao;
import com.dao.UserDao;
import com.service.CommentService;
import com.service.GameResultService;
import com.service.UserService;
import com.view.CommentView;
import com.view.GameResultView;
import com.view.UserView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws SQLException {
        Connection connection = JDBCConnection.getConnection();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== 게임 기록 관리 시스템 =====");
            System.out.println("1. 유저 관리");
            System.out.println("2. 게임 기록 관리");
            System.out.println("3. 댓글 관리");
            System.out.println("0. 종료");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            switch (choice) {
                case 1 -> startUserManagement(connection);
                case 2 -> startGameResultManagement(connection);
                case 3 -> startCommentManagement(connection);
                case 0 -> {
                    connection.close();
                    System.out.println("🚀 프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("❌ 잘못된 입력입니다. 다시 선택하세요.");
            }
        }
    }

    // 모델 관리
    private static void startUserManagement(Connection connection) {
        UserDao userDao = new UserDao(connection);
        UserService userService = new UserService(userDao);
        UserView userView = new UserView(userService);
        userView.showMenu();
    }

    // 게임 기록 관리
    private static void startGameResultManagement(Connection connection) {
        GameResultDao gameResultDao = new GameResultDao(connection);
        GameResultService gameResultService = new GameResultService(gameResultDao);
        GameResultView gameResultView = new GameResultView(gameResultService);
        gameResultView.showMenu();
    }

    // 댓글 관리
    private static void startCommentManagement(Connection connection) {
        CommentDao commentDao = new CommentDao(connection);
        CommentService commentService = new CommentService(commentDao);
        CommentView commentView = new CommentView(commentService);
        commentView.showMenu();
    }
}
