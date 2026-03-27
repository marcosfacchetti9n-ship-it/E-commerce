package com.portfolio.ecommerce.dto.auth;

import java.util.Set;

public record AuthResponse(
        String token,
        String firstName,
        String lastName,
        String email,
        Set<String> roles
) {
}
