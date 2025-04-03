# Database Status and Configuration

## Current Status (as of last check)

### Docker Container
- Container Name: mysql-container2
- Image: mysql:8.4.4
- Port Mapping: 3306:3306
- Status: Running

### Connection Details
- Host: localhost
- Port: 3306
- Default User: root
- Database Name: snake_game (to be created)

### Required Database Objects
1. Tables needed:
   - users
   - game_results
   - comments

### Current Issues
- Database 'snake_game' needs to be created
- Tables need to be created
- Connection configuration needs to be updated

## Next Steps
1. Create database:
   ```sql
   CREATE DATABASE snake_game;
   ```

2. Create tables:
   ```sql
   -- Users table
   CREATE TABLE users (
       id INT PRIMARY KEY AUTO_INCREMENT,
       username VARCHAR(50) NOT NULL UNIQUE,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

   -- Game Results table
   CREATE TABLE game_results (
       id INT PRIMARY KEY AUTO_INCREMENT,
       user_id INT NOT NULL,
       food1_score INT NOT NULL,
       food2_score INT NOT NULL,
       obstacle_count INT NOT NULL,
       game_duration BIGINT NOT NULL,
       end_time TIMESTAMP NOT NULL,
       was_ai_enabled BOOLEAN NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (user_id) REFERENCES users(id)
   );

   -- Comments table
   CREATE TABLE comments (
       id INT PRIMARY KEY AUTO_INCREMENT,
       user_id INT NOT NULL,
       game_result_id INT NOT NULL,
       content TEXT NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (user_id) REFERENCES users(id),
       FOREIGN KEY (game_result_id) REFERENCES game_results(id)
   );
   ```

3. Update configuration:
   - Modify `config.properties`:
     ```properties
     db.url=jdbc:mysql://localhost:3306/snake_game
     db.username=root
     db.password=your_password_here
     ```

## Connection Test Status
- [ ] Database exists
- [ ] Tables created
- [ ] Connection successful
- [ ] Queries working

## Recent Changes
- Docker container verified running
- Initial documentation created
- Schema design completed

## Troubleshooting Notes
If connection fails:
1. Check Docker container status
2. Verify database exists
3. Check user permissions
4. Test connection with: `mysql -h localhost -P 3306 -u root -p` 