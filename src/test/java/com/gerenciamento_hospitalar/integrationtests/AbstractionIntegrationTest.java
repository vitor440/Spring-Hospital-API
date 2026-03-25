package com.gerenciamento_hospitalar.integrationtests;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractionIntegrationTest.Initializer.class)
public class AbstractionIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>("postgres:15.17");

        public static void startable() {
            Startables.deepStart(Stream.of(postgreSQL)).join();
        }


        public static Map<String, String> propertiesSources() {
            return Map.of("spring.datasource.url", postgreSQL.getJdbcUrl(),
                    "spring.datasource.username", postgreSQL.getUsername(),
                    "spring.datasource.password", postgreSQL.getPassword());
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startable();

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource mapPropertySource = new MapPropertySource("testcontainers", (Map) propertiesSources());

            environment.getPropertySources().addFirst(mapPropertySource);
        }
    }
}
