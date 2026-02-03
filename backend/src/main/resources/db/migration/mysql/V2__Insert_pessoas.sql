-- Migration: V2__Insert_pessoas.sql
-- Description: Inserção de 10 pessoas iniciais - Hold (Global Tech Holding)

INSERT INTO pessoas (nome, cpf, data_nascimento, sexo, altura, email) VALUES
('Ana Silva', '111.222.333-44', '1990-03-15', 'F', 1.65, 'ana.silva@email.com'),
('Bruno Santos', '222.333.444-55', '1985-07-22', 'M', 1.78, 'bruno.santos@email.com'),
('Carla Oliveira', '333.444.555-66', '1992-11-08', 'F', 1.62, 'carla.oliveira@email.com'),
('Diego Costa', '444.555.666-77', '1988-01-30', 'M', 1.82, 'diego.costa@email.com'),
('Elena Ferreira', '555.666.777-88', '1995-09-12', 'F', 1.70, 'elena.ferreira@email.com'),
('Fernando Lima', '666.777.888-99', '1982-04-20', 'M', 1.75, 'fernando.lima@email.com'),
('Gabriela Souza', '777.888.999-00', '1993-08-05', 'F', 1.68, 'gabriela.souza@email.com'),
('Henrique Alves', '888.999.000-11', '1987-12-14', 'M', 1.80, 'henrique.alves@email.com'),
('Isabela Martins', '999.000.111-22', '1991-06-28', 'F', 1.63, 'isabela.martins@email.com'),
('João Pereira', '000.111.222-33', '1989-10-03', 'M', 1.76, 'joao.pereira@email.com');
