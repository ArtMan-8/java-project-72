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

import hexlet.code.model.UrlCheck;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        try (Connection connection = BaseRepository.getDataSource().getConnection()) {
            String sql = "INSERT INTO url_checks (url_id, status_code, h1, title, description, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, urlCheck.getUrlId());
            statement.setInt(2, urlCheck.getStatusCode());
            statement.setString(3, urlCheck.getH1());
            statement.setString(4, urlCheck.getTitle());
            statement.setString(5, urlCheck.getDescription());
            statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                urlCheck.setId(resultSet.getLong(1));
            }
        } catch (SQLException error) {
            throw new SQLException("Ошибка сохранения проверки URL", error);
        }
    }

    public static List<UrlCheck> findByUrlId(Long urlId) throws SQLException {
        try (Connection connection = BaseRepository.getDataSource().getConnection()) {
            String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, urlId);

            ResultSet resultSet = statement.executeQuery();
            List<UrlCheck> checks = new ArrayList<>();

            while (resultSet.next()) {
                UrlCheck check = new UrlCheck(
                    resultSet.getInt("status_code"),
                    resultSet.getString("title"),
                    resultSet.getString("h1"),
                    resultSet.getString("description"),
                    resultSet.getLong("url_id")
                );
                check.setId(resultSet.getLong("id"));
                check.setCreatedAt(resultSet.getTimestamp("created_at"));
                checks.add(check);
            }

            return checks;
        } catch (SQLException error) {
            throw new SQLException("Ошибка получения проверок URL", error);
        }
    }

    public static Optional<UrlCheck> findLatestByUrlId(Long urlId) throws SQLException {
        try (Connection connection = BaseRepository.getDataSource().getConnection()) {
            String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, urlId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UrlCheck check = new UrlCheck(
                    resultSet.getInt("status_code"),
                    resultSet.getString("title"),
                    resultSet.getString("h1"),
                    resultSet.getString("description"),
                    resultSet.getLong("url_id")
                );
                check.setId(resultSet.getLong("id"));
                check.setCreatedAt(resultSet.getTimestamp("created_at"));
                return Optional.of(check);
            }

            return Optional.empty();
        } catch (SQLException error) {
            throw new SQLException("Ошибка получения последней проверки URL", error);
        }
    }
}
