ALTER TABLE add_cart_data
DROP FOREIGN KEY fk_add_cart_data_on_order;

ALTER TABLE add_cart_data
DROP INDEX uk_add_cart_data_order_id;

ALTER TABLE add_cart_data
ADD CONSTRAINT fk_add_cart_order
FOREIGN KEY (order_id) REFERENCES order_data(id);
