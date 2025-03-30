import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record GameResult(
    String playerName,
    int score,
    int redFoodCount,
    int orangeFoodCount,
    int obstacleCount,
    double durationInSeconds,
    LocalDateTime endTime,
    boolean wasAIEnabled
) {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String getFormattedSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Game Summary for ").append(playerName).append("\n");
        summary.append("----------------------------\n");
        summary.append("Final Score: ").append(score).append("\n");
        summary.append("Duration: ").append(String.format("%.2f seconds", durationInSeconds)).append("\n");
        summary.append("End Time: ").append(endTime.format(timeFormatter)).append("\n");
        summary.append("Mode: ").append(wasAIEnabled ? "AI" : "Manual").append("\n");
        summary.append("\nItems Collected:\n");
        summary.append("- Red Food: ").append(redFoodCount).append("\n");
        summary.append("- Orange Food: ").append(orangeFoodCount).append("\n");
        summary.append("- Obstacles: ").append(obstacleCount).append("\n");
        
        return summary.toString();
    }

    public String getFormattedEndTime() {
        return endTime.format(timeFormatter);
    }
} 