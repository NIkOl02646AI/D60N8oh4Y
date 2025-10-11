// 代码生成时间: 2025-10-12 01:56:21
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class LeaderboardApp {

    private static final Map<Integer, String> leaderboard = new ConcurrentHashMap<>();
    private static final int MAX_ENTRIES = 10; // Maximum number of entries in the leaderboard

    // Method to add a score to the leaderboard
    private static void addToLeaderboard(int score, String name) {
        if (leaderboard.size() < MAX_ENTRIES) {
            leaderboard.put(score, name);
        } else {
            leaderboard.entrySet().stream()
                .min(Map.Entry.comparingByKey()) // Find the minimum score entry
                .ifPresent(minEntry -> leaderboard.remove(minEntry.getKey())); // Remove the minimum score entry
            leaderboard.put(score, name); // Add the new entry
        }
    }

    // Method to retrieve the leaderboard
    public static Map<Integer, String> getLeaderboard() {
        return leaderboard;
    }

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000); // Start Javalin server on port 7000

        // Endpoint to handle POST requests and add scores to the leaderboard
        app.post("/leaderboard", ctx -> {
            String name = ctx.formParam("name");
            int score = Integer.parseInt(ctx.formParam("score"));
            try {
                addToLeaderboard(score, name);
                ctx.status(200).result("Score added successfully!");
            } catch (NumberFormatException e) {
                ctx.status(400).result("Invalid score format. Please provide a valid integer score.");
            }
        });

        // Endpoint to retrieve the leaderboard
        app.get("/leaderboard", ctx -> {
            ctx.json(leaderboard);
        });
    }
}
