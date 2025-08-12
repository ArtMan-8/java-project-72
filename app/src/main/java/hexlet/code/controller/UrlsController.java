package hexlet.code.controller;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import static io.javalin.rendering.template.TemplateUtil.model;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;

public class UrlsController {
    public static void index(Context ctx) {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) {
        var urlId = ctx.pathParamAsClass("id", String.class).get();
        var url = UrlRepository.getById(Long.valueOf(urlId));

        if (url != null) {
            var page = new UrlPage(url);
            ctx.render("urls/show.jte", model("page", page));
        } else {
            throw new NotFoundResponse("Url with id=" + urlId + " not found");
        }
    }

    public static void create(Context ctx) {
        var url = ctx.formParam("url");
        if (!UrlRepository.existsByUrl(url)) {
            var newUrl = new Url(url);
            UrlRepository.save(newUrl);
        }
    }
}
