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
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utils.FlashMessages;
import hexlet.code.utils.NamedRoutes;
import hexlet.code.repository.UrlCheckRepository;

import java.util.Map;
import java.util.HashMap;

public class UrlsController {
    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        Map<Long, UrlCheck> latestChecks = new HashMap<>();

        for (var url : urls) {
            var latestCheck = UrlCheckRepository.findLatestByUrlId(url.getId());
            latestChecks.put(url.getId(), latestCheck.orElse(null));
        }

        var page = new UrlsPage(urls, latestChecks);
        FlashMessages.setFlashToPage(ctx, page);
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", String.class).get();
        var url = UrlRepository.findById(Long.valueOf(urlId));

        if (url.isPresent()) {
            var checks = UrlCheckRepository.findByUrlId(Long.valueOf(urlId));
            var page = new UrlPage(url.get(), checks);

            FlashMessages.setFlashToPage(ctx, page);

            ctx.render("urls/show.jte", model("page", page));
        } else {
            throw new NotFoundResponse("Url with id=" + urlId + " not found");
        }
    }

    public static void create(Context ctx) throws SQLException {
        var inputUrl = ctx.formParam("url");

        if (inputUrl == null || inputUrl.trim().isEmpty()) {
            ctx.sessionAttribute(FlashMessages.MESSAGE_KEY, FlashMessages.URL_EMPTY);
            ctx.sessionAttribute(FlashMessages.TYPE_KEY, FlashMessages.ERROR);
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        URL url;
        try {
            url = URI.create(inputUrl).toURL();
        } catch (Exception error) {
            ctx.sessionAttribute(FlashMessages.MESSAGE_KEY, FlashMessages.URL_INVALID);
            ctx.sessionAttribute(FlashMessages.TYPE_KEY, FlashMessages.ERROR);
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
                ctx.sessionAttribute(FlashMessages.MESSAGE_KEY, FlashMessages.URL_ALREADY_EXISTS);
                ctx.sessionAttribute(FlashMessages.TYPE_KEY, FlashMessages.INFO);
                ctx.redirect(NamedRoutes.urlsPath());
                return;
            }

            var newUrl = new Url(domainWithProtocol);
            UrlRepository.save(newUrl);
            ctx.sessionAttribute(FlashMessages.MESSAGE_KEY, FlashMessages.URL_CREATED);
            ctx.sessionAttribute(FlashMessages.TYPE_KEY, FlashMessages.SUCCESS);
            ctx.redirect(NamedRoutes.urlsPath());
        } catch (SQLException error) {
            ctx.sessionAttribute(FlashMessages.MESSAGE_KEY, FlashMessages.URL_ERROR);
            ctx.sessionAttribute(FlashMessages.TYPE_KEY, FlashMessages.ERROR);
            ctx.redirect(NamedRoutes.rootPath());
        }
    }
}
