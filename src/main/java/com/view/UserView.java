package com.view;

import com.model.User;
import com.service.UserService;
import com.model.GameResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserView {
    private final UserService userService;
    private final Scanner scanner;

    public UserView(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== 유저 관리 시스템 =====");
            System.out.println("1. 전체 유저 조회");
            System.out.println("2. 유저 조회 (ID)");
            System.out.println("3. 유저 조회 (username)");
            System.out.println("0. 뒤로가기");
            System.out.print("선택하세요: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            switch (choice) {
                case 1 -> getAllUsers();
                case 2 -> getUserById();
                case 3 -> getUserByUsername();
                case 0 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }
        }
    }

    private void getAllUsers() {
        List<User> users = userService.getAllUsers();
        System.out.println("\n=== 전체 유저 목록 ===");
        if (users.isEmpty()) {
            System.out.println("등록된 유저가 없습니다.");
            return;
        }
        users.forEach(user -> {
            System.out.printf("ID: %d | Username: %s%n", 
                user.id(), user.username());
        });
    }

    private void getUserById() {
        System.out.print("조회할 유저 ID를 입력하세요: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // 개행 문자 처리

        userService.getUserById(id)
            .ifPresentOrElse(
                user -> displayUserDetails(user),
                () -> System.out.println("해당 ID의 유저를 찾을 수 없습니다: " + id)
            );
    }

    private void getUserByUsername() {
        System.out.print("조회할 유저 이름을 입력하세요: ");
        String username = scanner.nextLine();

        userService.getUserByUsername(username)
            .ifPresentOrElse(
                user -> displayUserDetails(user),
                () -> System.out.println("해당 이름의 유저를 찾을 수 없습니다: " + username)
            );
    }

    private void displayUserDetails(User user) {
        System.out.println("\n=== 유저 상세 정보 ===");
        System.out.printf("ID: %d%n", user.id());
        System.out.printf("Username: %s%n", user.username());
        
        List<GameResult> history = user.getGameHistory();
        System.out.println("\n=== 게임 기록 ===");
        if (history.isEmpty()) {
            System.out.println("게임 기록이 없습니다.");
            return;
        }

        history.forEach(game -> System.out.printf("""
            점수: %d (Food1: %d, Food2: %d)
            장애물 수: %d
            게임 시간: %s
            AI 사용: %s
            ----------------
            """,
            game.getTotalScore(),
            game.food1Score(),
            game.food2Score(),
            game.obstacleCount(),
            game.getFormattedDuration(),
            game.wasAIEnabled() ? "예" : "아니오"
        ));
    }
}
