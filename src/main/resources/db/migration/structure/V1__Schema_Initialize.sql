CREATE TABLE roles
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    createdAt  DATETIME(6) NOT NULL,
    updatedAt  DATETIME(6),
    deletedAt  DATETIME(6),
    status     INTEGER,
    name       VARCHAR(255),
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

ALTER TABLE roles
    ADD CONSTRAINT uk_roles_name UNIQUE (name);

CREATE TABLE users
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    createdAt  DATETIME(6),
    updatedAt  DATETIME(6),
    deletedAt  DATETIME(6),
    status     INTEGER,
    role_id    BIGINT NOT NULL,
    email      VARCHAR(255),
    password   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uk_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT fk_users_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

CREATE TABLE add_cart_data
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    createdAt    DATETIME(6) NOT NULL,
    updatedAt    DATETIME(6),
    deletedAt    DATETIME(6),
    status       INTEGER,
    quantity     INTEGER NOT NULL,
    customer_id  BIGINT NOT NULL,
    dish_size_id BIGINT,
    extra_id     BIGINT,
    order_id     BIGINT,
    CONSTRAINT pk_add_cart_data PRIMARY KEY (id)
);

ALTER TABLE add_cart_data
    ADD CONSTRAINT uk_add_cart_data_order_id UNIQUE (order_id);

CREATE TABLE addresses
(
    id          BIGINT NOT NULL AUTO_INCREMENT,
    createdAt   DATETIME(6) NOT NULL,
    updatedAt   DATETIME(6),
    deletedAt   DATETIME(6),
    status      INTEGER,
    entity_type INTEGER,
    lat         DECIMAL(10, 8),
    `long`      DECIMAL(11, 8),
    entity_id   BIGINT,
    city        VARCHAR(255),
    region      VARCHAR(255),
    road        VARCHAR(255),
    street      VARCHAR(255),
    township    VARCHAR(255),
    CONSTRAINT pk_addresses PRIMARY KEY (id)
);

CREATE TABLE restaurants
(
    id               BIGINT NOT NULL AUTO_INCREMENT,
    createdAt        DATETIME(6) NOT NULL,
    updatedAt        DATETIME(6),
    deletedAt        DATETIME(6),
    status           INTEGER,
    res_owner_id     BIGINT NOT NULL,
    contact_number   VARCHAR(255),
    kpay_number      VARCHAR(255),
    nrc              VARCHAR(255),
    restaurant_image VARCHAR(255),
    restaurant_name  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_restaurants PRIMARY KEY (id)
);

ALTER TABLE restaurants
    ADD CONSTRAINT uk_restaurants_kpay_number UNIQUE (kpay_number);
ALTER TABLE restaurants
    ADD CONSTRAINT uk_restaurants_nrc UNIQUE (nrc);

ALTER TABLE restaurants
    ADD CONSTRAINT fk_restaurants_on_owner FOREIGN KEY (res_owner_id) REFERENCES users (id);

CREATE TABLE categories
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    createdAt  DATETIME(6) NOT NULL,
    updatedAt  DATETIME(6),
    deletedAt  DATETIME(6),
    status     INTEGER,
    res_id     BIGINT NOT NULL,
    name       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

ALTER TABLE categories
    ADD CONSTRAINT fk_categories_on_restaurant FOREIGN KEY (res_id) REFERENCES restaurants (id);

CREATE TABLE coupons
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    createdAt     DATETIME(6) NOT NULL,
    updatedAt     DATETIME(6),
    deletedAt     DATETIME(6),
    status        INTEGER,
    coupon_points DECIMAL(19, 4) NOT NULL,
    user_id       BIGINT NOT NULL,
    CONSTRAINT pk_coupons PRIMARY KEY (id)
);

