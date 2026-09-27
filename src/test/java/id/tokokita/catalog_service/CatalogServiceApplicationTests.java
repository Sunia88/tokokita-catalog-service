package id.tokokita.catalog_service;

import id.tokokita.catalog_service.support.PostgresTestcontainers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ImportTestcontainers(PostgresTestcontainers.class)
@Testcontainers(disabledWithoutDocker = true)
class CatalogServiceApplicationTests {

    @Test
    void contextLoads() {}
}
