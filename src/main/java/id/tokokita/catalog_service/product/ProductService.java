package id.tokokita.catalog_service.product;

import id.tokokita.catalog_service.product.dto.ProductCreateRequest;
import id.tokokita.catalog_service.product.dto.ProductResponse;
import id.tokokita.catalog_service.product.dto.ProductUpdateRequest;
import id.tokokita.catalog_service.product.entity.ProductEntity;
import id.tokokita.catalog_service.product.entity.ProductRepository;
import id.tokokita.catalog_service.product.mapper.ProductMapper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(ProductMapper::toResponse).toList();
    }

    public ProductResponse getById(Long id) {
        return ProductMapper.toResponse(findEntityById(id));
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest request) {
        ProductEntity entity = ProductMapper.toEntity(request);
        return ProductMapper.toResponse(productRepository.save(entity));
    }

    @Transactional
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        ProductEntity entity = findEntityById(id);
        if (!entity.getVersion().equals(request.version())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Product was modified by another transaction");
        }
        entity.update(request.name(), request.price(), request.category());
        return ProductMapper.toResponse(productRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        ProductEntity entity = findEntityById(id);
        productRepository.delete(entity);
    }

    private ProductEntity findEntityById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found: " + id));
    }
}
