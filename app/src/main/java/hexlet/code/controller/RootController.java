package hexlet.code.controller;

import static io.javalin.rendering.template.TemplateUtil.model;
import io.javalin.http.Context;

import hexlet.code.dto.BasePage;
import hexlet.code.util.FlashMessages;

public class RootController {
    public static void index(Context ctx) {
        var page = new BasePage();

        FlashMessages.setFlashToPage(ctx, page);

        ctx.render("index.jte", model("page", page));
    }
}
