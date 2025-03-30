import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    public static class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Tile) {
                Tile other = (Tile) obj;
                return this.x == other.x && this.y == other.y;
            }
            return false;
        }
    }  

    // Make these package-private so AI can access them
    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    
    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food1;
    Tile food2;
    Random random;
    
    //obstacles
    ArrayList<Tile> obstacles;
    private static final int MAX_OBSTACLES = 50;
    private static final int OBSTACLE_SPAWN_CHANCE = 101; // percentage chance per food collection

    //game logic
    int velocityX;
    int velocityY;
    Timer gameLoop;
    long startTime;
    LocalDateTime endTime;
    int food1Score = 0;
    int food2Score = 0;

    boolean gameOver = false;
    
    // Add AI controller and player name
    private SnakeAI ai;
    private String playerName;
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        
        // Get player name and validate
        playerName = null;
        while (playerName == null || playerName.trim().isEmpty()) {
            playerName = JOptionPane.showInputDialog(null, 
                "Enter your name to start the game:", 
                "Snake Game", 
                JOptionPane.QUESTION_MESSAGE);
            
            if (playerName == null) {
                JOptionPane.showMessageDialog(null, 
                    "Game cannot start without a player name.\nExiting game.", 
                    "Game Exit", 
                    JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
            
            playerName = playerName.trim();
            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "Please enter a valid name.", 
                    "Invalid Name", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }
        
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        obstacles = new ArrayList<Tile>();
        
        for (int i = 0; i < 4; i++) {
            snakeBody.add(new Tile(4 - i, 5));
        }

        food1 = new Tile(10, 10);
        food2 = new Tile(15, 15);
        random = new Random();
        placeFood();

        velocityX = 1;
        velocityY = 0;
        
        startTime = System.currentTimeMillis();
        gameLoop = new Timer(100, this);
        gameLoop.start();
        
        // Initialize AI and enable by default
        ai = new SnakeAI(this);
        ai.toggle();
        
        // Show initial message about controls
        JOptionPane.showMessageDialog(null, 
            "Welcome " + playerName + "!\n" +
            "AI is enabled by default.\n" +
            "Press 'A' to disable AI and play manually.\n" +
            "Use arrow keys when playing manually.\n" +
            "Watch out for obstacles that appear when collecting food!",
            "Game Controls",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
        //Grid Lines
        for(int i = 0; i < boardWidth/tileSize; i++) {
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize); 
        }

        //Obstacles
        g.setColor(Color.gray);
        for (Tile obstacle : obstacles) {
            g.fill3DRect(obstacle.x*tileSize, obstacle.y*tileSize, tileSize, tileSize, true);
        }

        //Food
        g.setColor(Color.red);
        g.fill3DRect(food1.x*tileSize, food1.y*tileSize, tileSize, tileSize, true);
        g.setColor(Color.orange);
        g.fill3DRect(food2.x*tileSize, food2.y*tileSize, tileSize, tileSize, true);

        //Snake Head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);
        
        //Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }

        //Score and Info
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over, " + playerName + "!", boardWidth/2 - 70, boardHeight/2 - 60);
            
            // Play Summary
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.white);
            g.drawString("Play Summary", boardWidth/2 - 70, boardHeight/2 - 30);
            
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Final Score: " + String.valueOf(snakeBody.size()), boardWidth/2 - 60, boardHeight/2);
            
            long currentTime = System.currentTimeMillis();
            double duration = (currentTime - startTime) / 1000.0;
            g.drawString(String.format("Time Played: %.2f seconds", duration), boardWidth/2 - 80, boardHeight/2 + 25);
            
            // End time
            g.drawString("Game Ended: " + endTime.format(timeFormatter), boardWidth/2 - 80, boardHeight/2 + 45);
            
            // Item Summary
            g.setColor(Color.white);
            g.drawString("Item Summary:", boardWidth/2 - 80, boardHeight/2 + 70);
            
            StringBuilder redItems = new StringBuilder();
            StringBuilder orangeItems = new StringBuilder();
            for (int i = 0; i < food1Score; i++) redItems.append("[]");
            for (int i = 0; i < food2Score; i++) orangeItems.append("[]");
            
            g.setColor(Color.red);
            g.drawString("Red: " + redItems.toString(), boardWidth/2 - 80, boardHeight/2 + 90);
            
            g.setColor(Color.orange);
            g.drawString("Orange: " + orangeItems.toString(), boardWidth/2 - 80, boardHeight/2 + 110);
            
            g.setColor(Color.gray);
            g.drawString("Obstacles: " + obstacles.size(), boardWidth/2 - 80, boardHeight/2 + 130);
            
            g.setColor(Color.yellow);
            g.drawString("Press SPACE to restart", boardWidth/2 - 80, boardHeight/2 + 150);
        } else {
            g.setColor(Color.white);
            g.drawString(playerName + "'s Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
            g.drawString("Mode: " + (ai.isEnabled() ? "AI" : "Manual"), tileSize - 16, tileSize * 2);
            g.drawString("Obstacles: " + obstacles.size(), tileSize - 16, tileSize * 3);

            // Display time and food scores
            long currentTime = System.currentTimeMillis();
            long duration = (currentTime - startTime) / 10;
            g.setColor(Color.white);
            g.drawString("Time: " + String.valueOf(duration), boardWidth - 150, tileSize);
            g.setColor(Color.red);
            g.drawString("Red: " + String.valueOf(food1Score), boardWidth - 150, tileSize + 20);
            g.setColor(Color.orange);
            g.drawString("Orange: " + String.valueOf(food2Score), boardWidth - 150, tileSize + 40);
        }
    }

    public void placeFood() {
        food1.x = random.nextInt(boardWidth/tileSize);
        food1.y = random.nextInt(boardHeight/tileSize);
        food2.x = random.nextInt(boardWidth/tileSize);
        food2.y = random.nextInt(boardHeight/tileSize);
        
        // Make sure food items don't overlap with each other, snake, or obstacles
        while (collision(food1, food2) || isOnSnake(food1) || isOnSnake(food2) || 
               isOnObstacle(food1) || isOnObstacle(food2)) {
            food2.x = random.nextInt(boardWidth/tileSize);
            food2.y = random.nextInt(boardHeight/tileSize);
        }
    }
    
    private boolean isOnSnake(Tile tile) {
        if (collision(tile, snakeHead)) return true;
        for (Tile bodyPart : snakeBody) {
            if (collision(tile, bodyPart)) return true;
        }
        return false;
    }
    
    private boolean isOnObstacle(Tile tile) {
        for (Tile obstacle : obstacles) {
            if (collision(tile, obstacle)) return true;
        }
        return false;
    }
    
    private void trySpawnObstacle() {
        if (obstacles.size() >= MAX_OBSTACLES) return;
        
        if (random.nextInt(100) < OBSTACLE_SPAWN_CHANCE) {
            Tile newObstacle;
            do {
                newObstacle = new Tile(
                    random.nextInt(boardWidth/tileSize),
                    random.nextInt(boardHeight/tileSize)
                );
            } while (isOnSnake(newObstacle) || isOnObstacle(newObstacle) || 
                     collision(newObstacle, food1) || collision(newObstacle, food2));
            
            obstacles.add(newObstacle);
        }
    }

    public void move() {
        //eat food
        if (collision(snakeHead, food1)) {
            snakeBody.add(new Tile(food1.x, food1.y));
            food1Score++;
            trySpawnObstacle();
            placeFood();
        }
        else if (collision(snakeHead, food2)) {
            snakeBody.add(new Tile(food2.x, food2.y));
            food2Score++;
            trySpawnObstacle();
            placeFood();
        }

        //move snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        
        //move snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //check collisions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        
        // Check obstacle collision
        for (Tile obstacle : obstacles) {
            if (collision(snakeHead, obstacle)) {
                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || 
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) {
            gameOver = true;
        }
        
        // Record end time and create game result when game is over
        if (gameOver && endTime == null) {
            endTime = LocalDateTime.now();
            double duration = (System.currentTimeMillis() - startTime) / 1000.0;
            GameResult result = new GameResult(
                playerName,
                snakeBody.size(),
                food1Score,
                food2Score,
                obstacles.size(),
                duration,
                endTime,
                ai.isEnabled()
            );
            // Display the game summary
            JOptionPane.showMessageDialog(null, 
                result.getFormattedSummary(),
                "Game Summary",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (ai.isEnabled()) {
            ai.makeMove();
        }
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }  

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            resetGame();
            return;
        }
        
        // Toggle AI with 'A' key
        if (e.getKeyCode() == KeyEvent.VK_A) {
            ai.toggle();
            return;
        }
        
        // Only process movement keys if AI is not enabled
        if (!ai.isEnabled()) {
            if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
                velocityX = 0;
                velocityY = -1;
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
                velocityX = 0;
                velocityY = 1;
            }
            else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
                velocityX = -1;
                velocityY = 0;
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
                velocityX = 1;
                velocityY = 0;
            }
        }
    }

    private void resetGame() {
        // Ask if player wants to change name
        String newName = JOptionPane.showInputDialog(null, 
            "Current player: " + playerName + "\nEnter new name or cancel to keep current:", 
            "Snake Game", 
            JOptionPane.QUESTION_MESSAGE);
        if (newName != null && !newName.trim().isEmpty()) {
            playerName = newName.trim();
        }

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        obstacles = new ArrayList<Tile>();
        for (int i = 0; i < 4; i++) {
            snakeBody.add(new Tile(4 - i, 5));
        }
        food1 = new Tile(10, 10);
        food2 = new Tile(15, 15);
        placeFood();
        velocityX = 1;
        velocityY = 0;
        gameOver = false;
        startTime = System.currentTimeMillis();
        endTime = null;  // Reset end time
        food1Score = 0;
        food2Score = 0;
        
        // Re-enable AI by default
        if (!ai.isEnabled()) {
            ai.toggle();
        }
        
        gameLoop.start();
    }

    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}

