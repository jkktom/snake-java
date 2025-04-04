-- Create the database
CREATE DATABASE SNAKE_GAME;

-- Create the user with password that can connect from any host
CREATE USER 'gorilla'@'%' IDENTIFIED BY 'gorilla';

-- Grant all privileges on the SNAKE_GAME database to the user
GRANT ALL PRIVILEGES ON SNAKE_GAME.* TO 'gorilla'@'%';

-- Apply the privilege changes
FLUSH PRIVILEGES;

-- Verify user creation and privileges
SELECT User, Host FROM mysql.user WHERE User='gorilla';
SHOW GRANTS FOR 'gorilla'@'%';