-- Create tables
CREATE TABLE product (
    product_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(300) NOT NULL,
    price BIGINT NOT NULL,
    stock BIGINT NOT NULL,
    sales_count BIGINT NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (product_id)
) ENGINE=InnoDB;

CREATE TABLE point (
    point_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    volume BIGINT NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (point_id)
) ENGINE=InnoDB;

CREATE TABLE point_history (
    point_history_id BIGINT NOT NULL AUTO_INCREMENT,
    point_id BIGINT NOT NULL,
    amount BIGINT NOT NULL,
    transaction_type ENUM('CHARGE', 'USE') NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (point_history_id)
) ENGINE=InnoDB;

CREATE TABLE coupon (
    coupon_id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255),
    discount_type ENUM('AMOUNT', 'PERCENT'),
    discount_value BIGINT,
    stock BIGINT,
    start_date DATETIME(6),
    end_date DATETIME(6),
    PRIMARY KEY (coupon_id)
) ENGINE=InnoDB;

CREATE TABLE user_coupon (
    user_coupon_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    coupon_id BIGINT,
    is_used BIT NOT NULL,
    expired_at DATETIME(6),
    PRIMARY KEY (user_coupon_id)
) ENGINE=InnoDB;

CREATE TABLE orders (
    order_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    total_amount BIGINT,
    status ENUM('CANCELED', 'COMPLETED', 'FAILED', 'PENDING', 'WAITING'),
    is_coupon_applied BIT NOT NULL,
    user_coupon_id BIGINT,
    PRIMARY KEY (order_id)
) ENGINE=InnoDB;

CREATE TABLE order_product (
    order_product_id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT,
    product_id BIGINT,
    quantity BIGINT,
    unit_price BIGINT,
    PRIMARY KEY (order_product_id)
) ENGINE=InnoDB;

CREATE TABLE payment (
    payment_id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT,
    amount BIGINT,
    payment_method ENUM('CARD', 'CASH', 'POINT'),
    status ENUM('APPROVED', 'CANCELED', 'FAILED', 'PENDING', 'REFUNDED'),
    idempotency_key VARCHAR(255),
    approved_at DATETIME(6),
    canceled_at DATETIME(6),
    PRIMARY KEY (payment_id)
) ENGINE=InnoDB;

CREATE TABLE bestseller (
    bestseller_id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT,
    name VARCHAR(255),
    price BIGINT,
    ranking BIGINT,
    top_date DATETIME(6),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (bestseller_id)
) ENGINE=InnoDB;

CREATE TABLE coupon_outbox_event (
    id BIGINT NOT NULL AUTO_INCREMENT,
    event_type VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    status ENUM('FAILED', 'PENDING', 'PROCESSED') NOT NULL,
    retry_count BIGINT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB;