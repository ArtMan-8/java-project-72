package hexlet.code.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.anyString;

import hexlet.code.dto.BasePage;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class FlashMessagesTest {
    private Context mockContext;
    private BasePage mockPage;

    @BeforeEach
    public final void setUp() {
        mockContext = mock(Context.class);
        mockPage = mock(BasePage.class);
    }

    @Test
    public void testConstants() {
        assertThat(FlashMessages.TYPE_KEY).isEqualTo("flashType");
        assertThat(FlashMessages.MESSAGE_KEY).isEqualTo("flashMessage");
        assertThat(FlashMessages.ERROR).isEqualTo("error");
        assertThat(FlashMessages.SUCCESS).isEqualTo("success");
        assertThat(FlashMessages.INFO).isEqualTo("info");
    }

    @Test
    public void testClearFlashMessages() {
        FlashMessages.clearFlashMessages(mockContext);

        verify(mockContext).sessionAttribute(FlashMessages.MESSAGE_KEY, null);
        verify(mockContext).sessionAttribute(FlashMessages.TYPE_KEY, null);
    }

    @Test
    public void testSetFlashToPage() {
        when(mockContext.sessionAttribute(FlashMessages.MESSAGE_KEY)).thenReturn("Тест");
        when(mockContext.sessionAttribute(FlashMessages.TYPE_KEY)).thenReturn(FlashMessages.SUCCESS);

        FlashMessages.setFlashToPage(mockContext, mockPage);

        verify(mockPage).setFlashMessage("Тест");
        verify(mockPage).setFlashType(FlashMessages.SUCCESS);
        verify(mockContext).sessionAttribute(FlashMessages.MESSAGE_KEY, null);
        verify(mockContext).sessionAttribute(FlashMessages.TYPE_KEY, null);
    }

    @Test
    public void testSetFlashToPageWithoutType() {
        when(mockContext.sessionAttribute(FlashMessages.MESSAGE_KEY)).thenReturn("Тест");
        when(mockContext.sessionAttribute(FlashMessages.TYPE_KEY)).thenReturn(null);

        FlashMessages.setFlashToPage(mockContext, mockPage);

        verify(mockPage).setFlashType(FlashMessages.INFO);
    }

    @Test
    public void testSetFlashToPageWithoutMessage() {
        when(mockContext.sessionAttribute(FlashMessages.MESSAGE_KEY)).thenReturn(null);

        FlashMessages.setFlashToPage(mockContext, mockPage);

        verify(mockPage, never()).setFlashMessage(anyString());
        verify(mockPage, never()).setFlashType(anyString());
    }
}
