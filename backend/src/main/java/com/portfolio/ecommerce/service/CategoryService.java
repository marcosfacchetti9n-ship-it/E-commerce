package com.portfolio.ecommerce.service;

import com.portfolio.ecommerce.dto.category.CategoryRequest;
import com.portfolio.ecommerce.dto.category.CategoryResponse;
import com.portfolio.ecommerce.entity.Category;
import com.portfolio.ecommerce.exception.BadRequestException;
import com.portfolio.ecommerce.exception.NotFoundException;
import com.portfolio.ecommerce.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MapperService mapperService;

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(mapperService::toCategoryResponse)
                .toList();
    }

    public CategoryResponse create(CategoryRequest request) {
        String normalizedName = request.name().trim();
        if (categoryRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new BadRequestException("Ya existe una categoria con ese nombre.");
        }

        Category category = new Category();
        category.setName(normalizedName);
        category.setDescription(request.description().trim());
        return mapperService.toCategoryResponse(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getEntity(id);
        String normalizedName = request.name().trim();
        categoryRepository.findByNameIgnoreCase(normalizedName)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BadRequestException("Ya existe una categoria con ese nombre.");
                });

        category.setName(normalizedName);
        category.setDescription(request.description().trim());
        return mapperService.toCategoryResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        Category category = getEntity(id);
        if (!category.getProducts().isEmpty()) {
            throw new BadRequestException("No podes eliminar una categoria que todavia tiene productos.");
        }
        categoryRepository.delete(category);
    }

    public Category getEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria no encontrada."));
    }
}
