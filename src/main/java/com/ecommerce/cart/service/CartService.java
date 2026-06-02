package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.CartItemResponse;
import com.ecommerce.cart.dto.CartResponse;
import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.cart.repository.CartItemRepository;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.common.exception.ApiException;
import com.ecommerce.common.security.SecurityUtils;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.service.ProductService;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public CartResponse getCart() {
        Cart cart = getOrCreateCart();
        return toResponse(cart);
    }

    @Transactional
    public CartResponse addProduct(Long productId) {
        Cart cart = getOrCreateCart();
        Product product = productService.getProduct(productId);

        if (product.getStock() < 1) {
            throw new ApiException("Товар отсутствует на складе", HttpStatus.BAD_REQUEST);
        }

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (item != null) {
            if (item.getQuantity() >= product.getStock()) {
                throw new ApiException("Недостаточно товара на складе", HttpStatus.BAD_REQUEST);
            }
            item.setQuantity(item.getQuantity() + 1);
        } else {
            item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(1)
                    .build();
            cart.getItems().add(item);
        }

        recalculateTotal(cart);
        cartRepository.save(cart);
        return toResponse(cart);
    }

    @Transactional
    public CartResponse updateItem(Long itemId, int quantity) {
        Cart cart = getOrCreateCart();
        CartItem item = cartItemRepository.findByIdAndCartUserId(itemId, SecurityUtils.getCurrentUser().getId())
                .orElseThrow(() -> new ApiException("Позиция корзины не найдена", HttpStatus.NOT_FOUND));

        if (quantity > item.getProduct().getStock()) {
            throw new ApiException("Недостаточно товара на складе", HttpStatus.BAD_REQUEST);
        }

        item.setQuantity(quantity);
        recalculateTotal(cart);
        cartRepository.save(cart);
        return toResponse(cart);
    }

    @Transactional
    public CartResponse removeItem(Long itemId) {
        Cart cart = getOrCreateCart();
        CartItem item = cartItemRepository.findByIdAndCartUserId(itemId, SecurityUtils.getCurrentUser().getId())
                .orElseThrow(() -> new ApiException("Позиция корзины не найдена", HttpStatus.NOT_FOUND));

        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        recalculateTotal(cart);
        cartRepository.save(cart);
        return toResponse(cart);
    }

    @Transactional
    public Cart getOrCreateCartEntity() {
        return getOrCreateCart();
    }

    @Transactional
    public void clearCart(Cart cart) {
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart() {
        Long userId = SecurityUtils.getCurrentUser().getId();
        return cartRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ApiException("Пользователь не найден", HttpStatus.NOT_FOUND));
                    Cart cart = Cart.builder().user(user).build();
                    return cartRepository.save(cart);
                });
    }

    private void recalculateTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);
    }

    private CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .totalPrice(cart.getTotalPrice())
                .items(cart.getItems().stream().map(this::toItemResponse).toList())
                .build();
    }

    private CartItemResponse toItemResponse(CartItem item) {
        BigDecimal subtotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        return CartItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .price(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .subtotal(subtotal)
                .build();
    }
}
