UPDATE products SET name = 'Смартфон X1', description = 'смартфон POCO ', price = 1800.00, stock = 50, category_id = 1
WHERE name = 'Смартфон X1';

UPDATE products SET description = 'Мощный ноутбук Apple pro 14', price = 3200.00, stock = 20, category_id = 1
WHERE name = 'Ноутбук Pro';

UPDATE products SET name = 'Футболка Puma', description = 'Хлопковая футболка', price = 100.00, stock = 100, category_id = 2
WHERE name IN ('Футболка Basic', 'Футболка Puma');

UPDATE products SET description = 'Учебник по программированию', price = 89.00, stock = 30, category_id = 3
WHERE name = 'Java для начинающих';
