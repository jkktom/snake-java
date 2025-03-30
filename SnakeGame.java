import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    public static class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
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

    //game logic
    int velocityX;
    int velocityY;
    Timer gameLoop;
    long startTime;
    int food1Score = 0;
    int food2Score = 0;

    boolean gameOver = false;
    
    // Add AI controller and player name
    private SnakeAI ai;
    private String playerName;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        
        // Get player name
        playerName = JOptionPane.showInputDialog(null, "Enter your name:", "Snake Game", JOptionPane.QUESTION_MESSAGE);
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
        }
        
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        
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
        ai.toggle(); // Enable AI by default
        
        // Show initial message about controls
        JOptionPane.showMessageDialog(null, 
            "Welcome " + playerName + "!\n" +
            "AI is enabled by default.\n" +
            "Press 'A' to disable AI and play manually.\n" +
            "Use arrow keys when playing manually.",
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
            
            // Item Summary
            g.setColor(Color.white);
            g.drawString("Item Summary:", boardWidth/2 - 80, boardHeight/2 + 50);
            
            StringBuilder redItems = new StringBuilder();
            StringBuilder orangeItems = new StringBuilder();
            for (int i = 0; i < food1Score; i++) redItems.append("[]");
            for (int i = 0; i < food2Score; i++) orangeItems.append("[]");
            
            g.setColor(Color.red);
            g.drawString("Red: " + redItems.toString(), boardWidth/2 - 80, boardHeight/2 + 70);
            
            g.setColor(Color.orange);
            g.drawString("Orange: " + orangeItems.toString(), boardWidth/2 - 80, boardHeight/2 + 90);
            
            g.setColor(Color.yellow);
            g.drawString("Press SPACE to restart", boardWidth/2 - 80, boardHeight/2 + 120);
        } else {
            g.setColor(Color.white);
            g.drawString(playerName + "'s Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
            g.drawString("Mode: " + (ai.isEnabled() ? "AI" : "Manual"), tileSize - 16, tileSize * 2);

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

    public void placeFood(){
        food1.x = random.nextInt(boardWidth/tileSize);
        food1.y = random.nextInt(boardHeight/tileSize);
        food2.x = random.nextInt(boardWidth/tileSize);
        food2.y = random.nextInt(boardHeight/tileSize);
        
        // Make sure food items don't overlap
        while (collision(food1, food2)) {
            food2.x = random.nextInt(boardWidth/tileSize);
            food2.y = random.nextInt(boardHeight/tileSize);
        }
    }

    public void move() {
        //eat food
        if (collision(snakeHead, food1)) {
            snakeBody.add(new Tile(food1.x, food1.y));
            food1Score++; // Increment red food score
            placeFood();
        }
        else if (collision(snakeHead, food2)) {
            snakeBody.add(new Tile(food2.x, food2.y));
            food2Score++; // Increment orange food score
            placeFood();
        }

        //move snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) { //right before the head
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

        //game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            //collide with snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || //passed left border or right border
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) { //passed top border or bottom border
            gameOver = true;
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
