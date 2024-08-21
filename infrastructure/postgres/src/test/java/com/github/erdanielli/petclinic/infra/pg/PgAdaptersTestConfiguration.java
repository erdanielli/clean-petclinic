package com.github.erdanielli.petclinic.infra.pg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootConfiguration
@EnableAutoConfiguration
public class PgAdaptersTestConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> pg() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    }

    @Bean
    JsonRowMapperFactory jsonRowMapperFactory(ObjectMapper objectMapper) {
        return new JsonRowMapperFactory(objectMapper);
    }
}