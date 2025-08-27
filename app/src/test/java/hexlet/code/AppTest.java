package hexlet.code;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utils.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class AppTest {
    private static Javalin app;
    private static MockWebServer mockWebServer;

    @BeforeEach
    public final void setup() throws IOException, SQLException {
        app = App.getApp();
        mockWebServer = new MockWebServer();

        Path htmlFixture = Paths.get("src/test/resources/fixtures/test-page.html")
            .toAbsolutePath().normalize();
        MockResponse response = new MockResponse()
            .setBody(Files.readString(htmlFixture)).setResponseCode(200);

        mockWebServer.enqueue(response);
        mockWebServer.start();
    }

    @AfterEach
    public final void shutdown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void getRootPath() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Title");
        });
    }

    @Test
    public void getUrlsPath() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void getUrlChecksPath() {
        JavalinTest.test(app, (server, client) -> {
            var url = "https://www.example.com";

            var response = client.post(NamedRoutes.urlsPath(), url);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
            assertThat(UrlRepository.findByName(url)).isNotNull();
        });
    }

    @Test
    public void getUrlPath() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("https://example.com");
            UrlRepository.save(url);

            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://example.com");
        });
    }

    @Test
    public void postUrlChecksPath() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("https://example.com");
            UrlRepository.save(url);

            var response = client.post(NamedRoutes.urlChecksPath(String.valueOf(url.getId())));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://example.com");

            UrlCheck urlCheck = UrlCheckRepository.findLatestByUrlId(url.getId()).orElseThrow();
            assertThat(urlCheck.getH1()).isEqualTo("Title");
            assertThat(urlCheck.getTitle()).isEqualTo("Head");
            assertThat(urlCheck.getDescription()).isEqualTo("Description");
        });
    }
}
