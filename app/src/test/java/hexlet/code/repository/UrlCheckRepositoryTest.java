package hexlet.code.repository;

import hexlet.code.model.UrlCheck;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.zaxxer.hikari.HikariDataSource;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UrlCheckRepositoryTest {
    private HikariDataSource dataSource;

    @BeforeAll
    void setUp() throws SQLException, IOException {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;");

        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute(readSchemaDB());
        }

        BaseRepository.setDataSource(dataSource);
    }

    @BeforeEach
    void cleanUp() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute("DELETE FROM url_checks");
            connection.createStatement().execute("DELETE FROM urls");
        }
    }

    @Test
    void testSaveAndFindByUrlId() throws SQLException {
        String insertUrlSql = "INSERT INTO urls (name) VALUES (?)";
        Long urlId;

        try (Connection connection = dataSource.getConnection();
             var statement = connection.prepareStatement(insertUrlSql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, "https://example.com");
            statement.executeUpdate();

            var resultSet = statement.getGeneratedKeys();
            resultSet.next();
            urlId = resultSet.getLong(1);
        }

        var urlCheck = new UrlCheck(200, "Test Title", "Test H1", "Test Description", urlId);
        UrlCheckRepository.save(urlCheck);

        assertThat(urlCheck.getId()).isNotNull();

        List<UrlCheck> checks = UrlCheckRepository.findByUrlId(urlId);
        assertThat(checks).hasSize(1);

        var savedCheck = checks.get(0);
        assertThat(savedCheck.getStatusCode()).isEqualTo(200);
        assertThat(savedCheck.getTitle()).isEqualTo("Test Title");
        assertThat(savedCheck.getH1()).isEqualTo("Test H1");
        assertThat(savedCheck.getDescription()).isEqualTo("Test Description");
    }

    @Test
    void testFindLatestByUrlId() throws SQLException {
        String insertUrlSql = "INSERT INTO urls (name) VALUES (?)";
        Long urlId;

        try (Connection connection = dataSource.getConnection();
             var statement = connection.prepareStatement(insertUrlSql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, "https://example.com");
            statement.executeUpdate();

            var resultSet = statement.getGeneratedKeys();
            resultSet.next();
            urlId = resultSet.getLong(1);
        }

        var firstCheck = new UrlCheck(200, "First Title", "First H1", "First Description", urlId);
        var secondCheck = new UrlCheck(404, "Second Title", "Second H1", "Second Description", urlId);

        UrlCheckRepository.save(firstCheck);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        UrlCheckRepository.save(secondCheck);

        Optional<UrlCheck> latestCheck = UrlCheckRepository.findLatestByUrlId(urlId);
        assertThat(latestCheck).isPresent();
        assertThat(latestCheck.get().getStatusCode()).isEqualTo(404);
        assertThat(latestCheck.get().getTitle()).isEqualTo("Second Title");
    }

    private String readSchemaDB() throws IOException {
        try (var stream = UrlCheckRepositoryTest.class.getResourceAsStream("/schema.sql")) {
            return new BufferedReader(new InputStreamReader(stream))
                .lines()
                .collect(Collectors.joining());
        }
    }
}
