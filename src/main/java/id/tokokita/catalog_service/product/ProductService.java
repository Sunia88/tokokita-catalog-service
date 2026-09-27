package id.tokokita.catalog_service.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class
ProductService {

    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<Product> findAll() {
        return List.copyOf(products.values());
    }

    public Product getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found: " + id));
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    public Product create(String name, BigDecimal price, String category) {
        long id = nextId.getAndIncrement();
        Product product = new Product(id, name, price, category);
        products.put(id, product);
        return product;
    }

    public Product update(Long id, String name, BigDecimal price, String category) {
        getById(id);
        Product updated = new Product(id, name, price, category);
        products.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        if (products.remove(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + id);
        }
    }
}
