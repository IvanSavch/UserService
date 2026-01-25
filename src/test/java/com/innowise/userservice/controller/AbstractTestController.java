package com.innowise.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;


@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
public abstract class AbstractTestController {

    @Autowired
    private Environment environment;

    @BeforeEach
    void logProperties() {
        log.info("PostgreSQL URL: {}", environment.getProperty("spring.datasource.url"));
        log.info("Redis host: {}", environment.getProperty("spring.data.redis.host"));
        log.info("Redis port: {}", environment.getProperty("spring.data.redis.port"));
    }
    @Container
    static  PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("testDB")
            .withUsername("postgres")
            .withPassword("root")
            .withReuse(true)
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 1));
    @Container
    static  GenericContainer<?> redis = new GenericContainer<>("redis").withExposedPorts(6379);
    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgres.getUsername());
        registry.add("spring.datasource.password", () -> postgres.getPassword());

        registry.add("spring.datasource.hikari.maxLifetime", () -> "30000");
        registry.add("spring.datasource.hikari.connectionTimeout", () -> "30000");
        registry.add("spring.datasource.hikari.maximumPoolSize", () -> "5");


    }
}
