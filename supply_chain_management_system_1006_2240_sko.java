// 代码生成时间: 2025-10-06 22:40:36
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.Handler;
import java.util.HashMap;
import java.util.Map;

public class SupplyChainManagementSystem {

    private static final Javalin app = Javalin.create().start(7000);
    private static final Map<String, String> supplyChainMap = new HashMap<>();

    public static void main(String[] args) {
        // Define exception handler
        app.exception(ExceptionHandler.of(throwable -> {
            throwable.printStackTrace();
            Context ctx = Javalin.servletRequestContext();
            ctx.status(500).result("Internal Server Error");
        }));

        // Define routes
        setupRoutes();

        // Print route overview
        app.routes(() -> RouteOverviewPlugin.overview("/"));
    }

    // Sets up all the routes for the application
    private static void setupRoutes() {
        app.get("/", ctx -> ctx.result("Welcome to the Supply Chain Management System"));

        // Add a new supply chain
        app.post("/supply-chain", ctx -> {
            String name = ctx.bodyAsClass(AddSupplyChainRequest.class).getName();
            supplyChainMap.put(name, "new");
            ctx.status(201).result("Supply chain added: " + name);
        });

        // Get all supply chains
        app.get("/supply-chains", ctx -> ctx.json(supplyChainMap));

        // Update a supply chain
        app.put("/supply-chain/{name}", ctx -> {
            String name = ctx.pathParam("name");
            if (supplyChainMap.containsKey(name)) {
                supplyChainMap.put(name, "updated");
                ctx.status(200).result("Supply chain updated: " + name);
            } else {
                ctx.status(404).result("Supply chain not found");
            }
        });

        // Delete a supply chain
        app.delete("/supply-chain/{name}", ctx -> {
            String name = ctx.pathParam("name");
            if (supplyChainMap.remove(name) != null) {
                ctx.status(200).result("Supply chain deleted: " + name);
            } else {
                ctx.status(404).result("Supply chain not found");
            }
        });
    }

    // Request class for adding a new supply chain
    static class AddSupplyChainRequest {
        private String name;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
