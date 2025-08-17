package hexlet.code.utils;

import io.javalin.http.Context;
import hexlet.code.dto.BasePage;

public class FlashMessages {
    public static final String TYPE_KEY = "flashType";
    public static final String ERROR = "error";
    public static final String SUCCESS = "success";
    public static final String INFO = "info";

    public static final String MESSAGE_KEY = "flashMessage";
    public static final String URL_CREATED = "Страница успешно добавлена";
    public static final String URL_NOT_FOUND = "Страница не найдена";
    public static final String URL_ALREADY_EXISTS = "Страница уже существует";
    public static final String URL_EMPTY = "URL не может быть пустым";
    public static final String URL_ERROR = "Ошибка при добавлении страницы";
    public static final String URL_INVALID = "Некорректный URL";
    public static final String URL_CHECK_SUCCESS = "Сайт успешно проверен";
    public static final String URL_CHECK_ERROR = "Ошибка при проверке сайта";

    public static void clearFlashMessages(Context ctx) {
        ctx.sessionAttribute(MESSAGE_KEY, null);
        ctx.sessionAttribute(TYPE_KEY, null);
    }

    public static void setFlashToPage(Context ctx, BasePage page) {
        var flashMessage = ctx.sessionAttribute(MESSAGE_KEY);
        var flashType = ctx.sessionAttribute(TYPE_KEY);
        if (flashMessage != null) {
            page.setFlashMessage(flashMessage.toString());
            page.setFlashType(flashType != null ? flashType.toString() : INFO);
            clearFlashMessages(ctx);
        }
    }
}
