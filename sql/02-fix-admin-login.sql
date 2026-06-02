USE employee_db;

UPDATE users
SET email = 'admin@gmail.com', password = 'admin123'
WHERE username = 'admin';

INSERT INTO users (username, password, email)
SELECT 'admin', 'admin123', 'admin@gmail.com'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');
