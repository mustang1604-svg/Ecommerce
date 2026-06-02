package com.ecommerce.product.service;

import com.ecommerce.common.exception.ApiException;
import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void findById_returnsProduct() {
        Product product = Product.builder()
                .id(1L)
                .name("Тест")
                .price(new BigDecimal("100.00"))
                .stock(10)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.findById(1L);

        assertThat(response.getName()).isEqualTo("Тест");
        assertThat(response.getPrice()).isEqualByComparingTo("100.00");
    }

    @Test
    void findById_throwsWhenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(ApiException.class)
                .extracting("status")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void create_savesProduct() {
        ProductRequest request = new ProductRequest();
        request.setName("Новый товар");
        request.setPrice(new BigDecimal("500.00"));
        request.setStock(5);
        request.setCategoryId(1L);

        Category category = Category.builder().id(1L).name("Электроника").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        ProductResponse response = productService.create(request);

        assertThat(response.getName()).isEqualTo("Новый товар");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void findAll_returnsPage() {
        Product product = Product.builder().id(1L).name("A").price(BigDecimal.TEN).stock(1).build();
        when(productRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(product)));

        Page<ProductResponse> page = productService.findAll(null, null, Pageable.unpaged());

        assertThat(page.getContent()).hasSize(1);
    }
}
