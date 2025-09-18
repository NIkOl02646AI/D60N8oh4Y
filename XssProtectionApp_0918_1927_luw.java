// 代码生成时间: 2025-09-18 19:27:57
import io.javalin.Javalin;
import io.javalin.core.util.Header;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A Javalin application that demonstrates a simple XSS protection mechanism.
 * This application sets the Content Security Policy (CSP) headers to help mitigate XSS attacks.
 */
public class XssProtectionApp {

    // Define a list of allowed sources for CSP
    private static final List<String> ALLOWED_SOURCES = Arrays.asList(
        "'self'",
        "https://secure.example.com",
        "https://api.example.com"
    );

    public static void main(String[] args) {

        // Create a Javalin instance
        Javalin app = Javalin.create().start(7000);

        // Define the CSP header to prevent XSS attacks
        String csp = ALLOWED_SOURCES.stream()
            .map(source -> "default-src " + source)
            .collect(Collectors.joining("; "));

        // Add a route to set the CSP header for all requests
        app.before((ctx) -> {
            ctx.header(Header.CONTENT_SECURITY_POLICY, csp);
        });

        // Define a simple route to show the protection is in place
        app.get("/", ctx -> ctx.result("Hello, this page is protected against XSS attacks!"));

        // Define a route to allow users to input data, which should be escaped to prevent XSS
        app.post("/input", ctx -> {
            String userInput = ctx.body();
            // Escape the user input to prevent XSS
            String safeInput = escapeHtml(userInput);
            ctx.result("Received input: " + safeInput);
        });

    }

    /**<n