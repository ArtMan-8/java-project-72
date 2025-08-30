package hexlet.code.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.Validator;

import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.utils.FlashMessages;

public class UrlsControllerTest {
    private Context ctx;

    @BeforeEach
    public final void setUp() {
        ctx = mock(Context.class);
    }

    @Test
    public void testIndex() throws SQLException {
        try (MockedStatic<UrlRepository> mockedUrlRepo = mockStatic(UrlRepository.class);
             MockedStatic<UrlCheckRepository> mockedUrlCheckRepo = mockStatic(UrlCheckRepository.class)) {

            mockedUrlRepo.when(UrlRepository::getEntities).thenReturn(List.of());
            mockedUrlCheckRepo.when(UrlCheckRepository::findLatestChecks).thenReturn(Map.of());

            UrlsController.index(ctx);
            verify(ctx).render(eq("urls/index.jte"), any());
        }
    }

    @Test
    public void testShow() throws SQLException {
        Url mockUrl = new Url("https://example.com");
        mockUrl.setId(1L);

        try (MockedStatic<UrlRepository> mockedUrlRepo = mockStatic(UrlRepository.class);
             MockedStatic<UrlCheckRepository> mockedUrlCheckRepo = mockStatic(UrlCheckRepository.class)) {

            Validator<String> validator = mock(Validator.class);
            when(validator.get()).thenReturn("1");
            when(ctx.pathParamAsClass("id", String.class)).thenReturn(validator);
            mockedUrlRepo.when(() -> UrlRepository.findById(1L)).thenReturn(Optional.of(mockUrl));
            mockedUrlCheckRepo.when(() -> UrlCheckRepository.findByUrlId(1L)).thenReturn(List.of());

            UrlsController.show(ctx);
            verify(ctx).render(eq("urls/show.jte"), any());
        }
    }

    @Test
    public void testShowNotFound() throws SQLException {
        try (MockedStatic<UrlRepository> mockedUrlRepo = mockStatic(UrlRepository.class)) {
            Validator<String> validator = mock(Validator.class);
            when(validator.get()).thenReturn("999");
            when(ctx.pathParamAsClass("id", String.class)).thenReturn(validator);
            mockedUrlRepo.when(() -> UrlRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(NotFoundResponse.class, () -> UrlsController.show(ctx));
        }
    }

    @Test
    public void testCreate() throws SQLException {
        try (MockedStatic<UrlRepository> mockedUrlRepo = mockStatic(UrlRepository.class)) {
            when(ctx.formParam("url")).thenReturn("https://example.com");
            mockedUrlRepo.when(() -> UrlRepository.findByName("https://example.com")).thenReturn(Optional.empty());
            mockedUrlRepo.when(() -> UrlRepository.save(any(Url.class))).thenAnswer(invocation -> null);

            UrlsController.create(ctx);
            verify(ctx).redirect(eq("/urls"));
            verify(ctx).sessionAttribute(eq(FlashMessages.MESSAGE_KEY), eq(FlashMessages.URL_CREATED));
            verify(ctx).sessionAttribute(eq(FlashMessages.TYPE_KEY), eq(FlashMessages.SUCCESS));
        }
    }

    @Test
    public void testCreateEmptyUrl() throws SQLException {
        when(ctx.formParam("url")).thenReturn("");

        UrlsController.create(ctx);
        verify(ctx).redirect(eq("/"));
        verify(ctx).sessionAttribute(eq(FlashMessages.MESSAGE_KEY), eq(FlashMessages.URL_EMPTY));
        verify(ctx).sessionAttribute(eq(FlashMessages.TYPE_KEY), eq(FlashMessages.ERROR));
    }

    @Test
    public void testCreateNullUrl() throws SQLException {
        when(ctx.formParam("url")).thenReturn(null);

        UrlsController.create(ctx);
        verify(ctx).redirect(eq("/"));
        verify(ctx).sessionAttribute(eq(FlashMessages.MESSAGE_KEY), eq(FlashMessages.URL_EMPTY));
        verify(ctx).sessionAttribute(eq(FlashMessages.TYPE_KEY), eq(FlashMessages.ERROR));
    }

    @Test
    public void testCreateInvalidUrl() throws SQLException {
        when(ctx.formParam("url")).thenReturn("invalid-url");

        UrlsController.create(ctx);
        verify(ctx).redirect(eq("/"));
        verify(ctx).sessionAttribute(eq(FlashMessages.MESSAGE_KEY), eq(FlashMessages.URL_INVALID));
        verify(ctx).sessionAttribute(eq(FlashMessages.TYPE_KEY), eq(FlashMessages.ERROR));
    }

    @Test
    public void testCreateExistingUrl() throws SQLException {
        Url existingUrl = new Url("https://example.com");
        existingUrl.setId(1L);

        try (MockedStatic<UrlRepository> mockedUrlRepo = mockStatic(UrlRepository.class)) {
            when(ctx.formParam("url")).thenReturn("https://example.com");
            mockedUrlRepo.when(() -> UrlRepository.findByName("https://example.com"))
                .thenReturn(Optional.of(existingUrl));

            UrlsController.create(ctx);
            verify(ctx).redirect(eq("/urls"));
            verify(ctx).sessionAttribute(eq(FlashMessages.MESSAGE_KEY), eq(FlashMessages.URL_ALREADY_EXISTS));
            verify(ctx).sessionAttribute(eq(FlashMessages.TYPE_KEY), eq(FlashMessages.INFO));
        }
    }

    @Test
    public void testCreateWithPort() throws SQLException {
        try (MockedStatic<UrlRepository> mockedUrlRepo = mockStatic(UrlRepository.class)) {
            when(ctx.formParam("url")).thenReturn("https://example.com:8080/path");
            mockedUrlRepo.when(() -> UrlRepository.findByName("https://example.com:8080"))
                .thenReturn(Optional.empty());
            mockedUrlRepo.when(() -> UrlRepository.save(any(Url.class)))
                .thenAnswer(invocation -> null);

            UrlsController.create(ctx);
            verify(ctx).redirect(eq("/urls"));
            verify(ctx).sessionAttribute(eq(FlashMessages.MESSAGE_KEY), eq(FlashMessages.URL_CREATED));
            verify(ctx).sessionAttribute(eq(FlashMessages.TYPE_KEY), eq(FlashMessages.SUCCESS));
        }
    }

    @Test
    public void testCreateWithSQLException() throws SQLException {
        try (MockedStatic<UrlRepository> mockedUrlRepo = mockStatic(UrlRepository.class)) {
            when(ctx.formParam("url")).thenReturn("https://example.com");
            mockedUrlRepo.when(() -> UrlRepository.findByName("https://example.com"))
                .thenReturn(Optional.empty());
            mockedUrlRepo.when(() -> UrlRepository.save(any(Url.class)))
                .thenThrow(new SQLException("Database error"));

            UrlsController.create(ctx);
            verify(ctx).redirect(eq("/"));
            verify(ctx).sessionAttribute(eq(FlashMessages.MESSAGE_KEY), eq(FlashMessages.URL_ERROR));
            verify(ctx).sessionAttribute(eq(FlashMessages.TYPE_KEY), eq(FlashMessages.ERROR));
        }
    }
}
