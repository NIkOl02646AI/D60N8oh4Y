// 代码生成时间: 2025-09-19 20:05:56
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import java.util.Random;

/**
 * A Javalin application that provides a REST API for generating random numbers.
 */
public class RandomNumberGenerator {

    private static final Random random = new Random();
    private static final int MIN_VALUE = 1; // Minimum value for a random number
    private static final int MAX_VALUE = 100; // Maximum value for a random number

    /**
     * Main method to start the Javalin application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);
        configureEndpoints(app);
    }

    /**
     * Configures the API endpoints for the application.
     * @param app The Javalin application instance.
     */
    private static void configureEndpoints(Javalin app) {
        // Endpoint to generate and return a random number
        app.get("/random", ctx -> {
            try {
                int randomNumber = generateRandomNumber();
                ctx.json(randomNumber);
            } catch (Exception e) {
                ctx.status(500);
                ctx.result("Error generating random number: " + e.getMessage());
            }
        });
    }

    /**
     * Generates a random number between a specified minimum and maximum value.
     * @return A random number between MIN_VALUE and MAX_VALUE.
     */
    private static int generateRandomNumber() {
        return random.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;
    }
}
