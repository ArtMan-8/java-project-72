package hexlet.code.dto;

import java.util.List;
import java.util.Map;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
public class UrlsPage extends BasePage {
    private List<Url> urls;
    private Map<Long, UrlCheck> latestChecks;
}
