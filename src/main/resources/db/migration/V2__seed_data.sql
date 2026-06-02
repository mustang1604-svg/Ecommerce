INSERT INTO categories (name) VALUES ('Электроника'), ('Одежда'), ('Книги');

INSERT INTO products (name, description, price, stock, category_id) VALUES
    ('Смартфон X1', 'смартфон POCO ', 1800.00, 50, 1),
    ('Ноутбук Pro', 'Мощный ноутбук Apple pro 14', 3200.00, 20, 1),
    ('Футболка Puma', 'Хлопковая футболка', 100.00, 100, 2),
    ('Java для начинающих', 'Учебник по программированию', 89.00, 30, 3);
