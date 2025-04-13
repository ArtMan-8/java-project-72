package hexlet.code;

import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) {
        var app = getApp();
        app.start(getAppPort());
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("Hello World!"));
        return app;
    }

    private static void initDB() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(getDBUrl());
    }

    private static int getAppPort() {
        String port = System.getenv()
            .getOrDefault("PORT", "7000");
        return Integer.parseInt(port);
    }

    private static String getDBUrl() {
        return System.getenv()
            .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }
}
