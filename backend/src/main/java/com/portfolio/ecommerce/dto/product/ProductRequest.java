package com.portfolio.ecommerce.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "El nombre es obligatorio.")
        String name,
        @NotBlank(message = "La descripcion es obligatoria.")
        @Size(max = 1500, message = "La descripcion no puede superar los 1500 caracteres.")
        String description,
        @NotNull(message = "El precio es obligatorio.")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0.")
        BigDecimal price,
        @NotNull(message = "El stock es obligatorio.")
        @Min(value = 0, message = "El stock no puede ser negativo.")
        Integer stock,
        @NotBlank(message = "La imagen es obligatoria.")
        String imageUrl,
        @NotNull(message = "La categoria es obligatoria.")
        Long categoryId
) {
}
