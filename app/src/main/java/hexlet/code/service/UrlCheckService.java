package hexlet.code.service;

import hexlet.code.model.UrlCheck;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;

public class UrlCheckService {
    public static UrlCheck checkUrl(String url) throws UnirestException {
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
