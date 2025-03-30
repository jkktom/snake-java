public class SnakeAI {
    private SnakeGame game;
    private boolean isEnabled = false;

    public SnakeAI(SnakeGame game) {
        this.game = game;
    }

    public void toggle() {
        isEnabled = !isEnabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void makeMove() {
        if (!isEnabled) return;
        
        // Get current position and target food
        int headX = game.snakeHead.x;
        int headY = game.snakeHead.y;
        
        // Choose the closest food that can be reached safely
        SnakeGame.Tile targetFood = findBestFood();
        
        // Calculate direction to food while avoiding obstacles
        int newVelocityX = 0;
        int newVelocityY = 0;

        // Try to move horizontally first if needed
        if (headX < targetFood.x && canMove(1, 0)) {
            newVelocityX = 1;
            newVelocityY = 0;
        } else if (headX > targetFood.x && canMove(-1, 0)) {
            newVelocityX = -1;
            newVelocityY = 0;
        }
        // If can't move horizontally, try vertical
        else if (headY < targetFood.y && canMove(0, 1)) {
            newVelocityX = 0;
            newVelocityY = 1;
        } else if (headY > targetFood.y && canMove(0, -1)) {
            newVelocityX = 0;
            newVelocityY = -1;
        }
        // If stuck, try any valid move
        else {
            int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            for (int[] dir : directions) {
                if (canMove(dir[0], dir[1])) {
                    newVelocityX = dir[0];
                    newVelocityY = dir[1];
                    break;
                }
            }
        }

        // Update velocities if a valid move was found
        if (newVelocityX != 0 || newVelocityY != 0) {
            game.velocityX = newVelocityX;
            game.velocityY = newVelocityY;
        }
    }

    private SnakeGame.Tile findBestFood() {
        int headX = game.snakeHead.x;
        int headY = game.snakeHead.y;
        
        // Calculate distances to both foods
        int food1Distance = Math.abs(headX - game.food1.x) + Math.abs(headY - game.food1.y);
        int food2Distance = Math.abs(headX - game.food2.x) + Math.abs(headY - game.food2.y);
        
        // Check if path to each food is blocked by obstacles
        boolean food1Blocked = isPathBlocked(game.food1);
        boolean food2Blocked = isPathBlocked(game.food2);
        
        // Choose the closest unblocked food
        if (!food1Blocked && !food2Blocked) {
            return (food1Distance <= food2Distance) ? game.food1 : game.food2;
        } else if (!food1Blocked) {
            return game.food1;
        } else if (!food2Blocked) {
            return game.food2;
        }
        
        // If both paths are blocked, choose the closest one anyway
        return (food1Distance <= food2Distance) ? game.food1 : game.food2;
    }
    
    private boolean isPathBlocked(SnakeGame.Tile target) {
        int headX = game.snakeHead.x;
        int headY = game.snakeHead.y;
        
        // Check for obstacles in the rectangular area between snake and target
        int minX = Math.min(headX, target.x);
        int maxX = Math.max(headX, target.x);
        int minY = Math.min(headY, target.y);
        int maxY = Math.max(headY, target.y);
        
        for (SnakeGame.Tile obstacle : game.obstacles) {
            if (obstacle.x >= minX && obstacle.x <= maxX && 
                obstacle.y >= minY && obstacle.y <= maxY) {
                return true;
            }
        }
        return false;
    }

    private boolean canMove(int velocityX, int velocityY) {
        // Check if moving in this direction would cause collision
        int newX = game.snakeHead.x + velocityX;
        int newY = game.snakeHead.y + velocityY;
        
        // Check wall collision
        if (newX < 0 || newX >= game.boardWidth/game.tileSize || 
            newY < 0 || newY >= game.boardHeight/game.tileSize) {
            return false;
        }
        
        // Check body collision
        for (SnakeGame.Tile bodyPart : game.snakeBody) {
            if (newX == bodyPart.x && newY == bodyPart.y) {
                return false;
            }
        }
        
        // Check obstacle collision
        for (SnakeGame.Tile obstacle : game.obstacles) {
            if (newX == obstacle.x && newY == obstacle.y) {
                return false;
            }
        }
        
        return true;
    }
} 