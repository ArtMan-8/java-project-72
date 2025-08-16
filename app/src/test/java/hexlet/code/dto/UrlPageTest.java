package hexlet.code.dto;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UrlPageTest {
    @Test
    void testUrlPageCreation() {
        var url = new Url("https://example.com");
        var checks = List.of(
            new UrlCheck(200, "Test Title", "Test H1", "Test Description", 1L)
        );

        var page = new UrlPage(url, checks);

        assertThat(page.getUrl()).isEqualTo(url);
        assertThat(page.getChecks()).hasSize(1);
        assertThat(page.getChecks().get(0).getStatusCode()).isEqualTo(200);
        assertThat(page.getChecks().get(0).getTitle()).isEqualTo("Test Title");
    }

    @Test
    void testUrlPageWithEmptyChecks() {
        var url = new Url("https://example.com");
        var checks = List.<UrlCheck>of();

        var page = new UrlPage(url, checks);

        assertThat(page.getUrl()).isEqualTo(url);
        assertThat(page.getChecks()).isEmpty();
    }

    @Test
    void testUrlPageWithMultipleChecks() {
        var url = new Url("https://example.com");
        var checks = List.of(
            new UrlCheck(200, "First Title", "First H1", "First Description", 1L),
            new UrlCheck(404, "Second Title", "Second H1", "Second Description", 1L)
        );

        var page = new UrlPage(url, checks);

        assertThat(page.getChecks()).hasSize(2);
        assertThat(page.getChecks().get(0).getStatusCode()).isEqualTo(200);
        assertThat(page.getChecks().get(1).getStatusCode()).isEqualTo(404);
    }
}
