package hexlet.code;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.watcher.DirectoryWatcher;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

@Slf4j
public class App {
    public static void main(String[] args) {
        var app = getApp();
        app.start(getAppPort());
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get("/", ctx -> {
            ctx.render("index.jte");
        });
        return app;
    }

    private static void initDB() {
        try (HikariDataSource ds = new HikariDataSource()) {
            ds.setJdbcUrl(getDBUrl());
        }
    }

    private static String getDBUrl() {
        return System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

    private static int getAppPort() {
        String port = System.getenv()
            .getOrDefault("PORT", "7000");
        return Integer.parseInt(port);
    }

    private static TemplateEngine createTemplateEngine() {
        Path path = Path.of("app", "src", "main", "resources", "templates");
        DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(path);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);

        DirectoryWatcher watcher = new DirectoryWatcher(templateEngine, codeResolver);
        watcher.start(templates -> { });

        return templateEngine;
    }
}
