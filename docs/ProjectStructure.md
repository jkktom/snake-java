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
1. Game Logic Separation:
   - Extract food placement logic to dedicated manager
   - Create obstacle spawning manager
   - Separate message handling
   - Move drawing logic to renderer package

2. Package Structure Enhancement:
   ```
   com.snake/
   ├── game/          # Game mechanics
   │   ├── FoodManager
   │   ├── ObstacleManager
   │   └── MessageManager
   └── renderer/      # Drawing logic
       ├── GameRenderer
       └── UIRenderer
   ```

3. Future Considerations:
   - Database integration (DAO layer)
   - Service layer for business logic
   - Enhanced UI components
   - User management system

Note: This structure represents the current state after the Gradle integration and model class implementation. Further improvements will be documented as they are implemented. 