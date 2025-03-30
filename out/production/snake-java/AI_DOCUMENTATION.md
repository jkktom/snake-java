# Snake Game AI Documentation

## Overview
This document describes the implementation of an AI player for the Snake game. The AI automatically controls the snake to collect food items while avoiding collisions with walls and itself.

## How to Use

### Controls
- Press `A` key to toggle AI mode on/off
- Use arrow keys for manual control when AI is disabled
- Press `SPACE` to restart when game is over

### AI Features
1. **Automatic Food Collection**
   - Targets the nearest food item (red or orange)
   - Calculates Manhattan distance to both food items
   - Dynamically switches targets based on proximity

2. **Collision Avoidance**
   - Prevents wall collisions
   - Avoids self-collision with snake body
   - Has fallback movement options when direct path is blocked

## Technical Implementation

### Classes

#### 1. SnakeAI Class
```java
public class SnakeAI {
    private SnakeGame game;
    private boolean isEnabled = false;
    // ... methods for AI control
}
```

Key methods:
- `toggle()`: Switches AI on/off
- `makeMove()`: Calculates and executes next move
- `canMove()`: Checks if a move is safe

#### 2. SnakeGame Integration
- Added AI controller instance
- Modified game loop to support AI moves
- Added keyboard control for AI toggling

### AI Algorithm

1. **Target Selection**
   ```java
   int food1Distance = Math.abs(headX - game.food1.x) + Math.abs(headY - game.food1.y);
   int food2Distance = Math.abs(headX - game.food2.x) + Math.abs(headY - game.food2.y);
   SnakeGame.Tile targetFood = (food1Distance <= food2Distance) ? game.food1 : game.food2;
   ```

2. **Movement Priority**
   1. Horizontal movement toward food
   2. Vertical movement toward food
   3. Any safe direction if direct path is blocked

3. **Safety Checks**
   - Wall boundary checking
   - Snake body collision detection
   - Movement validation before execution

## Performance Characteristics

### Strengths
- Quick decision making
- Efficient pathfinding to nearest food
- Reliable collision avoidance

### Limitations
- May get trapped in corners
- Doesn't plan multiple moves ahead
- Can make suboptimal choices in tight spaces

## Future Improvements
1. Implement A* pathfinding for better route planning
2. Add look-ahead to prevent trapping itself
3. Consider snake length in path planning
4. Add difficulty levels for AI behavior
5. Implement machine learning for adaptive behavior 