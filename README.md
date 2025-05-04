# TaskMate - Modern Task Management Application

TaskMate is a sleek, modern task management application built with JavaFX. It provides an intuitive interface for managing your daily tasks with features like priority levels, due dates, and task status tracking.

## Features

- **Modern UI**: Clean, card-based interface with a pleasing color scheme
- **Task Management**:
  - Create tasks with title, description, due date, and priority
  - Mark tasks as complete or pending
  - Delete tasks with confirmation
- **Task Organization**:
  - Filter tasks by priority (HIGH, MEDIUM, LOW)
  - Filter tasks by status (PENDING, COMPLETED)
  - View all tasks at once
- **Visual Feedback**:
  - Color-coded task cards (green for completed, orange for pending)
  - Priority-based visual indicators
  - Due date display

## Technical Details

- Built with JavaFX for modern UI
- Uses Java's LocalDate for date handling
- Implements ObservableList for real-time UI updates
- Follows modern UI/UX design principles

## Getting Started

### Prerequisites

- Java 8 or higher
- JavaFX SDK

### Running the Application

1. Clone the repository
2. Open the project in your preferred Java IDE
3. Run the `Main` class in `src/main/java/com/taskmate/Main.java`

## Project Structure

```
src/main/java/com/taskmate/
├── Main.java           # Main application class with UI implementation
├── Task.java           # Task model class
├── TaskManager.java    # Task management logic
├── FileHandler.java    # File I/O operations
└── LocalDateAdapter.java # Date serialization handling
```

## UI Components

- **Header**: Application title with modern styling
- **Add Task Section**: Form for creating new tasks
- **Task List Section**: Display of all tasks with filtering options
- **Task Actions**: Buttons for task management (Complete, Reopen, Delete)

## Color Scheme

- Primary Color: #2196F3 (Blue)
- Secondary Color: #4CAF50 (Green)
- Accent Color: #FF9800 (Orange)
- Danger Color: #F44336 (Red)
- Background: #F5F7FA (Light Gray)
- Card Background: #FFFFFF (White)

## Contributing

Feel free to submit issues and enhancement requests!

## License

This project is open source and available under the MIT License. 