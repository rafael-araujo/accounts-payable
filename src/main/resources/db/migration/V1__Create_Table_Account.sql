-- Criação da tabela de status
CREATE TABLE status_account (
    status_id SERIAL PRIMARY KEY,
    description VARCHAR(50) NOT NULL
);

-- Inserção de status iniciais
INSERT INTO status_account (description) VALUES ('Em aberto');
INSERT INTO status_account (description) VALUES ('Pago');
INSERT INTO status_account (description) VALUES ('Em atraso');
INSERT INTO status_account (description) VALUES ('Cancelado');

-- Criação da tabela de contas
CREATE TABLE account (
    account_id SERIAL PRIMARY KEY,
    due_date DATE NOT NULL,
    payment_date DATE,
    value DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    status_id INT NOT NULL,
    CONSTRAINT fk_status FOREIGN KEY (status_id) REFERENCES status_account(status_id)
);