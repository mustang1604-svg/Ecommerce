package com.ecommerce.cart.controller;

import com.ecommerce.cart.dto.CartResponse;
import com.ecommerce.cart.dto.UpdateCartItemRequest;
import com.ecommerce.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Корзина", description = "Корзина покупок")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Текущая корзина пользователя")
    public CartResponse getCart() {
        return cartService.getCart();
    }

    @PostMapping("/add/{productId}")
    @Operation(summary = "Добавить товар в корзину")
    public CartResponse addProduct(@PathVariable Long productId) {
        return cartService.addProduct(productId);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Изменить количество товара в корзине")
    public CartResponse updateItem(@PathVariable Long id, @Valid @RequestBody UpdateCartItemRequest request) {
        return cartService.updateItem(id, request.getQuantity());
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "Удалить товар из корзины")
    public CartResponse removeItem(@PathVariable Long id) {
        return cartService.removeItem(id);
    }
}
