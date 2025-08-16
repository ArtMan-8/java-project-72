package hexlet.code.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import hexlet.code.model.Url;

public final class UrlPageTest {

    @Test
    void testUrlPageCreation() {
        var url = new Url("https://example.com");
        var page = new UrlPage(url);
        assertNotNull(page);
        assertEquals(url, page.getUrl());
    }

    @Test
    void testUrlPageInheritance() {
        var url = new Url("https://example.com");
        var page = new UrlPage(url);
        assertTrue(page instanceof BasePage);
    }

    @Test
    void testUrlPageUrlSetter() {
        var url = new Url("https://example.com");
        var page = new UrlPage(url);

        var newUrl = new Url("https://test.com");
        page.setUrl(newUrl);
        assertEquals(newUrl, page.getUrl());
    }

    @Test
    void testUrlPageUrlNull() {
        var page = new UrlPage(null);
        assertNull(page.getUrl());
    }
}
