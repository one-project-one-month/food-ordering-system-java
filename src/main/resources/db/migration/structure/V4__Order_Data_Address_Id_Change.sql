ALTER TABLE order_data
DROP COLUMN user_address;

ALTER TABLE order_data
ADD COLUMN address_id BIGINT NOT NULL;

ALTER TABLE order_data
ADD CONSTRAINT fk_order_address
FOREIGN KEY (address_id) REFERENCES addresses(id);