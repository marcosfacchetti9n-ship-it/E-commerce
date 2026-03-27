package com.portfolio.ecommerce.controller;

import com.portfolio.ecommerce.dto.cart.AddCartItemRequest;
import com.portfolio.ecommerce.dto.cart.CartResponse;
import com.portfolio.ecommerce.dto.cart.UpdateCartItemRequest;
import com.portfolio.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartResponse getMyCart(@AuthenticationPrincipal(expression = "username") String email) {
        return cartService.getMyCart(email);
    }

    @PostMapping("/items")
    public CartResponse addItem(
            @AuthenticationPrincipal(expression = "username") String email,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        return cartService.addItem(email, request);
    }

    @PutMapping("/items/{itemId}")
    public CartResponse updateItem(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        return cartService.updateItem(email, itemId, request);
    }

    @DeleteMapping("/items/{itemId}")
    public CartResponse removeItem(@AuthenticationPrincipal(expression = "username") String email, @PathVariable Long itemId) {
        return cartService.removeItem(email, itemId);
    }

    @DeleteMapping("/clear")
    public CartResponse clear(@AuthenticationPrincipal(expression = "username") String email) {
        return cartService.clear(email);
    }
}
