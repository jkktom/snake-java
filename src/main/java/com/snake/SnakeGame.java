package com.snake;

import com.model.*;
import com.model.Frame;
import com.snake.behavior.collision.CollisionDetector;
import com.snake.behavior.collision.CollisionDetector.CollisionResult;
import com.snake.behavior.food.FoodPlacer;
import com.snake.behavior.food.FoodCollisionHandler;
import com.snake.behavior.obstacle.ObstacleManager;
import com.snake.behavior.obstacle.ObstacleSpawner;
import com.snake.behavior.score.ScoreTracker;
import com.snake.renderer.GameRenderer;
import com.snake.ui.message.MessageDisplay;
import com.snake.ui.overlay.GameOverlay;

import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int DELAY = 100;  // 10 FPS for game logic
    private static final int RENDER_DELAY = 16;  // ~60 FPS for rendering
    
    private final Frame frame;
    private Snake snake;
    private Food food1;
    private Food food2;
    private final User user;
    
    // Game components
    private final FoodPlacer foodPlacer;
    private final FoodCollisionHandler foodHandler;
    private final ObstacleManager obstacleManager;
    private final CollisionDetector collisionDetector;
    private final ScoreTracker scoreTracker;
    private final GameRenderer gameRenderer;
    private final GameOverlay gameOverlay;
    private SnakeAI snakeAI;
    
    private Timer gameLoop;
    private Timer renderLoop;
    private boolean gameOver = false;
    private LocalDateTime endTime;
    private volatile boolean needsRepaint = true;

    public SnakeGame(Frame frame, User user) {
        this.frame = frame;
        this.user = user;
        
        // Initialize components
        this.foodPlacer = new FoodPlacer(frame);
        ObstacleSpawner obstacleSpawner = new ObstacleSpawner(frame);
        this.obstacleManager = new ObstacleManager(obstacleSpawner);
        this.collisionDetector = new CollisionDetector(obstacleManager);
        this.foodHandler = new FoodCollisionHandler(foodPlacer);
        this.scoreTracker = new ScoreTracker();
        this.gameRenderer = new GameRenderer();
        this.gameOverlay = new GameOverlay();
        
        // Set up panel
        setPreferredSize(new Dimension(frame.width(), frame.height()));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        
        // Enable double buffering
        setDoubleBuffered(true);
        
        // Initialize game state
        resetGame();
        
        // Start game loop (10 FPS)
        gameLoop = new Timer(DELAY, e -> {
            if (!gameOver) {
                if (snakeAI.isEnabled()) {
                    snakeAI.makeMove();
                }
                move();
                needsRepaint = true;
            }
        });
        gameLoop.start();
        
        // Start render loop (60 FPS)
        renderLoop = new Timer(RENDER_DELAY, e -> {
            if (needsRepaint) {
                repaint();
                needsRepaint = false;
            }
        });
        renderLoop.start();
        
        // Show welcome message
        MessageDisplay.showWelcomeMessage();
    }
    
    private void resetGame() {
        snake = new Snake();
        food1 = foodPlacer.placeFood(snake, obstacleManager.getObstacles(), null);
        food2 = foodPlacer.placeFood(snake, obstacleManager.getObstacles(), food1);
        obstacleManager.clear();
        scoreTracker.reset();
        gameOver = false;
        endTime = null;
        needsRepaint = true;
        
        snakeAI = new SnakeAI(this);
        snakeAI.enableAI();
        
        requestFocus();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        // Create buffer
        Image offscreen = createImage(getWidth(), getHeight());
        if (offscreen == null) {
            super.paintComponent(g);
            return;
        }
        
        Graphics offG = offscreen.getGraphics();
        
        // Clear background
        offG.setColor(getBackground());
        offG.fillRect(0, 0, getWidth(), getHeight());
        
        // Render game state
        if (gameOver) {
            GameResult result = new GameResult(
                1, // playerId
                scoreTracker.getFood1Score(),
                scoreTracker.getFood2Score(),
                obstacleManager.getObstacles().size(),
                Duration.ofMillis(scoreTracker.getGameDuration()),
                endTime,
                snakeAI.isEnabled()
            );
            gameOverlay.render(offG, result);
        } else {
            gameRenderer.render(offG, snake, food1, food2, 
                              obstacleManager.getObstacles(),
                              user.username(), 
                              snakeAI.isEnabled(),
                              gameOver);
        }
        
        // Draw buffer to screen
        g.drawImage(offscreen, 0, 0, this);
        offG.dispose();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // No longer used for game loop
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

        // Check collisions
        CollisionResult collision = collisionDetector.checkCollision(newHead, snake);
        if (collision.hasCollision()) {
            gameOver = true;
            endTime = LocalDateTime.now();
            needsRepaint = true;
            return;
        }

        // Check food collision
        if (foodHandler.checkFoodCollision(newHead, food1)) {
            snake = foodHandler.handleFoodCollision(snake, newHead);
            scoreTracker.incrementFood1Score();
            food1 = foodPlacer.placeFood(snake, obstacleManager.getObstacles(), food2);
            obstacleManager.trySpawnObstacle(snake, food1, food2);
        } else if (foodHandler.checkFoodCollision(newHead, food2)) {
            snake = foodHandler.handleFoodCollision(snake, newHead);
            scoreTracker.incrementFood2Score();
            food2 = foodPlacer.placeFood(snake, obstacleManager.getObstacles(), food1);
            obstacleManager.trySpawnObstacle(snake, food1, food2);
        } else {
            snake = snake.move(newHead);
        }
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
            needsRepaint = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            snake = snake.setAI(!snake.isAI());
            if (snake.isAI()) {
                snakeAI.enableAI();
            } else {
                snakeAI.disableAI();
            }
            needsRepaint = true;
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
    public List<Obstacle> getObstacles() { return obstacleManager.getObstacles(); }
    
    public void setSnakeDirection(Direction direction) {
        snake = snake.setDirection(direction);
        needsRepaint = true;
    }
} 