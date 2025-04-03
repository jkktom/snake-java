package com.view;

import com.model.GameResult;
import com.service.GameResultService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class GameResultView {
    private final GameResultService gameResultService;
    private final Scanner scanner;

    public GameResultView(GameResultService gameResultService) {
        this.gameResultService = gameResultService;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== 기록 관리 시스템 =====");
            System.out.println("1. 전체 기록 조회");
            System.out.println("2. 단일 기록 조회 (ID)");
            System.out.println("3. 유저 별 기록 전체 조회"); 
            System.out.println("0. 뒤로가기");
            System.out.print("선택하세요: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            switch (choice) {
                case 1 -> getAllGameResults();
                case 2 -> getAllGameResultsByUserId();
                case 0 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }
        }
    }

    private void getAllGameResults() {
        try {
            List<GameResult> results = gameResultService.getAllGameResults();
            if (results.isEmpty()) {
                System.out.println("게임 기록이 없습니다.");
                return;
            }
            System.out.println("\n=== 전체 게임 기록 ===");
            results.forEach(this::displayGameResult);
        } catch (SQLException e) {
            System.out.println("게임 기록 조회 중 오류 발생: " + e.getMessage());
        }
    }

    private void getAllGameResultsByUserId() {
        System.out.print("조회할 유저 ID를 입력하세요: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // 개행 문자 처리

        List<GameResult> results = gameResultService.getAllGameResultsByUserId(userId);
        if (results.isEmpty()) {
            System.out.println("해당 유저의 게임 기록이 없습니다.");
            return;
        }

        System.out.printf("\n=== 유저 ID: %d의 게임 기록 ===\n", userId);
        results.forEach(this::displayGameResult);
    }

    private void displayGameResult(GameResult result) {
        System.out.println("\n----------------");
        System.out.println(gameResultService.getGameResultSummary(result));
        System.out.println("----------------");
    }
}
