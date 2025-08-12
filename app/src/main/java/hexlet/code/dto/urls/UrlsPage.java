package hexlet.code.dto.urls;

import java.util.List;
import hexlet.code.model.Url;

import lombok.Getter;
import lombok.AllArgsConstructor;


@Getter
@AllArgsConstructor
public class UrlsPage {
    private List<Url> urls;
}
