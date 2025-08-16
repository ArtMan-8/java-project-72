package hexlet.code.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hexlet.code.dto.BasePage;

public final class RootControllerTest {
    @Test
    void testBasePageCreation() {
        var page = new BasePage();
        assertNotNull(page);
    }

    @Test
    void testBasePageType() {
        var page = new BasePage();
        assertTrue(page instanceof BasePage);
    }
}
