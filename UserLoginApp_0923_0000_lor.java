// 代码生成时间: 2025-09-23 00:00:59
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.security.SecurityUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class UserLoginApp {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password123";
    private static final String AUTH_ROUTE = "/login";
    private static final String HOME_ROUTE = "/home";

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        // Define routes
        app.routes(() -> {

            // Login route
            app.post(AUTH_ROUTE, Handler.class.cast(new LoginHandler()));

            // Home route - secured
            app.get(HOME_ROUTE, Handler.class.cast(new HomeHandler()), RouteRole.LOGIN_REQUIRED);
        });
    }
}

class LoginHandler implements Handler {
    @Override
    public void handle(Context ctx) {
        String username = ctx.bodyAsClass(LoginRequest.class).getUsername();
        String password = ctx.bodyAsClass(LoginRequest.class).getPassword();

        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            // Save the username in the session
            ctx.sessionAttribute("username", username);

            // Redirect to home page
            ctx.redirect(HOME_ROUTE);
        } else {
            // Authentication failed
            ctx.status(401);
            ctx.result("Authentication failed");
        }
    }
}

class HomeHandler implements Handler {
    @Override
    public void handle(Context ctx) {
        String username = ctx.sessionAttribute("username");
        if (username != null) {
            ctx.result("Welcome to the home page, " + username + "!");
        } else {
            ctx.status(401);
            ctx.result("Please log in first");
        }
    }
}

class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

// Documentation
/**
 * A Javalin-based user login application.
 * It includes a login route and a home route that requires authentication.
 * The application uses simple in-memory authentication.
 */
