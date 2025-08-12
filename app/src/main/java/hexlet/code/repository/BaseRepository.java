package hexlet.code.repository;

import com.zaxxer.hikari.HikariDataSource;

public class BaseRepository {
    private static HikariDataSource dataSource;

    public static void setDataSource(HikariDataSource dataSource) {
        BaseRepository.dataSource = dataSource;
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}
