-- Create User table
CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255),
                      email VARCHAR(255)
);

-- Create Product table
CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         description VARCHAR(255),
                         status VARCHAR(50),
                         image_url VARCHAR(255),
                         owner_id BIGINT,
                         CONSTRAINT fk_product_owner FOREIGN KEY (owner_id) REFERENCES user(id)
);

-- Create Offer table
CREATE TABLE offer (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255),
                       description VARCHAR(255)
);

-- Join table for Offer and Product (Many-to-Many)
CREATE TABLE offer_products (
                                offer_id BIGINT NOT NULL,
                                product_id BIGINT NOT NULL,
                                PRIMARY KEY (offer_id, product_id),
                                CONSTRAINT fk_offer FOREIGN KEY (offer_id) REFERENCES offer(id) ON DELETE CASCADE,
                                CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);
