package hexlet.code.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.model.Url;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class UrlRepositoryTest {
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

    @Test
    void testSaveUrl() {
        assertDoesNotThrow(() -> {
            var url = new Url("https://example.com");
            UrlRepository.save(url);

            var savedUrl = UrlRepository.findByName("https://example.com");
            assertTrue(savedUrl.isPresent());
            assertEquals("https://example.com", savedUrl.get().getName());
        });
    }

    @Test
    void testFindByName() {
        assertDoesNotThrow(() -> {
            var url = new Url("https://test.com");
            UrlRepository.save(url);

            var foundUrl = UrlRepository.findByName("https://test.com");
            assertTrue(foundUrl.isPresent());
            assertEquals("https://test.com", foundUrl.get().getName());
        });
    }

    @Test
    void testFindById() {
        assertDoesNotThrow(() -> {
            var url = new Url("https://findbyid.com");
            UrlRepository.save(url);

            var savedUrl = UrlRepository.findByName("https://findbyid.com");
            assertTrue(savedUrl.isPresent());

            var foundById = UrlRepository.findById(savedUrl.get().getId());
            assertTrue(foundById.isPresent());
            assertEquals("https://findbyid.com", foundById.get().getName());
        });
    }

    @Test
    void testGetEntities() {
        assertDoesNotThrow(() -> {
            var urls = UrlRepository.getEntities();
            assertNotNull(urls);
            assertTrue(urls.size() >= 0);
        });
    }

    private String readSchemaDB() throws IOException {
        try (var stream = UrlRepositoryTest.class.getResourceAsStream("/schema.sql")) {
            return new BufferedReader(new InputStreamReader(stream))
                .lines()
                .collect(Collectors.joining());
        }
    }
}
