-- Migration: V1__Create_pessoas_table.sql
-- Description: Criação da tabela pessoas - Hold (Global Tech Holding) - MySQL

CREATE TABLE pessoas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NULL,
    data_nascimento DATE NULL,
    sexo VARCHAR(1) NOT NULL,
    altura DECIMAL(4, 2) NOT NULL,
    email VARCHAR(255) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_pessoas_nome ON pessoas(nome);
