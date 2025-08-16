package hexlet.code.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlCheck {
    private Long id;
    private Integer statusCode;
    private String title;
    private String h1;
    private String description;
    private Long urlId;
    private Timestamp createdAt;

    public UrlCheck(Integer newStatusCode, String newTitle, String newH1, String newDescription, Long newUrlId) {
        this.statusCode = newStatusCode;
        this.title = newTitle;
        this.h1 = newH1;
        this.description = newDescription;
        this.urlId = newUrlId;
    }
}
