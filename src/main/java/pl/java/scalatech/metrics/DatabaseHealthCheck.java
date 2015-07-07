package pl.java.scalatech.metrics;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import com.codahale.metrics.health.HealthCheck;

@Slf4j
public class DatabaseHealthCheck extends HealthCheck {
    private DataSource dataSource;
    private String validationQuery;

    public DatabaseHealthCheck(DataSource dataSource, String validationQuery) {
        this.dataSource = dataSource;
        this.validationQuery = validationQuery;
    }

    @Override
    protected Result check() throws Exception {
        try (Connection conn = dataSource.getConnection(); Statement statement = conn.createStatement()) {

            if (statement.execute(validationQuery)) { return Result.healthy(); }
        }

        return Result.unhealthy("validation query failed");
    }

}