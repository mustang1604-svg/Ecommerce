E-Commerce Platform
Интернет-магазин на Spring Boot 3 с каталогом товаров, корзиной, JWT-авторизацией и оформлением заказов.

Проект сделан как модульный монолит — один запускаемый сервис, но код разбит на модули (как в микросервисной архитектуре):

Модуль	Пакет	За что отвечает
user-service	com.ecommerce.user	Регистрация, вход, роли
product-service	com.ecommerce.product	CRUD товаров
cart-service	com.ecommerce.cart	Корзина покупок
order-service	com.ecommerce.order	Оформление заказов
Войти как администратор

При первом запуске создаётся админ:

Поле	Значение
Логин	admin
Пароль	admin123
Структура проекта
src/main/java/com/ecommerce/
├── EcommerceApplication.java      # Точка входа
├── common/                          # Общее: Security, JWT, ошибки
├── user/                            # Пользователи и auth
├── product/                         # Товары и категории
├── cart/                            # Корзина
└── order/                           # Заказы

src/main/resources/
├── application.yml                  # Настройки
└── db/migration/                    # Flyway-миграции SQL
Тесты
# Все тесты
mvn test

# Сборка + тесты
mvn clean verify
Юнит-тесты — Mockito, без базы данных
Интеграционные — Testcontainers + PostgreSQL (нужен Docker)
Стек технологий
Spring Boot 3.4, Spring Security, Spring Data JPA
PostgreSQL 14, Flyway
JWT (jjwt), SpringDoc OpenAPI
JUnit 5, Mockito, Testcontainers
GitHub Actions, Docker
