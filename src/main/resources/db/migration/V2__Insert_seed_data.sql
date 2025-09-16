-- Insert seed data
INSERT INTO product (name, description, price, stock, sales_count, created_at, updated_at) VALUES ('iPhone 15 Pro', 'Apple 최신 스마트폰', 1200000, 100, 50, NOW(), NOW());
INSERT INTO product (name, description, price, stock, sales_count, created_at, updated_at) VALUES ('Galaxy S24 Ultra', 'Samsung 플래그십 스마트폰', 1300000, 80, 45, NOW(), NOW());
INSERT INTO product (name, description, price, stock, sales_count, created_at, updated_at) VALUES ('MacBook Pro M3', 'Apple 노트북 최신 모델', 2500000, 30, 25, NOW(), NOW());
INSERT INTO product (name, description, price, stock, sales_count, created_at, updated_at) VALUES ('iPad Air', 'Apple 태블릿', 800000, 60, 35, NOW(), NOW());
INSERT INTO product (name, description, price, stock, sales_count, created_at, updated_at) VALUES ('AirPods Pro', 'Apple 무선 이어폰', 300000, 200, 120, NOW(), NOW());
INSERT INTO product (name, description, price, stock, sales_count, created_at, updated_at) VALUES ('Galaxy Watch', 'Samsung 스마트워치', 400000, 150, 80, NOW(), NOW());
INSERT INTO product (name, description, price, stock, sales_count, created_at, updated_at) VALUES ('Nintendo Switch', '닌텐도 게임 콘솔', 350000, 90, 60, NOW(), NOW());
INSERT INTO product (name, description, price, stock, sales_count, created_at, updated_at) VALUES ('Sony WH-1000XM5', '소니 노이즈캔슬링 헤드폰', 450000, 70, 40, NOW(), NOW());
INSERT INTO product (name, description, price, stock, sales_count, created_at, updated_at) VALUES ('Dell XPS 13', 'Dell 울트라북', 1800000, 40, 20, NOW(), NOW());
INSERT INTO product (name, description, price, stock, sales_count, created_at, updated_at) VALUES ('LG 그램 17', 'LG 경량 노트북', 2000000, 35, 15, NOW(), NOW());

INSERT INTO point (user_id, volume, created_at, updated_at) VALUES (1, 50000, NOW(), NOW());
INSERT INTO point (user_id, volume, created_at, updated_at) VALUES (2, 75000, NOW(), NOW());
INSERT INTO point (user_id, volume, created_at, updated_at) VALUES (3, 30000, NOW(), NOW());
INSERT INTO point (user_id, volume, created_at, updated_at) VALUES (4, 100000, NOW(), NOW());
INSERT INTO point (user_id, volume, created_at, updated_at) VALUES (5, 25000, NOW(), NOW());
INSERT INTO point (user_id, volume, created_at, updated_at) VALUES (6, 80000, NOW(), NOW());
INSERT INTO point (user_id, volume, created_at, updated_at) VALUES (7, 60000, NOW(), NOW());
INSERT INTO point (user_id, volume, created_at, updated_at) VALUES (8, 40000, NOW(), NOW());
INSERT INTO point (user_id, volume, created_at, updated_at) VALUES (9, 90000, NOW(), NOW());
INSERT INTO point (user_id, volume, created_at, updated_at) VALUES (10, 35000, NOW(), NOW());

INSERT INTO point_history (point_id, amount, transaction_type, created_at, updated_at) VALUES (1, 50000, 'CHARGE', NOW(), NOW());
INSERT INTO point_history (point_id, amount, transaction_type, created_at, updated_at) VALUES (2, 75000, 'CHARGE', NOW(), NOW());
INSERT INTO point_history (point_id, amount, transaction_type, created_at, updated_at) VALUES (3, 30000, 'CHARGE', NOW(), NOW());
INSERT INTO point_history (point_id, amount, transaction_type, created_at, updated_at) VALUES (4, 100000, 'CHARGE', NOW(), NOW());
INSERT INTO point_history (point_id, amount, transaction_type, created_at, updated_at) VALUES (5, 25000, 'CHARGE', NOW(), NOW());
INSERT INTO point_history (point_id, amount, transaction_type, created_at, updated_at) VALUES (6, 80000, 'CHARGE', NOW(), NOW());
INSERT INTO point_history (point_id, amount, transaction_type, created_at, updated_at) VALUES (7, 60000, 'CHARGE', NOW(), NOW());
INSERT INTO point_history (point_id, amount, transaction_type, created_at, updated_at) VALUES (8, 40000, 'CHARGE', NOW(), NOW());
INSERT INTO point_history (point_id, amount, transaction_type, created_at, updated_at) VALUES (9, 90000, 'CHARGE', NOW(), NOW());
INSERT INTO point_history (point_id, amount, transaction_type, created_at, updated_at) VALUES (10, 35000, 'CHARGE', NOW(), NOW());

