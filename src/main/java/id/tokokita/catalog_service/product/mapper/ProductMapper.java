package id.tokokita.catalog_service.product.mapper;

import id.tokokita.catalog_service.product.dto.ProductCreateRequest;
import id.tokokita.catalog_service.product.dto.ProductResponse;
import id.tokokita.catalog_service.product.entity.ProductEntity;

public final class ProductMapper {

    private ProductMapper() {}

    public static ProductResponse toResponse(ProductEntity entity) {
        return new ProductResponse(
                entity.getId(), entity.getName(), entity.getPrice(), entity.getCategory(), entity.getVersion());
    }

    public static ProductEntity toEntity(ProductCreateRequest request) {
        return new ProductEntity(request.name(), request.price(), request.category());
    }
}
