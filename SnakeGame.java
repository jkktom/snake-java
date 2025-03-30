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
    long startTime; // Add start time for duration tracking
    int food1Score = 0; // Track scores for each food type
    int food2Score = 0;

    boolean gameOver = false;
    
    // Add AI controller
    private SnakeAI ai;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        
        // Initialize snake with 4 body tiles (total length of 5)
        for (int i = 0; i < 4; i++) {
            snakeBody.add(new Tile(4 - i, 5)); // Place body tiles to the left of the head
        }

        food1 = new Tile(10, 10);
        food2 = new Tile(15, 15);
        random = new Random();
        placeFood();

        velocityX = 1;
        velocityY = 0;
        
        startTime = System.currentTimeMillis(); // Initialize start time
        //game timer
        gameLoop = new Timer(100, this); //how long it takes to start timer, milliseconds gone between frames 
        gameLoop.start();
        
        // Initialize AI
        ai = new SnakeAI(this);
	}	
    
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
        //Grid Lines
        for(int i = 0; i < boardWidth/tileSize; i++) {
            //(x1, y1, x2, y2)
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
        // g.fillRect(snakeHead.x, snakeHead.y, tileSize, tileSize);
        // g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);
        
        //Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
		}

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over!", boardWidth/2 - 50, boardHeight/2 - 60);
            
            // Play Summary
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.white);
            g.drawString("Play Summary", boardWidth/2 - 70, boardHeight/2 - 30);
            
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Final Score: " + String.valueOf(snakeBody.size()), boardWidth/2 - 60, boardHeight/2);
            
            long currentTime = System.currentTimeMillis();
            double duration = (currentTime - startTime) / 1000.0; // Convert to seconds with decimal
            g.drawString(String.format("Time Played: %.2f seconds", duration), boardWidth/2 - 80, boardHeight/2 + 25);
            
            // Item Summary
            g.setColor(Color.white);
            g.drawString("Item Summary:", boardWidth/2 - 80, boardHeight/2 + 50);
            
            // Create visual representation of collected items
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
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);

            // Display time and food scores
            long currentTime = System.currentTimeMillis();
            long duration = (currentTime - startTime) / 10; // Convert to split seconds (1/100th of a second)
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
        gameLoop.start();
    }

    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
