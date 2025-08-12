package hexlet.code.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Url {
    private Long id;
    private String name;
    private Timestamp createdAt;

    public Url(String name) {
        this.name = name;
    }
}
