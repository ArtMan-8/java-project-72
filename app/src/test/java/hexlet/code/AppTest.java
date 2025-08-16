package hexlet.code;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.repository.BaseRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class AppTest {
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
    void testDatabaseConnection() {
        assertDoesNotThrow(() -> {
            try (Connection connection = dataSource.getConnection()) {
                assertTrue(connection.isValid(1));
            }
        });
    }

    @Test
    void testSchemaCreation() {
        assertDoesNotThrow(() -> {
            try (Connection connection = dataSource.getConnection();
                 Statement stmt = connection.createStatement()) {
                var rs = stmt.executeQuery("SELECT COUNT(*) FROM urls");
                assertTrue(rs.next());
                assertEquals(0, rs.getInt(1));
            }
        });
    }

    @Test
    void testUrlInsertion() {
        assertDoesNotThrow(() -> {
            try (Connection connection = dataSource.getConnection();
                 Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("INSERT INTO urls (name) VALUES ('https://example.com')");

                var rs = stmt.executeQuery("SELECT COUNT(*) FROM urls WHERE name = 'https://example.com'");
                assertTrue(rs.next());
                assertEquals(1, rs.getInt(1));
            }
        });
    }

    @Test
    void testUrlUniqueness() {
        assertDoesNotThrow(() -> {
            try (Connection connection = dataSource.getConnection();
                 Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("INSERT INTO urls (name) VALUES ('https://test.com')");

                assertThrows(SQLException.class, () -> {
                    stmt.executeUpdate("INSERT INTO urls (name) VALUES ('https://test.com')");
                });
            }
        });
    }

    private String readSchemaDB() throws IOException {
        try (var stream = AppTest.class.getResourceAsStream("/schema.sql")) {
            return new BufferedReader(new InputStreamReader(stream))
                .lines()
                .collect(Collectors.joining());
        }
    }
}
