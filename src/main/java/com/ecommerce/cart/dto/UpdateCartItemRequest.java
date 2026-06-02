package com.ecommerce.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    @NotNull(message = "Количество обязательно")
    @Min(value = 1, message = "Минимум 1 товар")
    private Integer quantity;
}
