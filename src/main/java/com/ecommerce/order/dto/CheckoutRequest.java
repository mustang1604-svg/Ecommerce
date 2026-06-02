package com.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CheckoutRequest {

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^[+]?[\\d\\s\\-()]{10,20}$", message = "Некорректный номер телефона")
    private String phone;

    @Size(max = 500, message = "Адрес не более 500 символов")
    private String address;

    @Size(max = 1000, message = "Комментарий не более 1000 символов")
    private String comment;
}
