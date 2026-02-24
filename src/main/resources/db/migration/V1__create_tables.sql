-- Garante uso do schema padrão do H2
CREATE SCHEMA IF NOT EXISTS PUBLIC;
SET SCHEMA PUBLIC;

CREATE TABLE users (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    role varchar(40) NOT NULL
);


INSERT INTO users(name, email,role) VALUES (
'Pedro Rocha','pedro.rsilva@sptech.school','ADMIN');