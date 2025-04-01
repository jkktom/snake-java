# Project Structure Documentation

## Current Structure (After Gradle Integration)

```
snake-java/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               ├── snake/           # Main game package
│               │   ├── App.java     # Application entry point
│               │   ├── SnakeGame.java # Game panel and core logic
│               │   └── SnakeAI.java  # AI implementation
│               └── model/           # Game models
│                   ├── Snake.java   # Snake entity with immutable state
│                   ├── Direction.java # Movement directions
│                   ├── Food.java    # Food entity
│                   ├── Frame.java   # Window dimensions
│                   ├── GameResult.java # Game outcome data
│                   ├── Obstacle.java  # Obstacle entity
│                   ├── Comment.java   # User comments (simplified)
│                   ├── Tile.java     # Board tile types
│                   └── User.java     # User profile
├── gradle/                # Gradle wrapper files
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build/                # Compiled output
│   ├── classes/         # Compiled Java classes
│   └── resources/      # Processed resources
├── docs/               # Documentation
│   └── ProjectStructure.md
├── resources/          # Game resources
│   └── images/        # Game assets
├── .gradle/           # Gradle cache and state
├── gradlew            # Gradle wrapper script (Unix)
├── gradlew.bat        # Gradle wrapper script (Windows)
├── build.gradle       # Gradle build configuration
├── settings.gradle    # Gradle settings
├── ProjectHistory     # Project evolution documentation
├── AI_DOCUMENTATION.md # AI feature documentation
└── README.md         # Project overview
```

### Current Structure Overview
- Source code organization:
  - `src/main/java/com/`: Main source code directory
    - `snake/`: Core game implementation
      - `App.java`: Main entry point and window setup
      - `SnakeGame.java`: Game panel and core logic
      - `SnakeAI.java`: AI pathfinding and control
    - `model/`: Game entities and data structures
      - All models are immutable for thread safety
      - Records used where appropriate
      - Clear separation of concerns
- Build system:
  - Gradle integration complete
  - Proper source directory structure
  - Build artifacts in `build/` directory
- Documentation:
  - Organized in `docs/` directory
  - Project structure documentation
  - AI documentation
  - Project history tracking

### Planned Improvements

1. Game Behavior Organization:
```
com.snake/
├── behavior/           # Game behavior package
│   ├── food/          # Food-related behaviors
│   │   ├── FoodPlacer.java        # Food placement strategy
│   │   └── FoodCollisionHandler.java # Food collision logic
│   ├── obstacle/      # Obstacle-related behaviors
│   │   ├── ObstacleSpawner.java   # Obstacle generation
│   │   └── ObstacleManager.java   # Obstacle lifecycle
│   ├── collision/     # Collision detection
│   │   ├── WallCollision.java     # Wall collision checks
│   │   ├── SelfCollision.java     # Snake self-collision
│   │   └── CollisionDetector.java # General collision logic
│   └── score/         # Score management
│       ├── ScoreTracker.java      # Score calculation
│       └── ScoreFormatter.java    # Score display formatting
├── renderer/          # Visual rendering
│   ├── GameRenderer.java     # Main game rendering
│   ├── BoardRenderer.java    # Game board rendering
│   ├── SnakeRenderer.java    # Snake rendering
│   ├── FoodRenderer.java     # Food rendering
│   └── UIRenderer.java       # UI elements rendering
└── ui/                # User interface
    ├── message/
    │   ├── MessageDisplay.java    # Message rendering
    │   └── WelcomeScreen.java     # Welcome message
    └── overlay/
        ├── GameOverlay.java       # Game stats overlay
        └── ScoreOverlay.java      # Score display
```

### Behavior Package Details

1. `food` Package:
   - `FoodPlacer`: Handles food placement logic
     - Ensures food doesn't spawn on snake or obstacles
     - Manages multiple food items
     - Implements placement strategies
   - `FoodCollisionHandler`: Manages food collection
     - Detects food collection
     - Triggers snake growth
     - Updates score

2. `obstacle` Package:
   - `ObstacleSpawner`: Controls obstacle creation
     - Random spawn timing
     - Position validation
     - Difficulty-based spawning
   - `ObstacleManager`: Manages obstacles
     - Tracks active obstacles
     - Removes old obstacles
     - Validates obstacle positions

3. `collision` Package:
   - `WallCollision`: Border collision detection
   - `SelfCollision`: Snake self-intersection
   - `CollisionDetector`: Central collision logic
     - Combines all collision checks
     - Provides unified collision API

4. `score` Package:
   - `ScoreTracker`: Score management
     - Tracks current score
     - Handles different scoring rules
   - `ScoreFormatter`: Score presentation
     - Formats scores for display
     - Handles high score presentation

5. `renderer` Package:
   - Separates all drawing logic
   - Each component has dedicated renderer
   - Consistent styling and animations
   - Efficient rendering with dirty regions

6. `ui` Package:
   - Organized user interface elements
   - Centralized message handling
   - Consistent overlay management
   - Screen-specific components

### Benefits of New Structure
1. Clear Separation of Concerns:
   - Each behavior in dedicated package
   - Easy to modify individual components
   - Better testability
   
2. Improved Maintainability:
   - Focused class responsibilities
   - Reduced coupling
   - Easy to add new features

3. Better Organization:
   - Logical grouping of related code
   - Clear file naming conventions
   - Structured package hierarchy

Would you like me to proceed with implementing any of these packages?

Note: This structure represents the current state after the Gradle integration and model class implementation. Further improvements will be documented as they are implemented. 