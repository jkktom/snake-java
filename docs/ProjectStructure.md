# Project Structure Documentation

## Current Structure (After Basic Command Implementation)

```
snake-java/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       ├── config/         # Configuration
│       │       │   └── JDBCConnection.java
│       │       ├── dao/           # Data Access Objects
│       │       │   ├── UserDao.java
│       │       │   ├── GameResultDao.java
│       │       │   └── CommentDao.java
│       │       ├── service/       # Service Layer
│       │       │   ├── UserService.java
│       │       │   ├── GameResultService.java
│       │       │   └── CommentService.java
│       │       ├── view/          # View Layer
│       │       │   ├── UserView.java
│       │       │   ├── GameResultView.java
│       │       │   └── CommentView.java
│       │       ├── snake/         # Game Components
│       │       │   └── [existing game structure]
│       │       ├── model/         # Data Models
│       │       │   └── [existing models]
│       │       └── Application.java # Main Entry Point
│       └── resources/
│           ├── config.properties  # Database Config
│           └── queries.xml        # SQL Queries
├── docs/
│   ├── ProjectHistory
│   └── ProjectStructure.md
└── [existing structure]
```

### Current Implementation Status

1. Database Layer (In Progress):
   - Connection pooling with HikariCP
   - Basic DAO structure
   - Query management with XML
   - Configuration system

2. Service Layer (Implemented):
   - User management
   - Game result tracking
   - Comment system
   - Error handling

3. View Layer (Implemented):
   - Korean interface
   - Command-line menus
   - User interaction
   - Data display

4. Configuration (In Progress):
   - Database properties
   - Connection pooling settings
   - Query management
   - Resource loading

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