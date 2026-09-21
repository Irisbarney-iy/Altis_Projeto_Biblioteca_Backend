CREATE TABLE tb_loans (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    loan_date DATE NOT NULL,
    limit_term DATE NOT NULL,
    returned_date DATE,
    status VARCHAR(25) NOT NULL,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,

    CONSTRAINT fk_loans_user FOREIGN KEY (user_id) REFERENCES tb_users(id),
    CONSTRAINT fk_loans_book FOREIGN KEY (book_id) REFERENCES tb_books(id)
);