INSERT INTO categories (category_id, category_name, updated_at)
VALUES (1, 'Alcohol', '2023-04-06 13:35:26.053203'),
       (2, 'Water', '2023-04-06 13:35:43.848');

INSERT INTO products (product_id, product_name, product_price, product_sku, product_category_id, updated_at)
VALUES (1, 'Jack Daniels 1L', 320, 'JKDSHNKN001', 1, '2023-04-06 13:35:27.50224'),
       (2, 'Jack Daniels 0.75L', 240, 'JKDSHNKN002', 1, '2023-04-06 13:35:29.70997'),
       (3, 'Jack Daniels 0.5L', 160, 'JKDSHNKN003', 1, '2023-04-06 13:35:32.738537'),
       (4, 'Jim Beam 1L', 280, 'JMBMHNKN001', 1, '2023-04-06 13:35:35.110158'),
       (5, 'Jim Beam 0.75L', 210, 'JMBMHNKN002', 1, '2023-04-06 13:35:37.723805'),
       (6, 'Jim Beam 0.5L', 140, 'JMBMHNKN003', 1, '2023-04-06 13:35:40.219902'),
       (7, 'Zubrowka 1L', 200, 'ZBRPLN001', 1, '2023-04-06 13:35:42.46526'),
       (8, 'Morshynska 0.5L', 10, 'MRSHUKR001', 1, '2023-04-06 13:35:45.439244'),
       (9, 'Coca Cola 1L', 20, 'CKCLPEP001', 2, '2023-04-06 13:35:48.335656'),
       (10, 'Coca Cola 0.5L', 11, 'CKCLPEP002', 2, '2023-04-06 13:35:51.74694'),
       (11, 'Pepsi Cola 1L', 20, 'PSCLPEP001', 2, '2023-04-06 13:35:53.497099'),
       (12, 'Pepsi Cola 0.5L', 10, 'PSCLPEP002', 2, '2023-04-06 13:35:54.988179');

INSERT INTO orders (order_id, order_amount, order_items, created_at, updated_at)
VALUES ('7a314b8c-5f55-4c8a-b4a7-f77b50b3940d', 1160,
        '[{"product_id": 1, "product_sku": "JKDSHNKN001", "product_name": "Jack Daniels 1L", "product_price": 320.0, "order_quantity": 2}, {"product_id": 8, "product_sku": "MRSHUKR001", "product_name": "Morshynska 0.5L", "product_price": 10.0, "order_quantity": 42}, {"product_id": 12, "product_sku": "PSCLPEP002", "product_name": "Pepsi Cola 0.5L", "product_price": 10.0, "order_quantity": 10}]',
        '2023-04-06 13:35:57.014082', '2023-04-06 13:35:57.014264');

INSERT INTO orders (order_id, order_amount, order_items, created_at, updated_at)
VALUES ('5a214b8c-5f55-4c8a-b4a7-f77b50b3940a', 4280,
        '[{"product_id": 2, "product_sku": "JKDSHNKN002", "product_name": "Jack Daniels 0.75L", "product_price": 240.0, "order_quantity": 4}, {"product_id": 3, "product_sku": "JKDSHNKN003", "product_name": "Jack Daniels 0.5L", "product_price": 160.0, "order_quantity": 20}, {"product_id": 11, "product_sku": "PSCLPEP001", "product_name": "Pepsi Cola 1L", "product_price": 20.0, "order_quantity": 6}]',
        '2023-04-06 13:35:59.122775', '2023-04-06 13:35:59.122821');