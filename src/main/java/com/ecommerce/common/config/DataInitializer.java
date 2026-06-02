package com.ecommerce.common.config;

import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.user.entity.Role;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        createAdminIfMissing();
        seedCatalogIfEmpty();
    }

    private void createAdminIfMissing() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@shop.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }

    private void seedCatalogIfEmpty() {
        if (categoryRepository.count() > 0) {
            return;
        }

        Category electronics = categoryRepository.save(Category.builder().name("Электроника").build());
        Category clothes = categoryRepository.save(Category.builder().name("Одежда").build());
        Category books = categoryRepository.save(Category.builder().name("Книги").build());

        productRepository.save(Product.builder()
                .name("Смартфон X1")
                .description("смартфон POCO ")
                .price(new BigDecimal("1800.00"))
                .stock(50)
                .category(electronics)
                .build());
        productRepository.save(Product.builder()
                .name("Ноутбук Pro")
                .description("Мощный ноутбук Apple pro 14")
                .price(new BigDecimal("3200.00"))
                .stock(20)
                .category(electronics)
                .build());
        productRepository.save(Product.builder()
                .name("Футболка Puma")
                .description("Хлопковая футболка")
                .price(new BigDecimal("100.00"))
                .stock(100)
                .category(clothes)
                .build());
        productRepository.save(Product.builder()
                .name("Java для начинающих")
                .description("Учебник по программированию")
                .price(new BigDecimal("89.00"))
                .stock(30)
                .category(books)
                .build());
    }
}
