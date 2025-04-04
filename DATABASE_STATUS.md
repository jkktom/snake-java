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

## Step-by-Step Database Creation Guide

### 1. Start Docker Container (if not running)
```bash
# Check if container is running
docker ps | grep mysql-container2

# If not running, start the container
docker start mysql-container2

# If container doesn't exist, create it
docker run --name mysql-container2 -e MYSQL_ROOT_PASSWORD=your_password_here -p 3306:3306 -d mysql:8.4.4
```

### 2. Access MySQL Server
```bash
# Connect to MySQL server
mysql -h localhost -P 3306 -u root -p
# Enter your password when prompted
```

### 3. Create and Setup Database
```sql
-- Create database
CREATE DATABASE snake_game;

-- Switch to the new database
USE snake_game;

-- Create users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create game_results table
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

-- Create comments table
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

### 4. Verify Setup
```sql
-- Show all tables
SHOW TABLES;

-- Verify table structures
DESCRIBE users;
DESCRIBE game_results;
DESCRIBE comments;
```

### 5. Test Connections
```bash
# Test connection from command line
mysql -h localhost -P 3306 -u root -p snake_game -e "SHOW TABLES;"

# Expected output should show:
# +---------------------+
# | Tables_in_snake_game|
# +---------------------+
# | comments           |
# | game_results       |
# | users              |
# +---------------------+
```

### 6. Update Application Configuration
1. Modify `src/main/resources/config.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/snake_game
db.username=root
db.password=your_password_here
```

2. Verify application connection:
- Run the application
- Check for successful database connection message
- Try accessing the user management menu to verify database operations

### Troubleshooting
If you encounter issues:
1. Verify Docker container status:
   ```bash
   docker ps | grep mysql-container2
   ```
2. Check MySQL logs:
   ```bash
   docker logs mysql-container2
   ```
3. Test direct connection:
   ```bash
   # If using MariaDB client (common on Linux distributions)
   mariadb -h localhost -P 3306 -u root -p --ssl=0
   # Or use the mysql command (if available)
   mysql -h localhost -P 3306 -u root -p --ssl=0
   ```

### Allowing Remote Connections to Your Docker MySQL Database

To connect to your MySQL database running in Docker from another PC on the same network, follow these steps:

1. **Configure MySQL Docker Container**
   
   If you need to recreate your Docker container with proper network settings:
   ```bash
   # Stop and remove existing container
   docker stop mysql-container2
   docker rm mysql-container2
   
   # Create new container with bind to all interfaces
   docker run --name mysql-container2 \
     -e MYSQL_ROOT_PASSWORD=your_password_here \
     -p 0.0.0.0:3306:3306 \
     -d mysql:8.4.4
   ```

2. **Configure Firewall to Allow MySQL Port**
   ```bash
   # For systems with ufw (Ubuntu, etc.)
   sudo ufw allow 3306/tcp
   
   # For systems with firewalld (Fedora, CentOS, etc.)
   sudo firewalld-cmd --permanent --add-port=3306/tcp
   sudo firewalld-cmd --reload
   
   # For Arch Linux with iptables
   sudo iptables -A INPUT -p tcp --dport 3306 -j ACCEPT
   sudo iptables-save | sudo tee /etc/iptables/iptables.rules
   ```

3. **Create a Remote User with Host %**
   This is an important step. You need a user that can connect from any host:
   ```sql
   CREATE USER 'gorilla'@'%' IDENTIFIED BY 'gorilla';
   GRANT ALL PRIVILEGES ON SNAKE_GAME.* TO 'gorilla'@'%';
   FLUSH PRIVILEGES;
   ```

4. **Configure MySQL to Accept Remote Connections**
   You may need to modify MySQL configuration to bind to all interfaces.
   For a Docker container, this is usually already done, but you can verify with:
   ```bash
   docker exec -it mysql-container2 cat /etc/mysql/my.cnf
   ```
   
   Look for `bind-address = 0.0.0.0` or `bind-address = *` or make sure it's not set to `127.0.0.1` only.

5. **Test Remote Connection**
   From another machine on the same network:
   ```bash
   mysql -h 192.168.0.214 -P 3306 -u gorilla -p
   ```
   When prompted, enter the password: `gorilla`

6. **Update Client Configuration**
   On the client machine, create a `config.properties` file with:
   ```properties
   db.url=jdbc:mysql://192.168.0.214:3306/SNAKE_GAME
   db.username=gorilla
   db.password=gorilla
   ```

7. **Troubleshooting Connection Issues**
   If you can't connect:
   - Check that the MySQL service is running: `docker ps | grep mysql-container2`
   - Ensure your firewall is allowing the connection
   - Verify network connectivity: `ping 192.168.0.214`
   - Check MySQL logs: `docker logs mysql-container2`
   - Try connecting locally first to ensure the user has proper permissions

### Security Considerations
- Using a password like 'gorilla' is only for development environments
- For production:
  - Use a strong password
  - Consider using SSL for connections
  - Limit user privileges to only what's needed
  - Use a VPN if connecting over the internet
  - Consider restricting the user to specific hosts rather than '%'
