package hexlet.code.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public final class BasePageTest {

    @Test
    void testBasePageCreation() {
        var page = new BasePage();
        assertNotNull(page);
    }

    @Test
    void testFlashMessage() {
        var page = new BasePage();
        page.setFlashMessage("Test message");
        assertEquals("Test message", page.getFlashMessage());
    }

    @Test
    void testFlashType() {
        var page = new BasePage();
        page.setFlashType("success");
        assertEquals("success", page.getFlashType());
    }

    @Test
    void testFlashMessageNull() {
        var page = new BasePage();
        page.setFlashMessage(null);
        assertNull(page.getFlashMessage());
    }

    @Test
    void testFlashTypeNull() {
        var page = new BasePage();
        page.setFlashType(null);
        assertNull(page.getFlashType());
    }
}