INSERT INTO coupon (title, discount_type, discount_value, stock, start_date, end_date) VALUES ('신규가입 10% 할인', 'PERCENT', 10, 1000, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO coupon (title, discount_type, discount_value, stock, start_date, end_date) VALUES ('5만원 이상 구매시 5천원 할인', 'AMOUNT', 5000, 500, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO coupon (title, discount_type, discount_value, stock, start_date, end_date) VALUES ('VIP 회원 15% 할인', 'PERCENT', 15, 200, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO coupon (title, discount_type, discount_value, stock, start_date, end_date) VALUES ('첫 구매 1만원 할인', 'AMOUNT', 10000, 300, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO coupon (title, discount_type, discount_value, stock, start_date, end_date) VALUES ('주말 특가 20% 할인', 'PERCENT', 20, 100, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY));
INSERT INTO coupon (title, discount_type, discount_value, stock, start_date, end_date) VALUES ('생일축하 3만원 할인', 'AMOUNT', 30000, 50, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO coupon (title, discount_type, discount_value, stock, start_date, end_date) VALUES ('재구매 고객 12% 할인', 'PERCENT', 12, 400, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO coupon (title, discount_type, discount_value, stock, start_date, end_date) VALUES ('대량구매 2만원 할인', 'AMOUNT', 20000, 150, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO coupon (title, discount_type, discount_value, stock, start_date, end_date) VALUES ('멤버십 25% 할인', 'PERCENT', 25, 80, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO coupon (title, discount_type, discount_value, stock, start_date, end_date) VALUES ('특별이벤트 1만5천원 할인', 'AMOUNT', 15000, 250, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY));

INSERT INTO user_coupon (user_id, coupon_id, is_used, expired_at) VALUES (1, 1, false, DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO user_coupon (user_id, coupon_id, is_used, expired_at) VALUES (2, 2, false, DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO user_coupon (user_id, coupon_id, is_used, expired_at) VALUES (3, 3, false, DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO user_coupon (user_id, coupon_id, is_used, expired_at) VALUES (4, 4, false, DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO user_coupon (user_id, coupon_id, is_used, expired_at) VALUES (5, 5, false, DATE_ADD(NOW(), INTERVAL 7 DAY));
INSERT INTO user_coupon (user_id, coupon_id, is_used, expired_at) VALUES (6, 6, false, DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO user_coupon (user_id, coupon_id, is_used, expired_at) VALUES (7, 7, false, DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO user_coupon (user_id, coupon_id, is_used, expired_at) VALUES (8, 8, false, DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO user_coupon (user_id, coupon_id, is_used, expired_at) VALUES (9, 9, false, DATE_ADD(NOW(), INTERVAL 30 DAY));
INSERT INTO user_coupon (user_id, coupon_id, is_used, expired_at) VALUES (10, 10, false, DATE_ADD(NOW(), INTERVAL 30 DAY));

INSERT INTO orders (user_id, total_amount, status, is_coupon_applied, user_coupon_id) VALUES (1, 1200000, 'COMPLETED', false, NULL);
INSERT INTO orders (user_id, total_amount, status, is_coupon_applied, user_coupon_id) VALUES (2, 1300000, 'COMPLETED', true, 2);
INSERT INTO orders (user_id, total_amount, status, is_coupon_applied, user_coupon_id) VALUES (3, 2500000, 'PENDING', false, NULL);
INSERT INTO orders (user_id, total_amount, status, is_coupon_applied, user_coupon_id) VALUES (4, 800000, 'COMPLETED', true, 4);
INSERT INTO orders (user_id, total_amount, status, is_coupon_applied, user_coupon_id) VALUES (5, 300000, 'WAITING', false, NULL);
INSERT INTO orders (user_id, total_amount, status, is_coupon_applied, user_coupon_id) VALUES (6, 400000, 'COMPLETED', false, NULL);
INSERT INTO orders (user_id, total_amount, status, is_coupon_applied, user_coupon_id) VALUES (7, 350000, 'CANCELED', false, NULL);
INSERT INTO orders (user_id, total_amount, status, is_coupon_applied, user_coupon_id) VALUES (8, 450000, 'COMPLETED', true, 8);
INSERT INTO orders (user_id, total_amount, status, is_coupon_applied, user_coupon_id) VALUES (9, 1800000, 'PENDING', false, NULL);
INSERT INTO orders (user_id, total_amount, status, is_coupon_applied, user_coupon_id) VALUES (10, 2000000, 'COMPLETED', true, 10);

INSERT INTO order_product (order_id, product_id, quantity, unit_price) VALUES (1, 1, 1, 1200000);
INSERT INTO order_product (order_id, product_id, quantity, unit_price) VALUES (2, 2, 1, 1300000);
INSERT INTO order_product (order_id, product_id, quantity, unit_price) VALUES (3, 3, 1, 2500000);
INSERT INTO order_product (order_id, product_id, quantity, unit_price) VALUES (4, 4, 1, 800000);
INSERT INTO order_product (order_id, product_id, quantity, unit_price) VALUES (5, 5, 1, 300000);
INSERT INTO order_product (order_id, product_id, quantity, unit_price) VALUES (6, 6, 1, 400000);
INSERT INTO order_product (order_id, product_id, quantity, unit_price) VALUES (7, 7, 1, 350000);
INSERT INTO order_product (order_id, product_id, quantity, unit_price) VALUES (8, 8, 1, 450000);
INSERT INTO order_product (order_id, product_id, quantity, unit_price) VALUES (9, 9, 1, 1800000);
INSERT INTO order_product (order_id, product_id, quantity, unit_price) VALUES (10, 10, 1, 2000000);

INSERT INTO payment (order_id, amount, payment_method, status, idempotency_key, approved_at) VALUES (1, 1200000, 'CARD', 'APPROVED', 'pay_001', NOW());
INSERT INTO payment (order_id, amount, payment_method, status, idempotency_key, approved_at) VALUES (2, 1295000, 'CARD', 'APPROVED', 'pay_002', NOW());
INSERT INTO payment (order_id, amount, payment_method, status, idempotency_key, approved_at) VALUES (4, 790000, 'POINT', 'APPROVED', 'pay_004', NOW());
INSERT INTO payment (order_id, amount, payment_method, status, idempotency_key, approved_at) VALUES (6, 400000, 'CARD', 'APPROVED', 'pay_006', NOW());
INSERT INTO payment (order_id, amount, payment_method, status, idempotency_key, canceled_at) VALUES (7, 350000, 'CARD', 'CANCELED', 'pay_007', NOW());
INSERT INTO payment (order_id, amount, payment_method, status, idempotency_key, approved_at) VALUES (8, 430000, 'CARD', 'APPROVED', 'pay_008', NOW());
INSERT INTO payment (order_id, amount, payment_method, status, idempotency_key, approved_at) VALUES (10, 1985000, 'CARD', 'APPROVED', 'pay_010', NOW());
INSERT INTO payment (order_id, amount, payment_method, status, idempotency_key) VALUES (3, 2500000, 'CARD', 'PENDING', 'pay_003');
INSERT INTO payment (order_id, amount, payment_method, status, idempotency_key) VALUES (5, 300000, 'POINT', 'PENDING', 'pay_005');
INSERT INTO payment (order_id, amount, payment_method, status, idempotency_key) VALUES (9, 1800000, 'CARD', 'PENDING', 'pay_009');

INSERT INTO bestseller (product_id, name, price, ranking, top_date, created_at, updated_at) VALUES (5, 'AirPods Pro', 300000, 1, NOW(), NOW(), NOW());
INSERT INTO bestseller (product_id, name, price, ranking, top_date, created_at, updated_at) VALUES (6, 'Galaxy Watch', 400000, 2, NOW(), NOW(), NOW());
INSERT INTO bestseller (product_id, name, price, ranking, top_date, created_at, updated_at) VALUES (7, 'Nintendo Switch', 350000, 3, NOW(), NOW(), NOW());
INSERT INTO bestseller (product_id, name, price, ranking, top_date, created_at, updated_at) VALUES (1, 'iPhone 15 Pro', 1200000, 4, NOW(), NOW(), NOW());
INSERT INTO bestseller (product_id, name, price, ranking, top_date, created_at, updated_at) VALUES (2, 'Galaxy S24 Ultra', 1300000, 5, NOW(), NOW(), NOW());
INSERT INTO bestseller (product_id, name, price, ranking, top_date, created_at, updated_at) VALUES (8, 'Sony WH-1000XM5', 450000, 6, NOW(), NOW(), NOW());
INSERT INTO bestseller (product_id, name, price, ranking, top_date, created_at, updated_at) VALUES (4, 'iPad Air', 800000, 7, NOW(), NOW(), NOW());
INSERT INTO bestseller (product_id, name, price, ranking, top_date, created_at, updated_at) VALUES (3, 'MacBook Pro M3', 2500000, 8, NOW(), NOW(), NOW());
INSERT INTO bestseller (product_id, name, price, ranking, top_date, created_at, updated_at) VALUES (9, 'Dell XPS 13', 1800000, 9, NOW(), NOW(), NOW());
INSERT INTO bestseller (product_id, name, price, ranking, top_date, created_at, updated_at) VALUES (10, 'LG 그램 17', 2000000, 10, NOW(), NOW(), NOW());