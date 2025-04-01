package com.snake;

import com.model.*;
import com.model.Frame;

import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int TILE_SIZE = 25;
    private static final int MAX_OBSTACLES = 50;
    private static final int OBSTACLE_SPAWN_CHANCE = 101;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final Frame frame;
    private Snake snake;
    private Food food1;
    private Food food2;
    private final List<Obstacle> obstacles;
    private final Random random;
    
    private Timer gameLoop;
    private long startTime;
    private LocalDateTime endTime;
    private int food1Score = 0;
    private int food2Score = 0;
    private boolean gameOver = false;
    
    private SnakeAI snakeAI;
    private final User user;
    
    public SnakeGame(Frame frame, User user) {
        this.frame = frame;
        this.user = user;
        
        setPreferredSize(new Dimension(frame.width(), frame.height()));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        
        // Initialize game objects
        Point startPos = new Point(5, 5);
        snake = new Snake();
        obstacles = new ArrayList<>();
        random = new Random();
        
        // Place initial food
        food1 = new Food(new Point(10, 10));
        food2 = new Food(new Point(15, 15));
        placeFood();
        
        startTime = System.currentTimeMillis();
        gameLoop = new Timer(100, this);
        gameLoop.start();
        
        // Initialize AI
        snakeAI = new SnakeAI(this);
        snakeAI.enableAI();
        
        // Show welcome message
        showWelcomeMessage();
    }
    
    private void showWelcomeMessage() {
        JOptionPane.showMessageDialog(null, 
            "Welcome " + user.username() + "!\n" +
            "AI is enabled by default.\n" +
            "Press 'A' to disable AI and play manually.\n" +
            "Use arrow keys when playing manually.\n" +
            "Watch out for obstacles that appear when collecting food!",
            "Game Controls",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    private void draw(Graphics g) {
        drawGrid(g);
        drawObstacles(g);
        drawFood(g);
        drawSnake(g);
        drawScore(g);
    }
    
    private void drawGrid(Graphics g) {
        for(int i = 0; i < frame.width()/TILE_SIZE; i++) {
            g.drawLine(i*TILE_SIZE, 0, i*TILE_SIZE, frame.height());
            g.drawLine(0, i*TILE_SIZE, frame.width(), i*TILE_SIZE); 
        }
    }
    
    private void drawObstacles(Graphics g) {
        g.setColor(Color.gray);
        for (Obstacle obstacle : obstacles) {
            Point pos = obstacle.getPosition();
            g.fill3DRect(pos.x*TILE_SIZE, pos.y*TILE_SIZE, TILE_SIZE, TILE_SIZE, true);
        }
    }
    
    private void drawFood(Graphics g) {
        g.setColor(Color.red);
        Point food1Pos = food1.getPosition();
        g.fill3DRect(food1Pos.x*TILE_SIZE, food1Pos.y*TILE_SIZE, TILE_SIZE, TILE_SIZE, true);
        
        g.setColor(Color.orange);
        Point food2Pos = food2.getPosition();
        g.fill3DRect(food2Pos.x*TILE_SIZE, food2Pos.y*TILE_SIZE, TILE_SIZE, TILE_SIZE, true);
    }
    
    private void drawSnake(Graphics g) {
        g.setColor(Color.green);
        Point head = snake.getHead();
        g.fill3DRect(head.x*TILE_SIZE, head.y*TILE_SIZE, TILE_SIZE, TILE_SIZE, true);
        
        for (Point bodyPart : snake.getBody()) {
            g.fill3DRect(bodyPart.x*TILE_SIZE, bodyPart.y*TILE_SIZE, TILE_SIZE, TILE_SIZE, true);
        }
    }
    
    private void drawScore(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            drawGameOverScreen(g);
        } else {
            drawGameplayScore(g);
        }
    }
    
    private void drawGameOverScreen(Graphics g) {
        g.setColor(Color.red);
        g.drawString("Game Over, " + user.username() + "!", frame.width()/2 - 70, frame.height()/2 - 60);
        
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.white);
        g.drawString("Play Summary", frame.width()/2 - 70, frame.height()/2 - 30);
        
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Final Score: " + snake.getBody().size(), frame.width()/2 - 60, frame.height()/2);
        
        Duration duration = Duration.ofMillis(System.currentTimeMillis() - startTime);
        g.drawString(String.format("Time Played: %d:%02d", 
            duration.toMinutes(), duration.minusMinutes(duration.toMinutes()).getSeconds()),
            frame.width()/2 - 80, frame.height()/2 + 25);
        
        g.drawString("Game Ended: " + endTime.format(TIME_FORMATTER), frame.width()/2 - 80, frame.height()/2 + 45);
        
        drawItemSummary(g);
    }
    
    private void drawItemSummary(Graphics g) {
        g.setColor(Color.white);
        g.drawString("Item Summary:", frame.width()/2 - 80, frame.height()/2 + 70);
        
        g.setColor(Color.red);
        g.drawString("Red: " + "[]".repeat(food1Score), frame.width()/2 - 80, frame.height()/2 + 90);
        
        g.setColor(Color.orange);
        g.drawString("Orange: " + "[]".repeat(food2Score), frame.width()/2 - 80, frame.height()/2 + 110);
        
        g.setColor(Color.gray);
        g.drawString("Obstacles: " + obstacles.size(), frame.width()/2 - 80, frame.height()/2 + 130);
        
        g.setColor(Color.yellow);
        g.drawString("Press SPACE to restart", frame.width()/2 - 80, frame.height()/2 + 150);
    }
    
    private void drawGameplayScore(Graphics g) {
        g.setColor(Color.white);
        g.drawString(user.username() + "'s Score: " + snake.getBody().size(), TILE_SIZE - 16, TILE_SIZE);
        g.drawString("Mode: " + (snakeAI.isEnabled() ? "AI" : "Manual"), TILE_SIZE - 16, TILE_SIZE * 2);
        g.drawString("Obstacles: " + obstacles.size(), TILE_SIZE - 16, TILE_SIZE * 3);
        
        Duration duration = Duration.ofMillis(System.currentTimeMillis() - startTime);
        g.drawString("Time: " + duration.toSeconds(), frame.width() - 150, TILE_SIZE);
        
        g.setColor(Color.red);
        g.drawString("Red: " + food1Score, frame.width() - 150, TILE_SIZE + 20);
        g.setColor(Color.orange);
        g.drawString("Orange: " + food2Score, frame.width() - 150, TILE_SIZE + 40);
    }
    
    private void placeFood() {
        Point newPos1, newPos2;
        do {
            newPos1 = new Point(random.nextInt(frame.width()/TILE_SIZE), 
                              random.nextInt(frame.height()/TILE_SIZE));
            newPos2 = new Point(random.nextInt(frame.width()/TILE_SIZE), 
                              random.nextInt(frame.height()/TILE_SIZE));
        } while (isPositionOccupied(newPos1) || isPositionOccupied(newPos2));
        
        food1 = new Food(newPos1);
        food2 = new Food(newPos2);
    }
    
    private boolean isPositionOccupied(Point pos) {
        return snake.getBody().contains(pos) || 
               obstacles.stream().anyMatch(o -> o.getPosition().equals(pos)) ||
               food1.getPosition().equals(pos) ||
               food2.getPosition().equals(pos);
    }
    
    private void trySpawnObstacle() {
        if (obstacles.size() >= MAX_OBSTACLES) return;
        
        if (random.nextInt(100) < OBSTACLE_SPAWN_CHANCE) {
            Point obstaclePos;
            do {
                obstaclePos = new Point(random.nextInt(frame.width()/TILE_SIZE),
                                      random.nextInt(frame.height()/TILE_SIZE));
            } while (isPositionOccupied(obstaclePos));
            
            obstacles.add(new Obstacle(obstaclePos));
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (snakeAI.isEnabled()) {
                snakeAI.makeMove();
            }
            move();
        }
        repaint();
    }
    
    private void move() {
        if (gameOver) return;

        // Get current direction and calculate new head position
        Direction direction = snake.getDirection();
        Point head = snake.getHead();
        Point newHead = new Point(
            head.x + direction.getDx(),
            head.y + direction.getDy()
        );

        // Check wall collision
        if (!frame.contains(newHead.x, newHead.y)) {
            gameOver = true;
            endTime = LocalDateTime.now();
            return;
        }

        // Check obstacle collision
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPosition().equals(newHead)) {
                gameOver = true;
                endTime = LocalDateTime.now();
                return;
            }
        }

        // Check self collision
        for (Point bodyPart : snake.getBody()) {
            if (bodyPart.equals(newHead)) {
                gameOver = true;
                endTime = LocalDateTime.now();
                return;
            }
        }

        // Check food collision
        if (newHead.equals(food1.getPosition())) {
            food1Score++;
            snake = snake.grow(newHead);  // Create new snake with added segment
            trySpawnObstacle();
            placeFood();
        } else if (newHead.equals(food2.getPosition())) {
            food2Score++;
            snake = snake.grow(newHead);  // Create new snake with added segment
            trySpawnObstacle();
            placeFood();
        } else {
            snake = snake.move(newHead);  // Create new snake at new position
        }
    }
    
    private void resetGame() {
        // Reset snake
        snake = new Snake();
        
        // Reset food
        food1 = new Food(new Point(10, 10));
        food2 = new Food(new Point(15, 15));
        placeFood();
        
        // Clear obstacles
        obstacles.clear();
        
        // Reset scores
        food1Score = 0;
        food2Score = 0;
        
        // Reset game state
        gameOver = false;
        startTime = System.currentTimeMillis();
        
        // Request focus for keyboard input
        requestFocus();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                resetGame();
            }
            return;
        }

        Direction newDirection = switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> Direction.UP;
            case KeyEvent.VK_RIGHT -> Direction.RIGHT;
            case KeyEvent.VK_DOWN -> Direction.DOWN;
            case KeyEvent.VK_LEFT -> Direction.LEFT;
            default -> null;
        };

        if (newDirection != null) {
            snake = snake.setDirection(newDirection);
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            snake = snake.setAI(!snake.isAI());
            if (snake.isAI()) {
                snakeAI.enableAI();
            } else {
                snakeAI.disableAI();
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    // Getters for AI
    public Frame getFrame() { return frame; }
    public Snake getSnake() { return snake; }
    public Food getFood1() { return food1; }
    public Food getFood2() { return food2; }
    public List<Obstacle> getObstacles() { return List.copyOf(obstacles); }

    public void setSnakeDirection(Direction direction) {
        snake = snake.setDirection(direction);
    }
} 