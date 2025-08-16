package hexlet.code;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.watcher.DirectoryWatcher;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import hexlet.code.util.NamedRoutes;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlsController;
import hexlet.code.repository.BaseRepository;

@Slf4j
public class App {
    public static void main(String[] args) throws SQLException, IOException {
        var app = getApp();
        app.start(getAppPort());
    }

    public static Javalin getApp() throws SQLException, IOException {
        initDB();

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get(NamedRoutes.rootPath(), RootController::index);

        app.get(NamedRoutes.urlsPath(), UrlsController::index);

        app.post(NamedRoutes.urlsPath(),  UrlsController::create);

        app.get(NamedRoutes.urlPath("{id}"), UrlsController::show);

        app.post(NamedRoutes.urlChecksPath("{id}"), UrlsController::check);

        return app;
    }

    private static int getAppPort() {
        String port = System.getenv()
            .getOrDefault("PORT", "7000");
        return Integer.parseInt(port);
    }

    private static void initDB() throws SQLException, IOException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(getDBUrl());

        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute(readSchemaDB());
        } catch (SQLException error) {
            throw new SQLException("Ошибка инициализации базы данных", error);
        }

        BaseRepository.setDataSource(dataSource);
    }

    private static String getDBUrl() {
        return System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

    private static String readSchemaDB() throws IOException {
        try (var stream = App.class.getResourceAsStream("/schema.sql")) {
            return new BufferedReader(new InputStreamReader(stream))
                .lines()
                .collect(Collectors.joining());
        }
    }

    private static TemplateEngine createTemplateEngine() {
        Path path = Path.of("src", "main", "resources", "templates");
        DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(path);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);

        if (System.getenv("CI") == null) {
            DirectoryWatcher watcher = new DirectoryWatcher(templateEngine, codeResolver);
            watcher.start(templates -> { });
        }

        return templateEngine;
    }
}
