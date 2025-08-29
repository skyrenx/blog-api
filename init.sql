-- Drop tables if they exist
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS blog_entries;

-- Create "users" table
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password CHAR(68) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create "authorities" table
CREATE TABLE authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
    CONSTRAINT uq_authority UNIQUE (username, authority)
);

-- Create "blog_entries" table
CREATE TABLE blog_entries (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published BOOLEAN NOT NULL DEFAULT FALSE
);

-- Insert example users
INSERT INTO users (username, password, enabled) VALUES
('john', '$2a$12$ozqDQuYgkq3MbWxxwCsjmu8agtJRto6fU.CUYQgW5OEz57Y6yohAi', TRUE),
('mary', '$2a$12$ozqDQuYgkq3MbWxxwCsjmu8agtJRto6fU.CUYQgW5OEz57Y6yohAi', TRUE),
('michael', '$2a$12$ozqDQuYgkq3MbWxxwCsjmu8agtJRto6fU.CUYQgW5OEz57Y6yohAi', TRUE);

-- Insert authorities
INSERT INTO authorities (username, authority) VALUES
('john', 'ROLE_EMPLOYEE'),
('mary', 'ROLE_EMPLOYEE'),
('mary', 'ROLE_MANAGER'),
('michael', 'ROLE_EMPLOYEE'),
('michael', 'ROLE_MANAGER'),
('michael', 'ROLE_ADMIN');
