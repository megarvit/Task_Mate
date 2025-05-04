package com.taskmate;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Main extends Application {
    // Modern color palette
    private static final String PRIMARY_COLOR = "#2196F3";      // Blue
    private static final String SECONDARY_COLOR = "#4CAF50";    // Green
    private static final String ACCENT_COLOR = "#FF9800";       // Orange
    private static final String DANGER_COLOR = "#F44336";       // Red
    private static final String TEXT_COLOR = "#333333";         // Dark Gray
    private static final String SUBTEXT_COLOR = "#666666";      // Medium Gray
    private static final String BACKGROUND_COLOR = "#F5F7FA";   // Light Gray
    private static final String CARD_BACKGROUND = "#FFFFFF";    // White
    private static final String COMPLETED_TASK_COLOR = "#E8F5E9"; // Light Green
    private static final String PENDING_TASK_COLOR = "#FFF3E0";   // Light Orange

    private TaskManager taskManager;
    private ListView<Task> taskListView;
    private ObservableList<Task> observableTasks;

    @Override
    public void start(Stage primaryStage) {
        taskManager = new TaskManager();
        observableTasks = FXCollections.observableArrayList(taskManager.getAllTasks());

        // Create main layout with modern styling
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Header Section
        Label headerLabel = new Label("TaskMate");
        headerLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        headerLabel.setTextFill(Color.web(PRIMARY_COLOR));
        headerLabel.setPadding(new Insets(0, 0, 20, 0));

        // Add Task Section with card-like appearance
        VBox addTaskSection = new VBox(15);
        addTaskSection.setStyle("-fx-background-color: " + CARD_BACKGROUND + "; -fx-background-radius: 10; -fx-padding: 20;");
        addTaskSection.setEffect(new javafx.scene.effect.DropShadow(5, Color.gray(0.1)));

        Label addTaskLabel = new Label("Add New Task");
        addTaskLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        addTaskLabel.setTextFill(Color.web(TEXT_COLOR));

        // Input fields with consistent styling
        TextField titleField = createStyledTextField("Title");
        TextArea descriptionArea = createStyledTextArea("Description");
        descriptionArea.setPrefRowCount(3);
        DatePicker dueDatePicker = createStyledDatePicker();
        ComboBox<Task.Priority> priorityComboBox = createStyledComboBox(Task.Priority.values(), "Priority");

        Button addButton = createStyledButton("Add Task", SECONDARY_COLOR);
        addButton.setOnAction(e -> {
            if (titleField.getText().isEmpty() || dueDatePicker.getValue() == null || priorityComboBox.getValue() == null) {
                showAlert("Missing Information", "Please fill in all required fields (Title, Due Date, and Priority).");
                return;
            }
            Task newTask = new Task(
                titleField.getText(),
                descriptionArea.getText(),
                dueDatePicker.getValue(),
                priorityComboBox.getValue()
            );
            taskManager.addTask(newTask);
            refreshTaskList();
            clearInputFields(titleField, descriptionArea, dueDatePicker, priorityComboBox);
        });

        addTaskSection.getChildren().addAll(addTaskLabel, titleField, descriptionArea, dueDatePicker, priorityComboBox, addButton);

        // Task List Section with card-like appearance
        VBox taskListSection = new VBox(15);
        taskListSection.setStyle("-fx-background-color: " + CARD_BACKGROUND + "; -fx-background-radius: 10; -fx-padding: 20;");
        taskListSection.setEffect(new javafx.scene.effect.DropShadow(5, Color.gray(0.1)));

        Label taskListLabel = new Label("Your Tasks");
        taskListLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        taskListLabel.setTextFill(Color.web(TEXT_COLOR));

        // Filtering Section with modern styling
        HBox filterBox = new HBox(15);
        filterBox.setPadding(new Insets(10, 0, 10, 0));
        
        ComboBox<Task.Priority> filterPriorityBox = createStyledComboBox(Task.Priority.values(), "Filter by Priority");
        ComboBox<Task.Status> filterStatusBox = createStyledComboBox(Task.Status.values(), "Filter by Status");
        Button showAllButton = createStyledButton("Show All", PRIMARY_COLOR);

        filterPriorityBox.setOnAction(e -> {
            if (filterPriorityBox.getValue() != null) {
                observableTasks.setAll(taskManager.getTasksByPriority(filterPriorityBox.getValue()));
            }
        });

        filterStatusBox.setOnAction(e -> {
            if (filterStatusBox.getValue() != null) {
                observableTasks.setAll(taskManager.getTasksByStatus(filterStatusBox.getValue()));
            }
        });

        showAllButton.setOnAction(e -> refreshTaskList());
        filterBox.getChildren().addAll(filterPriorityBox, filterStatusBox, showAllButton);

        // Task List with improved cell factory
        taskListView = new ListView<>(observableTasks);
        taskListView.setPrefHeight(400);
        taskListView.setStyle("-fx-background-color: transparent;");
        taskListView.setCellFactory(param -> new ListCell<Task>() {
            private final VBox content = new VBox(5);
            private final Label titleLabel = new Label();
            private final Label detailsLabel = new Label();
            
            {
                titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
                titleLabel.setTextFill(Color.web(TEXT_COLOR));
                detailsLabel.setFont(Font.font("System", 12));
                detailsLabel.setTextFill(Color.web(SUBTEXT_COLOR));
                content.getChildren().addAll(titleLabel, detailsLabel);
                content.setPadding(new Insets(5));
            }

            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    titleLabel.setText(task.getTitle());
                    detailsLabel.setText(String.format("Due: %s | Priority: %s | Status: %s",
                        task.getDueDate(),
                        task.getPriority(),
                        task.getStatus()));
                    
                    setGraphic(content);
                    setStyle(String.format(
                        "-fx-background-color: %s; -fx-background-radius: 5; -fx-padding: 10;",
                        task.getStatus() == Task.Status.COMPLETED ? COMPLETED_TASK_COLOR : PENDING_TASK_COLOR
                    ));
                }
            }
        });

        // Task Actions Section with modern styling
        HBox actionBox = new HBox(15);
        actionBox.setPadding(new Insets(10, 0, 0, 0));
        
        Button completeButton = createStyledButton("Mark Complete", SECONDARY_COLOR);
        Button reopenButton = createStyledButton("Reopen Task", ACCENT_COLOR);
        Button deleteButton = createStyledButton("Delete Task", DANGER_COLOR);

        completeButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                selectedTask.setStatus(Task.Status.COMPLETED);
                taskManager.updateTask(selectedTask);
                refreshTaskList();
            } else {
                showAlert("No Task Selected", "Please select a task to mark as complete.");
            }
        });

        reopenButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                selectedTask.setStatus(Task.Status.PENDING);
                taskManager.updateTask(selectedTask);
                refreshTaskList();
            } else {
                showAlert("No Task Selected", "Please select a task to reopen.");
            }
        });

        deleteButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDialog.setTitle("Confirm Deletion");
                confirmDialog.setHeaderText("Delete Task");
                confirmDialog.setContentText("Are you sure you want to delete this task?");
                confirmDialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        taskManager.deleteTask(selectedTask);
                        refreshTaskList();
                    }
                });
            } else {
                showAlert("No Task Selected", "Please select a task to delete.");
            }
        });

        actionBox.getChildren().addAll(completeButton, reopenButton, deleteButton);
        taskListSection.getChildren().addAll(taskListLabel, filterBox, taskListView, actionBox);

        // Add all sections to main layout
        mainLayout.getChildren().addAll(headerLabel, addTaskSection, taskListSection);

        // Create and show the scene
        Scene scene = new Scene(mainLayout, 800, 900);
        primaryStage.setTitle("TaskMate - Task Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-radius: 5; -fx-padding: 8; -fx-text-fill: " + TEXT_COLOR + ";");
        return field;
    }

    private TextArea createStyledTextArea(String prompt) {
        TextArea area = new TextArea();
        area.setPromptText(prompt);
        area.setStyle("-fx-background-radius: 5; -fx-padding: 8; -fx-text-fill: " + TEXT_COLOR + ";");
        return area;
    }

    private DatePicker createStyledDatePicker() {
        DatePicker picker = new DatePicker();
        picker.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
        return picker;
    }

    private <T> ComboBox<T> createStyledComboBox(T[] values, String prompt) {
        ComboBox<T> comboBox = new ComboBox<>(FXCollections.observableArrayList(values));
        comboBox.setPromptText(prompt);
        comboBox.setStyle("-fx-background-radius: 5; -fx-padding: 8; -fx-text-fill: " + TEXT_COLOR + ";");
        return comboBox;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format(
            "-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; " +
            "-fx-background-radius: 5; -fx-padding: 8 15;",
            color
        ));
        button.setOnMouseEntered(e -> button.setStyle(String.format(
            "-fx-background-color: derive(%s, 20%%); -fx-text-fill: white; -fx-font-weight: bold; " +
            "-fx-background-radius: 5; -fx-padding: 8 15;",
            color
        )));
        button.setOnMouseExited(e -> button.setStyle(String.format(
            "-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; " +
            "-fx-background-radius: 5; -fx-padding: 8 15;",
            color
        )));
        return button;
    }

    private void refreshTaskList() {
        observableTasks.setAll(taskManager.getAllTasks());
    }

    private void clearInputFields(TextField titleField, TextArea descriptionArea,
                                DatePicker dueDatePicker, ComboBox<Task.Priority> priorityComboBox) {
        titleField.clear();
        descriptionArea.clear();
        dueDatePicker.setValue(null);
        priorityComboBox.setValue(null);
    }

    public static void main(String[] args) {
        launch(args);
    }
} 