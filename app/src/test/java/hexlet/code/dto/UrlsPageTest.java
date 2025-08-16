package hexlet.code.dto;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UrlsPageTest {
    @Test
    void testUrlsPageCreation() {
        var urls = List.of(
            new Url("https://example.com"),
            new Url("https://test.com")
        );

        Map<Long, UrlCheck> latestChecks = new HashMap<>();
        latestChecks.put(1L, null);
        latestChecks.put(2L, null);

        var page = new UrlsPage(urls, latestChecks);

        assertThat(page.getUrls()).hasSize(2);
        assertThat(page.getLatestChecks()).hasSize(2);
        assertThat(page.getUrls().get(0).getName()).isEqualTo("https://example.com");
        assertThat(page.getUrls().get(1).getName()).isEqualTo("https://test.com");
    }

    @Test
    void testUrlsPageWithChecks() {
        var urls = List.of(new Url("https://example.com"));
        urls.get(0).setId(1L);

        var check = new UrlCheck(200, "Test Title", "Test H1", "Test Description", 1L);
        Map<Long, UrlCheck> latestChecks = new HashMap<>();
        latestChecks.put(1L, check);

        var page = new UrlsPage(urls, latestChecks);

        assertThat(page.getLatestChecks().get(1L).getStatusCode()).isEqualTo(200);
        assertThat(page.getLatestChecks().get(1L).getTitle()).isEqualTo("Test Title");
    }
}
