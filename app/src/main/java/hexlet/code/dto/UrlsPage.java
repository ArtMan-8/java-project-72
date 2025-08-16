package hexlet.code.dto;

import java.util.List;
import hexlet.code.model.Url;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
public class UrlsPage extends BasePage {
    private List<Url> urls;
}
