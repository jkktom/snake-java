-- First create the database
CREATE DATABASE SNAKE_GAME;

-- Grant permissions to the new user, but only for the snake_game database
GRANT ALL PRIVILEGES ON SNAKE_GAME.* TO 'gorilla'@'%';

-- Apply the privileges
FLUSH PRIVILEGES;