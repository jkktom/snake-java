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
            try {
                System.out.println("\n=== 댓글 관리 ===");
                System.out.println("1. 전체 댓글 조회");
                System.out.println("2. 댓글 ID로 조회");
                System.out.println("3. 사용자별 댓글 조회");
                System.out.println("4. 댓글 등록");
                System.out.println("0. 뒤로가기");
                System.out.print("선택: ");

                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("❌ 선택을 입력해주세요.");
                    continue;
                }

                try {
                    int choice = Integer.parseInt(input);
                    switch (choice) {
                        case 1 -> getAllComments();
                        case 2 -> getCommentById();
                        case 3 -> getAllCommentsByUserId();
                        case 4 -> addComment();
                        case 0 -> {
                            System.out.println("메인 메뉴로 돌아갑니다.");
                            return;
                        }
                        default -> System.out.println("❌ 잘못된 선택입니다. 다시 선택해주세요.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ 숫자를 입력해주세요.");
                }
            } catch (SQLException e) {
                System.out.println("❌ 데이터베이스 오류: " + e.getMessage());
                    return;

            } catch (Exception e) {
                System.out.println("❌ 예상치 못한 오류가 발생했습니다: " + e.getMessage());
                return;
            }
        }
    }

    private void getAllComments() throws SQLException {
        List<Comment> comments = commentService.getAllComments();
        if (comments.isEmpty()) {
            System.out.println("\n💬 등록된 댓글이 없습니다.");
            return;
        }
        System.out.println("\n=== 전체 댓글 목록 ===");
        comments.forEach(comment -> System.out.println(commentService.getCommentSummary(comment)));
        System.out.println("\n총 " + comments.size() + "개의 댓글이 있습니다.");
    }

    private void getCommentById() throws SQLException {
        try {
            System.out.print("조회할 댓글 ID를 입력하세요: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("❌ 댓글 ID를 입력해주세요.");
                return;
            }

            int id = Integer.parseInt(input);
            var comment = commentService.getCommentById(id);
            
            if (comment.isEmpty()) {
                System.out.println("❌ 해당 ID의 댓글을 찾을 수 없습니다: " + id);
                return;
            }
            
            System.out.println("\n=== 댓글 상세 정보 ===");
            System.out.println(commentService.getCommentSummary(comment.get()));
            
        } catch (NumberFormatException e) {
            System.out.println("❌ 유효한 숫자를 입력해주세요.");
        }
    }

    private void getAllCommentsByUserId() throws SQLException {
        try {
            System.out.print("조회할 사용자 ID를 입력하세요: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("❌ 사용자 ID를 입력해주세요.");
                return;
            }

            int userId = Integer.parseInt(input);
            List<Comment> comments = commentService.getAllCommentsByUserId(userId);
            
            if (comments.isEmpty()) {
                System.out.println("💬 해당 사용자의 댓글이 없습니다: " + userId);
                return;
            }
            
            System.out.println("\n=== 사용자 " + userId + "의 댓글 목록 ===");
            comments.forEach(comment -> System.out.println(commentService.getCommentSummary(comment)));
            System.out.println("\n총 " + comments.size() + "개의 댓글이 있습니다.");
            
        } catch (NumberFormatException e) {
            System.out.println("❌ 유효한 숫자를 입력해주세요.");
        }
    }

    private void addComment() throws SQLException {
        try {
            System.out.print("사용자 ID를 입력하세요: ");
            String userInput = scanner.nextLine().trim();
            if (userInput.isEmpty()) {
                System.out.println("❌ 사용자 ID를 입력해주세요.");
                return;
            }
            int userId = Integer.parseInt(userInput);

            System.out.print("게임 기록 ID를 입력하세요: ");
            String gameInput = scanner.nextLine().trim();
            if (gameInput.isEmpty()) {
                System.out.println("❌ 게임 기록 ID를 입력해주세요.");
                return;
            }
            int gameResultId = Integer.parseInt(gameInput);

            System.out.print("댓글 내용을 입력하세요: ");
            String content = scanner.nextLine().trim();
            if (content.isEmpty()) {
                System.out.println("❌ 댓글 내용을 입력해주세요.");
                return;
            }

            commentService.addComment(userId, gameResultId, content);
            System.out.println("✅ 댓글이 성공적으로 등록되었습니다.");
            
        } catch (NumberFormatException e) {
            System.out.println("❌ 유효한 숫자를 입력해주세요.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}