ALTER TABLE coupons
    ADD CONSTRAINT fk_coupons_on_user FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE menus
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    createdAt  DATETIME(6) NOT NULL,
    updatedAt  DATETIME(6),
    deletedAt  DATETIME(6),
    status     INTEGER,
    price      DECIMAL(19, 4) NOT NULL,
    cate_id    BIGINT NOT NULL,
    res_id     BIGINT NOT NULL,
    dish       VARCHAR(255) NOT NULL,
    dish_img   VARCHAR(255),
    CONSTRAINT pk_menus PRIMARY KEY (id)
);

ALTER TABLE menus
    ADD CONSTRAINT fk_menus_on_category FOREIGN KEY (cate_id) REFERENCES categories (id);
ALTER TABLE menus
    ADD CONSTRAINT fk_menus_on_restaurant FOREIGN KEY (res_id) REFERENCES restaurants (id);

CREATE TABLE dish_sizes
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    createdAt     DATETIME(6) NOT NULL,
    updatedAt     DATETIME(6),
    deletedAt     DATETIME(6),
    status        INTEGER,
    price         DECIMAL(19, 4) NOT NULL,
    menu_id       BIGINT NOT NULL,
    dish_size_img VARCHAR(255),
    name          VARCHAR(255) NOT NULL,
    CONSTRAINT pk_dish_sizes PRIMARY KEY (id)
);

ALTER TABLE dish_sizes
    ADD CONSTRAINT fk_dish_sizes_on_menu FOREIGN KEY (menu_id) REFERENCES menus (id);

ALTER TABLE add_cart_data
    ADD CONSTRAINT fk_add_cart_data_on_dish_size FOREIGN KEY (dish_size_id) REFERENCES dish_sizes (id);

CREATE TABLE extra
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    createdAt  DATETIME(6) NOT NULL,
    updatedAt  DATETIME(6),
    deletedAt  DATETIME(6),
    status     INTEGER,
    price      DECIMAL(19, 4) NOT NULL,
    menu_id    BIGINT NOT NULL,
    name       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_extra PRIMARY KEY (id)
);

ALTER TABLE extra
    ADD CONSTRAINT fk_extra_on_menu FOREIGN KEY (menu_id) REFERENCES menus (id);

ALTER TABLE add_cart_data
    ADD CONSTRAINT fk_add_cart_data_on_extra FOREIGN KEY (extra_id) REFERENCES extra (id);

CREATE TABLE payment_data
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    createdAt          DATETIME(6) NOT NULL,
    updatedAt          DATETIME(6),
    deletedAt          DATETIME(6),
    status             INTEGER,
    coupon_id          BIGINT,
    date_time          DATETIME(6) NOT NULL,
    deliver_id         BIGINT,
    res_id             BIGINT NOT NULL,
    user_id            BIGINT NOT NULL,
    payment_screenshot VARCHAR(255),
    CONSTRAINT pk_payment_data PRIMARY KEY (id)
);

ALTER TABLE payment_data
    ADD CONSTRAINT uk_payment_data_coupon_id UNIQUE (coupon_id);

ALTER TABLE payment_data
    ADD CONSTRAINT fk_payment_data_on_coupon FOREIGN KEY (coupon_id) REFERENCES coupons (id);
ALTER TABLE payment_data
    ADD CONSTRAINT fk_payment_data_on_delivery_user FOREIGN KEY (deliver_id) REFERENCES users (id);
ALTER TABLE payment_data
    ADD CONSTRAINT fk_payment_data_on_restaurant FOREIGN KEY (res_id) REFERENCES restaurants (id);
ALTER TABLE payment_data
    ADD CONSTRAINT fk_payment_data_on_user FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE order_data
(
    id              BIGINT NOT NULL AUTO_INCREMENT,
    createdAt       DATETIME(6) NOT NULL,
    updatedAt       DATETIME(6),
    deletedAt       DATETIME(6),
    status          INTEGER,
    delivery_status INTEGER NOT NULL,
    total_amount    DECIMAL(19, 4) NOT NULL,
    order_date_time DATETIME(6) NOT NULL,
    payment_id      BIGINT NOT NULL,
    user_address    LONGTEXT NOT NULL,
    CONSTRAINT pk_order_data PRIMARY KEY (id)
);

