// 代码生成时间: 2025-09-23 20:24:11
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Context;
import java.util.HashMap;
# NOTE: 重要实现细节
import java.util.Map;

/**
 * MathTool provides a REST API for basic mathematical operations.
 */
public class MathTool {

    private static final String API_PATH = "/math";

    /**
     * Starts the Javalin server with the defined API endpoints.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);

        app.routes(() -> {
# TODO: 优化性能
            // Addition endpoint
            ApiBuilder.get(API_PATH + "/add", MathTool::handleAddition);

            // Subtraction endpoint
            ApiBuilder.get(API_PATH + "/subtract", MathTool::handleSubtraction);

            // Multiplication endpoint
            ApiBuilder.get(API_PATH + "/multiply", MathTool::handleMultiplication);

            // Division endpoint
            ApiBuilder.get(API_PATH + "/divide", MathTool::handleDivision);
        });
    }

    /**
     * Handles addition requests.
     * @param ctx the context of the request.
     */
    private static void handleAddition(Context ctx) {
        Map<String, Integer> params = getQueryParams(ctx);
        int sum = params.getOrDefault("a", 0) + params.getOrDefault("b", 0);
        ctx.json(Map.of("result", sum));
# NOTE: 重要实现细节
    }

    /**
     * Handles subtraction requests.
     * @param ctx the context of the request.
# 优化算法效率
     */
    private static void handleSubtraction(Context ctx) {
        Map<String, Integer> params = getQueryParams(ctx);
        int result = params.getOrDefault("a", 0) - params.getOrDefault("b", 0);
        ctx.json(Map.of("result", result));
    }

    /**
     * Handles multiplication requests.
     * @param ctx the context of the request.
     */
    private static void handleMultiplication(Context ctx) {
        Map<String, Integer> params = getQueryParams(ctx);
        int product = params.getOrDefault("a", 1) * params.getOrDefault("b", 1);
# 优化算法效率
        ctx.json(Map.of("result", product));
    }

    /**
     * Handles division requests.
     * @param ctx the context of the request.
     */
# TODO: 优化性能
    private static void handleDivision(Context ctx) {
        Map<String, Integer> params = getQueryParams(ctx);
        try {
            int a = params.get("a");
            int b = params.get("b");
            if (b == 0) {
# 增强安全性
                throw new ArithmeticException("Cannot divide by zero.");
            }
            ctx.json(Map.of("result", a / b));
        } catch (ArithmeticException e) {
            ctx.status(400).json(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Extracts query parameters from the request.
     * @param ctx the context of the request.
     * @return a map of query parameters.
     */
    private static Map<String, Integer> getQueryParams(Context ctx) {
        Map<String, Integer> params = new HashMap<>();
        params.put("a", Integer.parseInt(ctx.queryParam("a")));
        params.put("b", Integer.parseInt(ctx.queryParam("b")));
        return params;
    }
}
# FIXME: 处理边界情况
