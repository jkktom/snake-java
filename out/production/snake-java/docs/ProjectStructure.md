# Project Structure Documentation

## Original Structure (Pre-reorganization)

```
snake-java/
├── App.java                 # Main application entry point
├── SnakeGame.java          # Core game logic
├── SnakeAI.java           # AI implementation
├── GameResult.java        # Game result data structure
├── AI_DOCUMENTATION.md    # AI feature documentation
├── ProjectHistory         # Project evolution documentation
├── README.md             # Project overview
├── snake-java.iml        # IntelliJ IDEA module file
├── image.png             # Project image
└── out/                  # Build output directory
    └── production/
        └── snake-java/   # Compiled classes and resources
            ├── App.class
            ├── SnakeGame.class
            ├── SnakeGame$Tile.class
            ├── SnakeAI.class
            ├── GameResult.class
            ├── AI_DOCUMENTATION.md
            ├── README.md
            └── snake-java.iml
```

### Current Structure Overview
- All Java source files are in the root directory
- Documentation files are mixed with source code
- Build output is in the `out` directory
- No separate directories for source code, resources, or documentation
- No dedicated test directory
- No clear separation of concerns in the directory structure

This document will be updated after the project reorganization to reflect the new structure. 

## Proposed New Structure

```
snake-java/
├── src/
│   ├── DB_source/        # Database source files
│   │   ├── init.sql      # Database initialization script
│   │   └── schema.sql    # Database schema definitions
│   └── main/
│       └── java/
│           └── com/
│               ├── snake/           # Main application package
│               │   └── App.java     # Application entry point
│               ├── config/          # Game configurations
│               │   └── GameConfig   # Snake game settings, board size, speed settings
│               ├── dao/             # Data persistence
│               │   ├── ScoreDao     # High score and game results persistence
│               │   ├── UserDao      # User data persistence
│               │   └── CommentDao   # Comment data persistence
│               ├── model/           # Game models
│               │   ├── Snake        # Snake entity and movement logic
│               │   ├── GameResult   # Game result and scoring
│               │   ├── Food         # Food entity
│               │   ├── Tile         # Game board tile representation
│               │   ├── User         # User profile and authentication
│               │   └── Comment      # User comments and feedback
│               ├── service/         # Game services
│               │   ├── GameService  # Main game logic service
│               │   ├── AIService    # Snake AI implementation
│               │   ├── UserService  # User management service
│               │   └── CommentService # Comment management service
│               ├── util/            # Utilities
│               │   ├── Direction    # Movement direction enum
│               │   └── GameUtils    # Common game utilities
│               └── view/            # UI components
│                   ├── GameBoard    # Game board rendering
│                   ├── GameUI       # Main game interface
│                   ├── UserView     # User profile view
│                   └── CommentView  # Comment display and input
├── docs/                  # Documentation
│   ├── ProjectStructure.md
│   ├── AI_DOCUMENTATION.md
│   └── ProjectHistory
├── resources/             # Game resources
│   └── images/           # Game images and assets
└── build/                # Compiled output
```

### New Structure Overview
- Source code organization:
  - `DB_source/`: Database related source files
    - SQL scripts for database initialization
    - Database schema definitions
  - `main/java/com/`: Main application source code
    - `snake/`: Main application package
      - `App.java`: Application entry point and game initialization
    - `config/`: Game configuration including board size, speed, and difficulty settings
    - `dao/`: Score, user, and comment data persistence
    - `model/`: Core game entities (Snake, Food, GameResult) and user-related models
      - Added `User` for player profiles and authentication
      - Added `Comment` for game feedback and user interaction
    - `service/`: Game logic and business services
      - Added `UserService` for user management
      - Added `CommentService` for comment handling
    - `util/`: Common utilities and helper classes
    - `view/`: Game UI components and rendering logic
      - Added `UserView` for user profile interface
      - Added `CommentView` for comment display and input
- Documentation in `docs/`:
  - Project structure documentation
  - AI implementation details
  - Project history and changes
- Resources directory for game assets
- Standardized build output directory

### File Migration Plan
- Current `App.java` → `snake/App.java` (Main entry point)
- Current `SnakeGame.java` will be split into:
  - `model/Snake.java`
  - `model/Tile.java`
  - `service/GameService.java`
  - `view/GameBoard.java`
- Current `SnakeAI.java` → `service/AIService.java`
- Current `GameResult.java` → `model/GameResult.java`
- New files to be created:
  - `model/User.java`
  - `model/Comment.java`
  - `service/UserService.java`
  - `service/CommentService.java`
  - `dao/UserDao.java`
  - `dao/CommentDao.java`
  - `view/UserView.java`
  - `view/CommentView.java`
- Documentation files moved to `docs/`
- Image assets moved to `resources/images/`

This structure better organizes the Snake game components while following Java best practices and making the codebase more maintainable.

Note: This is the proposed structure pending implementation and confirmation. 