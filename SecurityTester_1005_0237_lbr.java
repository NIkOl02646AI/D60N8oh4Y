// 代码生成时间: 2025-10-05 02:37:22
import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.util.Header;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SecurityTester {

    // Define the Javalin app instance
    private static Javalin app;

    /**
     * Main method to start the Javalin server
     * @param args no arguments are expected
     */
    public static void main(String[] args) {
        // Initialize Javalin app
        app = Javalin.create().start(7000); // Start server on port 7000

        // Define routes
        app.get("/ping", SecurityTester::ping);
        app.post("/test", SecurityTester::performSecurityTest);
    }

    /**
     * Ping endpoint for health check
     */
    private static Handler ping = ctx -> ctx.result("pong");

    /**
     * Perform security test
     * @param ctx the Javalin context
     */
    private static Handler performSecurityTest = ctx -> {
        try {
            // Get the URL from the request body
            String requestBody = ctx.body();
            URL url = new URL(requestBody);

            // Set up the HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty(Header.CONTENT_TYPE, "application/json");

            // Execute the request and read the response
            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Send the response back to the client
            ctx.result(response.toString());

        } catch (Exception e) {
            // Handle any exceptions that occur during the test
            ctx.status(500).result("Error performing security test: " + e.getMessage());
        }
    };

    /**
     * Stops the Javalin server
     */
    public static void stopServer() {
        if (app != null) {
            app.stop();
        }
    }
}
