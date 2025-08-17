package hexlet.code.controller;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlRepository;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.util.FlashMessages;
import hexlet.code.util.NamedRoutes;
import kong.unirest.core.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;

import java.sql.SQLException;

public class UrlCheckController {
    public static void check(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", String.class).get();
        var url = UrlRepository.findById(Long.valueOf(urlId));

        if (url.isEmpty()) {
            throw new NotFoundResponse("Url with id=" + urlId + " not found");
        }

        try {
            var urlCheck = checkUrl(url.get().getName());
            urlCheck.setUrlId(Long.valueOf(urlId));
            UrlCheckRepository.save(urlCheck);

            ctx.sessionAttribute(FlashMessages.MESSAGE_KEY, FlashMessages.URL_CHECK_SUCCESS);
            ctx.sessionAttribute(FlashMessages.TYPE_KEY, FlashMessages.SUCCESS);
        } catch (UnirestException error) {
            var urlCheck = new UrlCheck(500, "", "", "Ошибка подключения: " + error.getMessage(), Long.valueOf(urlId));
            UrlCheckRepository.save(urlCheck);

            ctx.sessionAttribute(FlashMessages.MESSAGE_KEY, FlashMessages.URL_CHECK_ERROR);
            ctx.sessionAttribute(FlashMessages.TYPE_KEY, FlashMessages.ERROR);
        } catch (Exception error) {
            ctx.sessionAttribute(FlashMessages.MESSAGE_KEY, FlashMessages.URL_CHECK_ERROR);
            ctx.sessionAttribute(FlashMessages.TYPE_KEY, FlashMessages.ERROR);
        }

        ctx.redirect(NamedRoutes.urlPath(urlId));
    }

    private static UrlCheck checkUrl(String url) throws UnirestException {
        try {
            HttpResponse<String> response = Unirest.get(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .asString();

            int statusCode = response.getStatus();
            String html = response.getBody();

            Document doc = Jsoup.parse(html, url);

            String title = doc.title();
            String h1 = "";
            String description = "";

            Element h1Element = doc.selectFirst("h1");
            if (h1Element != null) {
                h1 = h1Element.text();
            }

            Element metaDescription = doc.selectFirst("meta[name=description]");
            if (metaDescription != null) {
                description = metaDescription.attr("content");
            }

            return new UrlCheck(statusCode, title, h1, description, null);
        } catch (UnirestException e) {
            return new UrlCheck(500, "", "", "Ошибка подключения: " + e.getMessage(), null);
        }
    }
}
