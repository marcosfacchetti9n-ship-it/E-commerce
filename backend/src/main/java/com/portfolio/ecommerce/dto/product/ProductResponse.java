package com.portfolio.ecommerce.dto.product;

import com.portfolio.ecommerce.dto.category.CategoryResponse;
import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String imageUrl,
        CategoryResponse category
) {
}
