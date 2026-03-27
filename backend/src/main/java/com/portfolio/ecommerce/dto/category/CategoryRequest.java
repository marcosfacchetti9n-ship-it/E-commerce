package com.portfolio.ecommerce.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "El nombre es obligatorio.")
        String name,
        @NotBlank(message = "La descripcion es obligatoria.")
        @Size(max = 500, message = "La descripcion no puede superar los 500 caracteres.")
        String description
) {
}
