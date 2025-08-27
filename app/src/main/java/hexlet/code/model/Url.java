package hexlet.code.model;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Url {
    private Long id;
    private String name;
    private Instant createdAt;

    public Url(String urlName) {
        this.name = urlName;
    }
}
