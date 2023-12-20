package com.website.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@SpringBootTest
public class DatabaseConnectionTest {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.password}")
    private String password;

    final String URL = "jdbc:mysql://localhost:3306/website";
    final String USERNAME = "root";
    final String PASSWORD = "";

    @Test
    void testMysqlConnection() throws Exception{

        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("connection = {}", connection);
            Assertions.assertThat(connection).isNotNull();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DataSource dataSource = DataSourceBuilder.create()
                .url(URL)
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        try {
            Connection connection = dataSource.getConnection();
            Assertions.assertThat(connection).isNotNull();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void testEnvironmentDatabaseConnectionTest() {
        DataSource dataSource = DataSourceBuilder.create()
                .url(url)
                .password(password)
                .username(username)
                .build();

        try {
            Connection connection = dataSource.getConnection();
            log.info("test database connection instance= {}", connection);
            Assertions.assertThat(connection).isNotNull();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