ALTER TABLE order_data
    ADD CONSTRAINT uk_order_data_payment_id UNIQUE (payment_id);

ALTER TABLE order_data
    ADD CONSTRAINT fk_order_data_on_payment FOREIGN KEY (payment_id) REFERENCES payment_data (id);

ALTER TABLE add_cart_data
    ADD CONSTRAINT fk_add_cart_data_on_order FOREIGN KEY (order_id) REFERENCES order_data (id);

CREATE TABLE delivery_data
(
    id                BIGINT NOT NULL AUTO_INCREMENT,
    createdAt         DATETIME(6) NOT NULL,
    updatedAt         DATETIME(6),
    deletedAt         DATETIME(6),
    status            INTEGER,
    delivery_stuff_id BIGINT NOT NULL,
    order_id          BIGINT NOT NULL,
    CONSTRAINT pk_delivery_data PRIMARY KEY (id)
);

ALTER TABLE delivery_data
    ADD CONSTRAINT uk_delivery_data_order_id UNIQUE (order_id);

ALTER TABLE delivery_data
    ADD CONSTRAINT fk_delivery_data_on_delivery_stuff FOREIGN KEY (delivery_stuff_id) REFERENCES users (id);
ALTER TABLE delivery_data
    ADD CONSTRAINT fk_delivery_data_on_order FOREIGN KEY (order_id) REFERENCES order_data (id);

CREATE TABLE profiles
(
    id          BIGINT NOT NULL AUTO_INCREMENT,
    createdAt   DATETIME(6) NOT NULL,
    updatedAt   DATETIME(6),
    deletedAt   DATETIME(6),
    status      INTEGER,
    count       INT DEFAULT 0,
    dob         DATE,
    user_id     BIGINT NOT NULL,
    email       VARCHAR(255) NOT NULL,
    gender      VARCHAR(255),
    name        VARCHAR(255) NOT NULL,
    nrc         VARCHAR(255),
    phone       VARCHAR(255),
    profile_pic VARCHAR(255),
    address     LONGTEXT,
    CONSTRAINT pk_profiles PRIMARY KEY (id)
);

ALTER TABLE profiles
    ADD CONSTRAINT uk_profiles_user_id UNIQUE (user_id);
ALTER TABLE profiles
    ADD CONSTRAINT uk_profiles_email UNIQUE (email);
ALTER TABLE profiles
    ADD CONSTRAINT uk_profiles_nrc UNIQUE (nrc);
ALTER TABLE profiles
    ADD CONSTRAINT uk_profiles_phone UNIQUE (phone);

ALTER TABLE profiles
    ADD CONSTRAINT fk_profiles_on_user FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE ratings
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    createdAt     DATETIME(6) NOT NULL,
    updatedAt     DATETIME(6),
    deletedAt     DATETIME(6),
    status        INTEGER,
    rating_points INTEGER NOT NULL,
    delivery_id   BIGINT NOT NULL,
    user_id       BIGINT NOT NULL,
    CONSTRAINT pk_ratings PRIMARY KEY (id)
);

ALTER TABLE ratings
    ADD CONSTRAINT fk_ratings_on_delivery_user FOREIGN KEY (delivery_id) REFERENCES users (id);
ALTER TABLE ratings
    ADD CONSTRAINT fk_ratings_on_user FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE restaurant_vendors
(
    id            BIGINT NOT NULL AUTO_INCREMENT,
    createdAt     DATETIME(6) NOT NULL,
    updatedAt     DATETIME(6),
    deletedAt     DATETIME(6),
    status        INTEGER,
    delivery_id   BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    CONSTRAINT pk_restaurant_vendors PRIMARY KEY (id)
);

ALTER TABLE restaurant_vendors
    ADD CONSTRAINT fk_restaurant_vendors_on_delivery_user FOREIGN KEY (delivery_id) REFERENCES users (id);
ALTER TABLE restaurant_vendors
    ADD CONSTRAINT fk_restaurant_vendors_on_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id);