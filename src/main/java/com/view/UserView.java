package com.view;

import com.model.User;
import com.service.UserService;
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
            System.out.println("0. 뒤로가기");
            System.out.print("선택하세요: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            switch (choice) {
                case 1 -> getAllUsers();
                case 0 -> {
                    System.out.println("메뉴로 돌아갑니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }
        }
    }

    private void getAllUsers() {
        try {
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
        } catch (SQLException e) {
            System.out.println("데이터베이스 오류: " + e.getMessage());
        }
    }
}

