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

import java.sql.SQLException;
import java.util.Optional;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlRepository;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.utils.FlashMessages;
import kong.unirest.core.UnirestException;
import kong.unirest.core.Unirest;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UrlCheckControllerTest {
    private Context ctx;
    private Url mockUrl;
    private Validator<String> validator;

    @BeforeEach
    public final void setUp() {
        ctx = mock(Context.class);
        mockUrl = new Url("https://example.com");
        mockUrl.setId(1L);
        validator = mock(Validator.class);
        when(validator.get()).thenReturn("1");
        when(ctx.pathParamAsClass("id", String.class)).thenReturn(validator);
    }

    @Test
    public void testCheckSuccess() throws SQLException {
        try (MockedStatic<UrlRepository> mockedUrlRepo = mockStatic(UrlRepository.class);
             MockedStatic<UrlCheckRepository> mockedUrlCheckRepo = mockStatic(UrlCheckRepository.class)) {

            mockedUrlRepo.when(() -> UrlRepository.findById(1L))
                .thenReturn(Optional.of(mockUrl));
            mockedUrlCheckRepo.when(() -> UrlCheckRepository.save(any(UrlCheck.class)))
                .thenAnswer(invocation -> null);

            UrlCheckController.check(ctx);

            verify(ctx).sessionAttribute(FlashMessages.MESSAGE_KEY, FlashMessages.URL_CHECK_SUCCESS);
            verify(ctx).sessionAttribute(FlashMessages.TYPE_KEY, FlashMessages.SUCCESS);
            verify(ctx).redirect(eq("/urls/1"));
        }
    }

    @Test
    public void testCheckUrlNotFound() throws SQLException {
        try (MockedStatic<UrlRepository> mockedUrlRepo = mockStatic(UrlRepository.class)) {
            when(validator.get()).thenReturn("999");
            mockedUrlRepo.when(() -> UrlRepository.findById(999L))
                .thenReturn(Optional.empty());

            assertThrows(NotFoundResponse.class, () -> {
                UrlCheckController.check(ctx);
            });
        }
    }

    @Test
    public void testCheckUnirestException() throws SQLException {
        try (MockedStatic<UrlRepository> mockedUrlRepo = mockStatic(UrlRepository.class);
             MockedStatic<UrlCheckRepository> mockedUrlCheckRepo = mockStatic(UrlCheckRepository.class)) {

            mockedUrlRepo.when(() -> UrlRepository.findById(1L)).thenReturn(Optional.of(mockUrl));
            mockedUrlCheckRepo.when(() -> UrlCheckRepository.save(any(UrlCheck.class))).thenAnswer(invocation -> null);

            try (MockedStatic<Unirest> mockedUnirest = mockStatic(Unirest.class)) {
                mockedUnirest.when(() -> Unirest.get(any(String.class)))
                    .thenThrow(new UnirestException("Connection failed"));

                UrlCheckController.check(ctx);

                verify(ctx).sessionAttribute(FlashMessages.MESSAGE_KEY, FlashMessages.URL_CHECK_ERROR);
                verify(ctx).sessionAttribute(FlashMessages.TYPE_KEY, FlashMessages.ERROR);
                verify(ctx).redirect(eq("/urls/1"));
            }
        }
    }

    @Test
    public void testCheckGeneralException() throws SQLException {
        try (MockedStatic<UrlRepository> mockedUrlRepo = mockStatic(UrlRepository.class);
             MockedStatic<UrlCheckRepository> mockedUrlCheckRepo = mockStatic(UrlCheckRepository.class)) {

            mockedUrlRepo.when(() -> UrlRepository.findById(1L))
                .thenReturn(Optional.of(mockUrl));
            mockedUrlCheckRepo.when(() -> UrlCheckRepository.save(any(UrlCheck.class)))
                .thenThrow(new RuntimeException("Test error"));

            UrlCheckController.check(ctx);

            verify(ctx).sessionAttribute(FlashMessages.MESSAGE_KEY, FlashMessages.URL_CHECK_ERROR);
            verify(ctx).sessionAttribute(FlashMessages.TYPE_KEY, FlashMessages.ERROR);
            verify(ctx).redirect(eq("/urls/1"));
        }
    }
}
