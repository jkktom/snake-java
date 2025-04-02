package com.view;

import com.model.Comment;
import com.service.CommentService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CommentView {
    private final CommentService commentService;
    private final Scanner scanner;

    public CommentView(CommentService commentService) {
        this.commentService = commentService;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== 댓글 관리 ===");
            System.out.println("1. 전체 댓글 조회");
            System.out.println("2. 댓글 ID로 조회");
            System.out.println("3. 사용자별 댓글 조회");
            System.out.println("4. 댓글 등록");
            System.out.println("0. 뒤로가기");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기

            try {
                switch (choice) {
                    case 1 -> getAllComments();
                    case 2 -> getCommentById();
                    case 3 -> getAllCommentsByUserId();
                    case 4 -> addComment();
                    case 0 -> {
                        System.out.println("메인 메뉴로 돌아갑니다.");
                        return;
                    }
                    default -> System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
                }
            } catch (SQLException e) {
                System.out.println("데이터베이스 오류: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("오류 발생: " + e.getMessage());
            }
        }
    }

    private void getAllComments() throws SQLException {
        List<Comment> comments = commentService.getAllComments();
        if (!comments.isEmpty()) {
            System.out.println("\n=== 전체 댓글 목록 ===");
            comments.forEach(comment -> System.out.println(commentService.getCommentSummary(comment)));
        }
    }

    private void getCommentById() throws SQLException {
        System.out.print("조회할 댓글 ID를 입력하세요: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        commentService.getCommentById(id)
            .ifPresent(comment -> System.out.println(commentService.getCommentSummary(comment)));
    }

    private void getAllCommentsByUserId() throws SQLException {
        System.out.print("조회할 사용자 ID를 입력하세요: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        List<Comment> comments = commentService.getAllCommentsByUserId(userId);
        if (!comments.isEmpty()) {
            System.out.println("\n=== 사용자 " + userId + "의 댓글 목록 ===");
            comments.forEach(comment -> System.out.println(commentService.getCommentSummary(comment)));
        }
    }

    private void addComment() throws SQLException {
        System.out.print("사용자 ID를 입력하세요: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        System.out.print("게임 기록 ID를 입력하세요: ");
        int gameResultId = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        System.out.print("댓글 내용을 입력하세요: ");
        String content = scanner.nextLine();

        commentService.addComment(userId, gameResultId, content);
    }
}
