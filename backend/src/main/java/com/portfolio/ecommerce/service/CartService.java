package com.portfolio.ecommerce.service;

import com.portfolio.ecommerce.dto.cart.AddCartItemRequest;
import com.portfolio.ecommerce.dto.cart.CartResponse;
import com.portfolio.ecommerce.dto.cart.UpdateCartItemRequest;
import com.portfolio.ecommerce.entity.Cart;
import com.portfolio.ecommerce.entity.CartItem;
import com.portfolio.ecommerce.entity.Product;
import com.portfolio.ecommerce.exception.BadRequestException;
import com.portfolio.ecommerce.exception.NotFoundException;
import com.portfolio.ecommerce.repository.CartItemRepository;
import com.portfolio.ecommerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final MapperService mapperService;

    public CartResponse getMyCart(String email) {
        return mapperService.toCartResponse(getCartEntity(email));
    }

    @Transactional
    public CartResponse addItem(String email, AddCartItemRequest request) {
        Cart cart = getCartEntity(email);
        Product product = productService.getEntity(request.productId());

        validateRequestedQuantity(product, request.quantity());

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    cart.getItems().add(newItem);
                    return newItem;
                });

        int newQuantity = item.getQuantity() + request.quantity();
        validateRequestedQuantity(product, newQuantity);
        item.setQuantity(newQuantity);

        cartRepository.save(cart);
        return mapperService.toCartResponse(getCartEntity(email));
    }

    @Transactional
    public CartResponse updateItem(String email, Long itemId, UpdateCartItemRequest request) {
        Cart cart = getCartEntity(email);
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item del carrito no encontrado."));

        validateRequestedQuantity(item.getProduct(), request.quantity());
        item.setQuantity(request.quantity());
        cartRepository.save(cart);
        return mapperService.toCartResponse(getCartEntity(email));
    }

    @Transactional
    public CartResponse removeItem(String email, Long itemId) {
        Cart cart = getCartEntity(email);
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item del carrito no encontrado."));
        cart.getItems().remove(item);
        cartRepository.save(cart);
        return mapperService.toCartResponse(getCartEntity(email));
    }

    @Transactional
    public CartResponse clear(String email) {
        Cart cart = getCartEntity(email);
        cart.getItems().clear();
        cartRepository.save(cart);
        return mapperService.toCartResponse(getCartEntity(email));
    }

    public Cart getCartEntity(String email) {
        return cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado."));
    }

    private void validateRequestedQuantity(Product product, Integer quantity) {
        if (product.getStock() < quantity) {
            throw new BadRequestException("No hay stock suficiente para " + product.getName() + ".");
        }
    }
}
