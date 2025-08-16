package hexlet.code.controller;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import static io.javalin.rendering.template.TemplateUtil.model;

import java.net.URI;
import java.net.URL;
import java.sql.SQLException;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.FlashMassages;
import hexlet.code.util.NamedRoutes;

public class UrlsController {
    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);

        FlashMassages.setFlashToPage(ctx, page);

        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", String.class).get();
        var url = UrlRepository.findById(Long.valueOf(urlId));

        if (url.isPresent()) {
            var page = new UrlPage(url.get());

            FlashMassages.setFlashToPage(ctx, page);

            ctx.render("urls/show.jte", model("page", page));
        } else {
            throw new NotFoundResponse("Url with id=" + urlId + " not found");
        }
    }

    public static void create(Context ctx) throws SQLException {
        var inputUrl = ctx.formParam("url");

        if (inputUrl == null || inputUrl.trim().isEmpty()) {
            ctx.sessionAttribute(FlashMassages.MESSAGE_KEY, FlashMassages.URL_EMPTY);
            ctx.sessionAttribute(FlashMassages.TYPE_KEY, FlashMassages.ERROR);
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        URL url;
        try {
            url = URI.create(inputUrl).toURL();
        } catch (Exception error) {
            ctx.sessionAttribute(FlashMassages.MESSAGE_KEY, FlashMassages.URL_INVALID);
            ctx.sessionAttribute(FlashMassages.TYPE_KEY, FlashMassages.ERROR);
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        try {
            String domainWithProtocol = url.getProtocol() + "://" + url.getHost();
            if (url.getPort() != -1) {
                domainWithProtocol += ":" + url.getPort();
            }

            var existingUrl = UrlRepository.findByName(domainWithProtocol);
            if (existingUrl.isPresent()) {
                ctx.sessionAttribute(FlashMassages.MESSAGE_KEY, FlashMassages.URL_ALREADY_EXISTS);
                ctx.sessionAttribute(FlashMassages.TYPE_KEY, FlashMassages.INFO);
                ctx.redirect(NamedRoutes.urlsPath());
                return;
            }

            var newUrl = new Url(domainWithProtocol);
            UrlRepository.save(newUrl);
            ctx.sessionAttribute(FlashMassages.MESSAGE_KEY, FlashMassages.URL_CREATED);
            ctx.sessionAttribute(FlashMassages.TYPE_KEY, FlashMassages.SUCCESS);
            ctx.redirect(NamedRoutes.urlsPath());
        } catch (SQLException error) {
            ctx.sessionAttribute(FlashMassages.MESSAGE_KEY, FlashMassages.URL_ERROR);
            ctx.sessionAttribute(FlashMassages.TYPE_KEY, FlashMassages.ERROR);
            ctx.redirect(NamedRoutes.rootPath());
        }
    }
}
