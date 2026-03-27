package com.portfolio.ecommerce.service;

import com.portfolio.ecommerce.dto.product.ProductRequest;
import com.portfolio.ecommerce.dto.product.ProductResponse;
import com.portfolio.ecommerce.entity.Product;
import com.portfolio.ecommerce.exception.NotFoundException;
import com.portfolio.ecommerce.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final MapperService mapperService;

    public List<ProductResponse> findAll(Long categoryId) {
        List<Product> products = categoryId == null
                ? productRepository.findAll()
                : productRepository.findByCategoryId(categoryId);

        return products.stream().map(mapperService::toProductResponse).toList();
    }

    public ProductResponse findById(Long id) {
        return mapperService.toProductResponse(getEntity(id));
    }

    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        applyRequest(product, request);
        return mapperService.toProductResponse(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getEntity(id);
        applyRequest(product, request);
        return mapperService.toProductResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        productRepository.delete(getEntity(id));
    }

    public Product getEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado."));
    }

    private void applyRequest(Product product, ProductRequest request) {
        product.setName(request.name().trim());
        product.setDescription(request.description().trim());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setImageUrl(request.imageUrl().trim());
        product.setCategory(categoryService.getEntity(request.categoryId()));
    }
}
