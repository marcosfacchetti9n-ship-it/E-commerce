package com.portfolio.ecommerce.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "El nombre es obligatorio.")
        String firstName,
        @NotBlank(message = "El apellido es obligatorio.")
        String lastName,
        @Email(message = "El email no es valido.")
        @NotBlank(message = "El email es obligatorio.")
        String email,
        @NotBlank(message = "La password es obligatoria.")
        @Size(min = 6, message = "La password debe tener al menos 6 caracteres.")
        String password
) {
}
