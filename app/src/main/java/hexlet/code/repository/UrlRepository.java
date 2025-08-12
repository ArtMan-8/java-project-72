package hexlet.code.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hexlet.code.model.Url;

public class UrlRepository extends BaseRepository {
    public static List<Url> getEntities() throws SQLException {
        try (Connection connection = BaseRepository.getDataSource().getConnection()) {
            String sql = "SELECT * FROM urls";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            List<Url> urls = new ArrayList<>();
            while (resultSet.next()) {
                Url url = new Url(resultSet.getString("name"));
                url.setId(resultSet.getLong("id"));
                url.setCreatedAt(resultSet.getTimestamp("created_at"));
                urls.add(url);
            }
            return urls;
        } catch (SQLException error) {
            throw new SQLException("Ошибка получения списка URL", error);
        }
    }

    public static void save(Url url) throws SQLException {
        try (Connection connection = BaseRepository.getDataSource().getConnection()) {
            String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, url.getName());
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                url.setId(resultSet.getLong(1));
            }
        } catch (SQLException error) {
            throw new SQLException("Ошибка сохранения URL", error);
        }
    }

    public static Optional<Url> findById(Long id) throws SQLException {
        try (Connection connection = BaseRepository.getDataSource().getConnection()) {
            String sql = "SELECT * FROM urls WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Url foundUrl = new Url(resultSet.getString("name"));
                foundUrl.setId(resultSet.getLong("id"));
                foundUrl.setCreatedAt(resultSet.getTimestamp("created_at"));
                return Optional.of(foundUrl);
            }

            return Optional.empty();
        } catch (SQLException error) {
            throw new SQLException("Ошибка поиска URL по id", error);
        }
    }

    public static Optional<Url> findByName(String url) throws SQLException {
        try (Connection connection = BaseRepository.getDataSource().getConnection()) {
            String sql = "SELECT * FROM urls WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, url);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Url foundUrl = new Url(resultSet.getString("name"));
                foundUrl.setId(resultSet.getLong("id"));
                foundUrl.setCreatedAt(resultSet.getTimestamp("created_at"));
                return Optional.of(foundUrl);
            }

            return Optional.empty();
        } catch (SQLException error) {
            throw new SQLException("Ошибка поиска URL по имени", error);
        }
    }
}
