# E-Commerce Platform

Интернет-магазин на **Spring Boot 3** с каталогом товаров, корзиной, JWT-авторизацией и оформлением заказов.

Проект сделан как **модульный монолит** — один запускаемый сервис, но код разбит на модули (как в микросервисной архитектуре):

| Модуль | Пакет | За что отвечает |
|--------|-------|-----------------|
| user-service | `com.ecommerce.user` | Регистрация, вход, роли |
| product-service | `com.ecommerce.product` | CRUD товаров |
| cart-service | `com.ecommerce.cart` | Корзина покупок |
| order-service | `com.ecommerce.order` | Оформление заказов |

---

## Что нужно установить

1. **JDK 21** (или новее) — [Adoptium](https://adoptium.net/)
2. **Maven 3.9+** — [maven.apache.org](https://maven.apache.org/)
3. **Docker Desktop** — для PostgreSQL (или установите PostgreSQL 14 вручную)
4. *(Опционально)* **Postman** — для тестирования API

---

## Быстрый старт (пошагово)

### Шаг 1. Запустить базу данных

В корне проекта выполните:

```bash
docker compose up -d
```

Будет поднят PostgreSQL:
- хост: `localhost:5432`
- база: `ecommerce`
- логин/пароль: `ecommerce` / `ecommerce`

### Шаг 2. Запустить приложение

```bash
mvn spring-boot:run
```

Приложение стартует на **http://localhost:8080**

### Шаг 3. Открыть документацию API (Swagger)

В браузере: **http://localhost:8080/swagger-ui.html**

Здесь можно посмотреть все эндпоинты и отправить тестовые запросы.

### Шаг 4. Войти как администратор

При первом запуске создаётся админ:

| Поле | Значение |
|------|----------|
| Логин | `admin` |
| Пароль | `admin123` |

**Как получить токен:**

1. В Swagger найдите `POST /api/auth/login`
2. Отправьте тело:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
3. Скопируйте поле `token` из ответа
4. Нажмите кнопку **Authorize** в Swagger и вставьте: `Bearer ВАШ_ТОКЕН`

---

## Основные API-эндпоинты

### Товары (каталог)

| Метод | URL | Кто может |
|-------|-----|-----------|
| GET | `/api/products` | Все (гость) |
| GET | `/api/products/{id}` | Все |
| POST | `/api/products` | Админ |
| PUT | `/api/products/{id}` | Админ |
| DELETE | `/api/products/{id}` | Админ |

Параметры списка: `?name=телефон&categoryId=1&page=0&size=10&sort=price,desc`

### Авторизация

| Метод | URL | Описание |
|-------|-----|----------|
| POST | `/api/auth/register` | Регистрация |
| POST | `/api/auth/login` | Вход, получение JWT |

### Корзина (нужен токен)

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/api/cart` | Текущая корзина |
| POST | `/api/cart/add/{productId}` | Добавить товар |
| PUT | `/api/cart/update/{id}` | Изменить количество |
| DELETE | `/api/cart/remove/{id}` | Удалить позицию |

### Заказы

| Метод | URL | Описание |
|-------|-----|----------|
| POST | `/api/orders/checkout` | Оформить заказ |
| GET | `/api/orders` | Мои заказы |
| GET | `/api/orders/admin/all` | Все заказы (админ) |

### Пользователи (админ)

| Метод | URL |
|-------|-----|
| GET | `/api/users` |

---

## Типичный сценарий покупки

```
1. POST /api/auth/register   → создать аккаунт
2. POST /api/auth/login      → получить JWT
3. GET  /api/products        → выбрать товар
4. POST /api/cart/add/1      → положить в корзину (с токеном)
5. GET  /api/cart            → проверить сумму
6. POST /api/orders/checkout → оформить заказ
```

---

## Структура проекта

```
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
```

---

## Тесты

```bash
# Все тесты
mvn test

# Сборка + тесты
mvn clean verify
```

- **Юнит-тесты** — Mockito, без базы данных
- **Интеграционные** — Testcontainers + PostgreSQL (нужен Docker)

---

## CI/CD (GitHub Actions)

Файл: `.github/workflows/maven.yml`

При push в `main` / `master`:
1. Сборка проекта (`mvn clean verify`)
2. Сборка Docker-образа

---

## Безопасность

- **JWT** — токен в заголовке `Authorization: Bearer ...`
- **Роли** — `USER` (покупатель), `ADMIN` (управление)
- **CSRF** — отключён для REST API (стандарт для stateless JWT)
- **Пароли** — хранятся в виде BCrypt-хэша
- **HTTPS** — включайте на продакшене (nginx, облако)

Секрет JWT в продакшене задайте через переменную окружения:

```bash
set JWT_SECRET=ваш-длинный-секрет-минимум-32-символа
```

---

## Postman

Импортируйте файл: `postman/Ecommerce-API.postman_collection.json`

1. Запустите **Login** — токен сохранится автоматически
2. Остальные запросы используют переменную `{{token}}`

---

## Полезные ссылки

| Ресурс | URL |
|--------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |

---

## Стек технологий

- Spring Boot 3.4, Spring Security, Spring Data JPA
- PostgreSQL 14, Flyway
- JWT (jjwt), SpringDoc OpenAPI
- JUnit 5, Mockito, Testcontainers
- GitHub Actions, Docker

---

## Частые проблемы

**Ошибка подключения к БД**  
→ Проверьте, что `docker compose up -d` выполнен и PostgreSQL запущен.

**401 Unauthorized**  
→ Добавьте заголовок `Authorization: Bearer <token>`.

**403 Forbidden**  
→ Нужна роль ADMIN. Войдите как `admin` / `admin123`.

**Тесты с Testcontainers падают**  
→ Убедитесь, что Docker Desktop запущен.
