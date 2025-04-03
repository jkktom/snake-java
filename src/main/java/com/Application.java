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
import java.sql.SQLException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;

        try {
            // 데이터베이스 연결 시도
            if (!JDBCConnection.isConnectionValid()) {
                System.out.println("\n❌ 데이터베이스 연결에 실패했습니다.");
                System.out.println("다음 사항을 확인해주세요:");
                System.out.println("1. MySQL 서버가 실행 중인지 확인");
                System.out.println("2. 데이터베이스 접속 정보(URL, 사용자 이름, 비밀번호)가 올바른지 확인");
                System.out.println("3. 데이터베이스와 테이블이 생성되어 있는지 확인");
                return;
            }

            connection = JDBCConnection.getConnection();
            System.out.println("\n✅ 데이터베이스에 성공적으로 연결되었습니다.");

            while (true) {
                try {
                    System.out.println("\n===== 게임 기록 관리 시스템 =====");
                    System.out.println("1. 유저 관리");
                    System.out.println("2. 게임 기록 관리");
                    System.out.println("3. 댓글 관리");
                    System.out.println("0. 종료");
                    System.out.print("선택: ");

                    int choice = scanner.nextInt();
                    scanner.nextLine(); // 개행 문자 처리

                    if (choice == 0) {
                        System.out.println("🚀 프로그램을 종료합니다.");
                        break;
                    }

                    // 각 작업 시작 전 연결 상태 확인
                    if (!connection.isValid(1)) {
                        System.out.println("\n❌ 데이터베이스 연결이 끊어졌습니다. 프로그램을 다시 시작해주세요.");
                        break;
                    }

                    switch (choice) {
                        case 1 -> startUserManagement(connection);
                        case 2 -> startGameResultManagement(connection);
                        case 3 -> startCommentManagement(connection);
                        default -> System.out.println("❌ 잘못된 입력입니다. 다시 선택하세요.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ 숫자를 입력해주세요.");
                    scanner.nextLine(); // 잘못된 입력 비우기
                } catch (SQLException e) {
                    System.out.println("\n❌ 데이터베이스 작업 중 오류가 발생했습니다: " + e.getMessage());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("\n❌ 예상치 못한 오류가 발생했습니다: " + e.getMessage());
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
                JDBCConnection.close();
                scanner.close();
            } catch (SQLException e) {
                System.out.println("❌ 연결 종료 중 오류가 발생했습니다: " + e.getMessage());
            }
        }
    }

    // 모델 관리
    private static void startUserManagement(Connection connection) {
        try {
            UserDao userDao = new UserDao(connection);
            UserService userService = new UserService(userDao);
            UserView userView = new UserView(userService);
            userView.showMenu();
        } catch (Exception e) {
            System.out.println("❌ 유저 관리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 게임 기록 관리
    private static void startGameResultManagement(Connection connection) {
        try {
            GameResultDao gameResultDao = new GameResultDao(connection);
            GameResultService gameResultService = new GameResultService(gameResultDao);
            GameResultView gameResultView = new GameResultView(gameResultService);
            gameResultView.showMenu();
        } catch (Exception e) {
            System.out.println("❌ 게임 기록 관리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 댓글 관리
    private static void startCommentManagement(Connection connection) {
        try {
            CommentDao commentDao = new CommentDao(connection);
            CommentService commentService = new CommentService(commentDao);
            CommentView commentView = new CommentView(commentService);
            commentView.showMenu();
        } catch (Exception e) {
            System.out.println("❌ 댓글 관리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
