package id.tokokita.catalog_service.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductCreateRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull @PositiveOrZero BigDecimal price,
        @NotBlank String category) {}
