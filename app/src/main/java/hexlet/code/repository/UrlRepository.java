package hexlet.code.repository;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

import hexlet.code.model.Url;

public class UrlRepository extends BaseRepository {
    @Getter
    private static List<Url> entities = new ArrayList<Url>();

    public static void save(Url url) {
        if (url.getId() == null) {
            url.setId((long) entities.size() + 1);
            entities.add(url);
        } else {
            // TODO: добавить flash-сообщение
        }
    }

    public static Url getById(Long id) {
        for (Url url : entities) {
            if (url.getId().equals(id)) {
                return url;
            }
        }

        return null;
    }

    public static boolean existsByUrl(String url) {
        return entities.stream()
                .anyMatch(value -> value.getName().equals(url));
    }
}
