package hexlet.code.service;

import hexlet.code.model.UrlCheck;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UrlCheckServiceTest {
    @Test
    void testUrlCheckCreation() {
        var urlCheck = new UrlCheck(200, "Test Title", "Test H1", "Test Description", 1L);

        assertThat(urlCheck.getStatusCode()).isEqualTo(200);
        assertThat(urlCheck.getTitle()).isEqualTo("Test Title");
        assertThat(urlCheck.getH1()).isEqualTo("Test H1");
        assertThat(urlCheck.getDescription()).isEqualTo("Test Description");
        assertThat(urlCheck.getUrlId()).isEqualTo(1L);
    }

    @Test
    void testUrlCheckWithNullValues() {
        var urlCheck = new UrlCheck(404, null, null, null, 1L);

        assertThat(urlCheck.getStatusCode()).isEqualTo(404);
        assertThat(urlCheck.getTitle()).isNull();
        assertThat(urlCheck.getH1()).isNull();
        assertThat(urlCheck.getDescription()).isNull();
        assertThat(urlCheck.getUrlId()).isEqualTo(1L);
    }
}
