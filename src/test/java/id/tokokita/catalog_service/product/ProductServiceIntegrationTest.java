package id.tokokita.catalog_service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import id.tokokita.catalog_service.product.dto.ProductCreateRequest;
import id.tokokita.catalog_service.product.dto.ProductResponse;
import id.tokokita.catalog_service.product.dto.ProductUpdateRequest;
import id.tokokita.catalog_service.product.entity.ProductRepository;
import id.tokokita.catalog_service.support.PostgresTestcontainers;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ImportTestcontainers(PostgresTestcontainers.class)
@Testcontainers(disabledWithoutDocker = true)
@Transactional
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createPersistsProduct() {
        ProductResponse created = productService.create(
                new ProductCreateRequest("Widget", new BigDecimal("9.99"), "Electronics"));

        assertThat(created.id()).isNotNull();
        assertThat(created.version()).isZero();
        assertThat(productRepository.existsById(created.id())).isTrue();
    }

    @Test
    void updatePersistsChangesAndIncrementsVersion() {
        ProductResponse created = productService.create(
                new ProductCreateRequest("Widget", new BigDecimal("9.99"), "Electronics"));

        ProductResponse updated = productService.update(
                created.id(),
                new ProductUpdateRequest("Gadget", new BigDecimal("19.99"), "Electronics", created.version()));

        assertThat(updated.name()).isEqualTo("Gadget");
        assertThat(updated.price()).isEqualByComparingTo("19.99");
        assertThat(updated.version()).isEqualTo(1L);
    }

    @Test
    void updateWithStaleVersionThrowsConflict() {
        ProductResponse created = productService.create(
                new ProductCreateRequest("Widget", new BigDecimal("9.99"), "Electronics"));

        productService.update(
                created.id(),
                new ProductUpdateRequest("Gadget", new BigDecimal("19.99"), "Electronics", created.version()));

        assertThatThrownBy(() -> productService.update(
                        created.id(),
                        new ProductUpdateRequest("Old", new BigDecimal("1.00"), "Electronics", created.version())))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(ex -> ((ResponseStatusException) ex).getStatusCode().value())
                .isEqualTo(409);
    }

    @Test
    void deleteRemovesProduct() {
        ProductResponse created = productService.create(
                new ProductCreateRequest("Widget", new BigDecimal("9.99"), "Electronics"));

        productService.delete(created.id());

        assertThat(productRepository.existsById(created.id())).isFalse();
    }
}
