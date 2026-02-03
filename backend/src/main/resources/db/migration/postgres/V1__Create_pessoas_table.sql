-- Migration: V1__Create_pessoas_table.sql
-- Description: Criação da tabela pessoas - Hold (Global Tech Holding)

CREATE TABLE pessoas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14),
    data_nascimento DATE,
    sexo VARCHAR(1) NOT NULL,
    altura DECIMAL(4, 2) NOT NULL,
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_pessoas_nome ON pessoas(nome);
