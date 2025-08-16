package hexlet.code.service;

import hexlet.code.model.UrlCheck;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class UrlCheckService {
    public static UrlCheck checkUrl(String url) throws IOException {
        try {
            Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(10000)
                .get();

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

            return new UrlCheck(200, title, h1, description, null);
        } catch (IOException e) {
            // В случае ошибки возвращаем проверку с кодом ошибки
            return new UrlCheck(500, "", "", "Ошибка подключения: " + e.getMessage(), null);
        }
    }
}
