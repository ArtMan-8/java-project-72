package hexlet.code.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;

public final class UrlsControllerTest {

    @Test
    void testUrlCreation() {
        var url = new Url("https://example.com");
        assertNotNull(url);
        assertEquals("https://example.com", url.getName());
    }

    @Test
    void testUrlPageCreation() {
        var url = new Url("https://example.com");
        var page = new UrlPage(url);
        assertNotNull(page);
        assertEquals(url, page.getUrl());
    }

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
    void testUrlModelProperties() {
        var url = new Url("https://example.com");
        url.setId(1L);
        url.setCreatedAt(java.sql.Timestamp.valueOf("2024-01-01 00:00:00"));

        assertEquals(1L, url.getId());
        assertEquals("https://example.com", url.getName());
        assertNotNull(url.getCreatedAt());
    }
}
