CREATE TABLE tb_books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100) NOT NULL,
    release_year INTEGER NOT NULL,
    total_quantity INTEGER NOT NULL,
    in_use_quantity INTEGER NOT NULL DEFAULT 0,
    publisher_id BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP,
    CONSTRAINT fk_books_publisher FOREIGN KEY (publisher_id) REFERENCES tb_publishers(id),
    CONSTRAINT chk_quantity_valid CHECK (total_quantity >= in_use_quantity AND in_use_quantity >= 0 AND total_quantity >= 0)
);