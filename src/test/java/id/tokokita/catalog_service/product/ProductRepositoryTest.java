package id.tokokita.catalog_service.product;

import static org.assertj.core.api.Assertions.assertThat;

import id.tokokita.catalog_service.product.entity.ProductEntity;
import id.tokokita.catalog_service.product.entity.ProductRepository;
import id.tokokita.catalog_service.support.PostgresTestcontainers;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ImportTestcontainers(PostgresTestcontainers.class)
@Testcontainers(disabledWithoutDocker = true)
@Transactional
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void saveAndFindById() {
        ProductEntity saved = productRepository.save(new ProductEntity("Widget", new BigDecimal("9.99"), "Electronics"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVersion()).isZero();

        ProductEntity found = productRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getName()).isEqualTo("Widget");
        assertThat(found.getPrice()).isEqualByComparingTo("9.99");
        assertThat(found.getCategory()).isEqualTo("Electronics");
    }

    @Test
    void updateIncrementsVersion() {
        ProductEntity entity = productRepository.save(new ProductEntity("Widget", new BigDecimal("9.99"), "Electronics"));
        entity.update("Gadget", new BigDecimal("19.99"), "Electronics");

        ProductEntity updated = productRepository.saveAndFlush(entity);

        assertThat(updated.getName()).isEqualTo("Gadget");
        assertThat(updated.getVersion()).isEqualTo(1L);
    }

    @Test
    void deleteRemovesProduct() {
        ProductEntity saved = productRepository.save(new ProductEntity("Widget", new BigDecimal("9.99"), "Electronics"));

        productRepository.delete(saved);

        assertThat(productRepository.findById(saved.getId())).isEmpty();
    }
}
