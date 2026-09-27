package id.tokokita.catalog_service.product.dto;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, BigDecimal price, String category, Long version) {}
