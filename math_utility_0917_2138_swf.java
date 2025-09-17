// 代码生成时间: 2025-09-17 21:38:55
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.plugin.json.JavalinJackson;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MathUtility {
    // 定义Javalin服务器
    private static Javalin app = Javalin.create()
        .configure(JavalinJackson.create())
        .start(7000);

    // 数学运算方法集合
    private static final Map<String, Function<JSONObject, JSONObject>> operations = new HashMap<>();

    static {
        // 初始化数学运算方法
        operations.put("add", MathUtility::add);
        operations.put("subtract", MathUtility::subtract);
        operations.put("multiply", MathUtility::multiply);
        operations.put("divide", MathUtility::divide);
    }

    // 加法运算
    private static JSONObject add(JSONObject input) {
        int a = input.getInt("a");
        int b = input.getInt("b");
        return new JSONObject().put("result", a + b);
    }

    // 减法运算
    private static JSONObject subtract(JSONObject input) {
        int a = input.getInt("a");
        int b = input.getInt("b");
        return new JSONObject().put("result", a - b);
    }

    // 乘法运算
    private static JSONObject multiply(JSONObject input) {
        int a = input.getInt("a");
        int b = input.getInt("b");
        return new JSONObject().put("result", a * b);
    }

    // 除法运算
    private static JSONObject divide(JSONObject input) {
        int a = input.getInt("a");
        int b = input.getInt("b");
        if (b == 0) {
            return new JSONObject().put("error", "Cannot divide by zero");
        }
        return new JSONObject().put("result", a / b);
    }

    // 定义API路由
    public static void defineRoutes() {
        // 为每种运算定义一个GET请求
        ApiBuilder.get("/:operation", ctx -> {
            String operation = ctx.pathParam("operation");
            JSONObject input = ctx.bodyAsClass(JSONObject.class);

            if (operations.containsKey(operation)) {
                JSONObject result = operations.get(operation).apply(input);
                ctx.json(result);
            } else {
                ctx.status(404).json(new JSONObject().put("error", "Operation not found"));
            }
        });
    }

    // 程序入口点
    public static void main(String[] args) {
        defineRoutes();
        System.out.println("Math Utility server is running on http://localhost:7000");
    }
}
