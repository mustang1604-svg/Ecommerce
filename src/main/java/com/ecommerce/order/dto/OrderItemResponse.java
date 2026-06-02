package com.ecommerce.order.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {

    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
