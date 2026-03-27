package com.portfolio.ecommerce.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "El email no es valido.")
        @NotBlank(message = "El email es obligatorio.")
        String email,
        @NotBlank(message = "La password es obligatoria.")
        String password
) {
}
