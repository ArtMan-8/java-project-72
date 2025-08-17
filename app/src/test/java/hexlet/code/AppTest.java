package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public final class AppTest {
    private static Javalin app;
    private static MockWebServer mockWebServer;

    @BeforeAll
    public static void beforeAll() throws SQLException, IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    public void beforeEach() throws SQLException, IOException {
        app = App.getApp();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testRoot() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertStatusCode(response, 200);
            assertPageContains(response.body().string(), "Анализатор страниц");
        });
    }

    @Test
    public void testUrlsIndex() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertStatusCode(response, 200);
            assertPageContains(response.body().string(), "Сайты");
        });
    }

    @Test
    public void testUrlCreate() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=https://example.com");
            assertPostRedirect(response, "/urls");

            assertUrlCreated("https://example.com", 1);

            var urlsResponse = client.get("/urls");
            assertUrlDisplayedOnPage("https://example.com", urlsResponse.body().string());
        });
    }

    @Test
    public void testUrlCreateWithPort() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=https://example.com:8080");
            assertPostRedirect(response, "/urls");

            assertUrlCreated("https://example.com:8080", 1);
        });
    }

    @Test
    public void testUrlCreateInvalidUrl() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=invalid-url");
            assertPostRedirect(response, "/");
        });
    }

    @Test
    public void testUrlCreateEmptyUrl() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=");
            assertPostRedirect(response, "/");
        });
    }

    @Test
    public void testUrlCreateDuplicate() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=https://example.com");

            var response = client.post("/urls", "url=https://example.com");
            assertPostRedirect(response, "/urls");

            assertUrlCreated("https://example.com", 1);
        });
    }

    @Test
    public void testUrlShow() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("https://example.com");
            UrlRepository.save(url);

            var response = client.get("/urls/1");
            assertStatusCode(response, 200);
            assertPageContains(response.body().string(), "https://example.com");
        });
    }

    @Test
    public void testUrlShowNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999");
            assertStatusCode(response, 404);
        });
    }

    @Test
    public void testUrlCheck() throws SQLException, IOException {
        JavalinTest.test(app, (server, client) -> {
            var mockUrl = "http://localhost:" + mockWebServer.getPort();
            var url = new Url(mockUrl);
            UrlRepository.save(url);

            var htmlContent = readFixture("test-page.html");
            mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(htmlContent));


            var response = client.post("/urls/1/checks");
            assertPostRedirect(response, "/urls/1");
            assertUrlCheckSaved(1L, 200, "Test Title", "Test H1", "Test Description");

            var showResponse = client.get("/urls/1");
            var pageContent = showResponse.body().string();
            assertPageContains(pageContent, "200");
            assertPageContains(pageContent, "Test Title");
            assertPageContains(pageContent, "Test H1");
            assertPageContains(pageContent, "Test Description");
        });
    }

    @Test
    public void testUrlCheckNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls/999/checks");
            assertStatusCode(response, 404);
        });
    }

    @Test
    public void testUrlCheckConnectionError() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("http://localhost:99999");
            UrlRepository.save(url);

            var response = client.post("/urls/1/checks");
            assertPostRedirect(response, "/urls/1");

            var checks = UrlCheckRepository.findByUrlId(1L);
            assertThat(checks).hasSize(1);
            assertThat(checks.get(0).getStatusCode()).isEqualTo(500);
            assertThat(checks.get(0).getDescription()).contains("Ошибка подключения");
        });
    }

    private void assertUrlCreated(String url, int expectedCount) throws SQLException {
        var urls = UrlRepository.getEntities();
        assertThat(urls).hasSize(expectedCount);
        assertThat(urls.stream().anyMatch(u -> u.getName().equals(url))).isTrue();
    }

    private void assertUrlDisplayedOnPage(String url, String pageContent) {
        assertThat(pageContent).contains(url);
    }

    private void assertPostRedirect(Response response, String expectedLocation) {
        assertThat(response.code()).isEqualTo(200);
    }

    private void assertStatusCode(Response response, int expectedCode) {
        assertThat(response.code()).isEqualTo(expectedCode);
    }

    private void assertPageContains(String pageContent, String expectedText) {
        assertThat(pageContent).contains(expectedText);
    }

    private String readFixture(String filename) throws IOException {
        return Files.readString(Path.of("src/test/resources/fixtures", filename));
    }

    private void assertUrlCheckSaved(
        Long urlId,
        int expectedStatusCode,
        String expectedTitle,
        String expectedH1,
        String expectedDescription
    ) throws SQLException {
        var checks = UrlCheckRepository.findByUrlId(urlId);
        assertThat(checks).hasSize(1);
        assertThat(checks.get(0).getStatusCode()).isEqualTo(expectedStatusCode);
        assertThat(checks.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(checks.get(0).getH1()).isEqualTo(expectedH1);
        assertThat(checks.get(0).getDescription()).isEqualTo(expectedDescription);
    }
}
