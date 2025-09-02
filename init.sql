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

INSERT INTO blog_entries (title, content, author, published)
VALUES
('The Future of Artificial Intelligence', 
 'Artificial intelligence is rapidly evolving, shaping industries from healthcare to finance. In the next decade, we may see AI embedded in every aspect of daily life, from personal assistants to medical diagnostics.', 
 'Michael', TRUE),

('Why I Love PostgreSQL', 
 'PostgreSQL is more than just an open-source database. It is robust, reliable, and packed with features like window functions, JSONB, and powerful indexing. It has been my go-to database for years.', 
 'Michael', TRUE),

('Minimalism in Software Development', 
 'In coding, less is often more. By writing clean, simple, and maintainable code, developers can save themselves countless hours of debugging and future refactoring.', 
 'Michael', FALSE),

('The Rise of Remote Work', 
 'The pandemic accelerated remote work adoption worldwide. Even now, remote and hybrid models are reshaping how companies think about productivity, collaboration, and office culture.', 
 'Michael', TRUE),

('Books Every Developer Should Read', 
 'Some books transcend time for developers: “Clean Code” by Robert C. Martin, “The Pragmatic Programmer” by Hunt and Thomas, and “Design Patterns” by the GoF are must-reads for any serious engineer.', 
 'Michael', TRUE),

('Why Learning Never Stops in Tech', 
 'Technology evolves quickly. Staying relevant requires continuous learning—whether it’s mastering a new framework, reading documentation, or experimenting with new programming paradigms.', 
 'Michael', FALSE),

('The Beauty of Open Source', 
 'Open-source projects show the true power of community. Developers from across the world collaborate, contribute, and create software that powers the internet itself.', 
 'Michael', TRUE),

('A Beginner’s Guide to Mindful Coding', 
 'Mindful coding means being intentional and focused while writing software. It helps reduce bugs, improve creativity, and keep the joy in development alive.', 
 'Michael', FALSE),

('The Importance of Testing Your Code', 
 'Automated testing isn’t just about catching bugs early. It gives developers confidence, speeds up deployment, and ensures that features work as intended even years later.', 
 'Michael', TRUE),

('My Journey as a Developer', 
 'From writing my first “Hello World” program to building full-stack applications, software engineering has been both challenging and rewarding. Every new project is a chance to learn and grow.', 
 'Michael', TRUE);


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
