package hexlet.code.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.ArrayList;

import hexlet.code.model.Url;

public final class UrlsPageTest {

    @Test
    void testUrlsPageCreation() {
        var urls = List.of(
            new Url("https://example.com"),
            new Url("https://test.com")
        );
        var page = new UrlsPage(urls);
        assertNotNull(page);
        assertEquals(urls, page.getUrls());
    }

    @Test
    void testUrlsPageInheritance() {
        var urls = new ArrayList<Url>();
        var page = new UrlsPage(urls);
        assertTrue(page instanceof BasePage);
    }

    @Test
    void testUrlsPageUrlsSetter() {
        var urls = new ArrayList<Url>();
        var page = new UrlsPage(urls);

        var newUrls = List.of(new Url("https://new.com"));
        page.setUrls(newUrls);
        assertEquals(newUrls, page.getUrls());
    }

    @Test
    void testUrlsPageEmptyList() {
        var urls = new ArrayList<Url>();
        var page = new UrlsPage(urls);
        assertTrue(page.getUrls().isEmpty());
    }

    @Test
    void testUrlsPageNullUrls() {
        var page = new UrlsPage(null);
        assertNull(page.getUrls());
    }
}
