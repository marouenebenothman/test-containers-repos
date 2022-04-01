package com.jetbrains.testcontainersdemo;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public class UnitDatabaseTest {
    private static MySQLContainer container = (MySQLContainer) new MySQLContainer("mysql:5.7.37")
        .withReuse(true);

    @BeforeAll
    public static void setup() {
        container.start();
    }


    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",container::getJdbcUrl);
        registry.add("spring.datasource.username",container::getUsername);
        registry.add("spring.datasource.password",container::getPassword);
    }
}