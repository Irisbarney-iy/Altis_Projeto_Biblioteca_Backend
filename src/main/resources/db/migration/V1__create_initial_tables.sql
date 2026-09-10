CREATE TABLE tb_users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    birth_date DATE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    admin BOOLEAN NOT NULL DEFAULT FALSE,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP
);

CREATE TABLE tb_publishers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    site VARCHAR(200),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP
);

CREATE TABLE tb_books (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    ano_lancamento INTEGER NOT NULL,
    quantidade_total INTEGER NOT NULL,
    quantidade_em_uso INTEGER NOT NULL DEFAULT 0,
    publisher_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP,
    CONSTRAINT fk_books_publisher FOREIGN KEY (publisher_id) REFERENCES tb_publishers(id)
);

CREATE TABLE tb_loans (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    data_emprestimo DATE NOT NULL,
    prazo_limite DATE NOT NULL,
    data_devolucao DATE,
    status VARCHAR(25),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP,
    CONSTRAINT fk_loans_user FOREIGN KEY (user_id) REFERENCES tb_users(id),
    CONSTRAINT fk_loans_book FOREIGN KEY (book_id) REFERENCES tb_books(id)
);