package view.controllers;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Category;
import model.Priority;
import model.Task;
import storage.DataStore;
import view.TaskManagementView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TaskManagementController {

    private TaskManagementView taskManagementView;
    private DataStore dataStore;

    public TaskManagementController(TaskManagementView view) {
        this.taskManagementView = view;
        this.dataStore = DataStore.getInstance();
        initialize();
    }

    private void initialize() {
        // Set up button actions
        taskManagementView.getAddTaskBtn().setOnAction(e -> openAddTaskDialog());
        taskManagementView.getEditTaskBtn().setOnAction(e -> openEditTaskDialog());
        taskManagementView.getDeleteTaskBtn().setOnAction(e -> deleteSelectedTask());
        taskManagementView.getViewTaskBtn().setOnAction(e -> viewSelectedTaskDetails());
    }

    // Method to open Add Task Dialog
    private void openAddTaskDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Task");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(taskManagementView.getStage());
        dialog.setResizable(true); // Make dialog resizable

        GridPane grid = createTaskFormGrid();

        // Define column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30); // Labels take 30% width
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70); // Fields take 70% width
        grid.getColumnConstraints().addAll(col1, col2);

        // Form Fields
        TextField titleField = new TextField();
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3); // Set preferred row count for better visibility
        ComboBox<String> categoryComboBox = new ComboBox<>();
        ComboBox<String> priorityComboBox = new ComboBox<>();
        DatePicker deadlinePicker = new DatePicker();
        ComboBox<String> statusComboBox = new ComboBox<>();

        // Disable past dates in DatePicker
        deadlinePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // Optional: Highlight disabled dates
                }
            }
        });

        // Populate Category and Priority ComboBoxes
        List<Category> categories = dataStore.getAllCategories();
        for (Category cat : categories) {
            categoryComboBox.getItems().add(cat.getName());
        }

        List<Priority> priorities = dataStore.getAllPriorities();
        for (Priority pri : priorities) {
            priorityComboBox.getItems().add(pri.getName());
        }

        // Populate Status ComboBox
        statusComboBox.getItems().addAll("Open", "In Progress", "Completed", "Delayed", "Postponed");

        // Add components to grid with proper alignment
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);

        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionArea, 1, 1);

        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryComboBox, 1, 2);

        grid.add(new Label("Priority:"), 0, 3);
        grid.add(priorityComboBox, 1, 3);

        grid.add(new Label("Deadline:"), 0, 4);
        grid.add(deadlinePicker, 1, 4);

        grid.add(new Label("Status:"), 0, 5);
        grid.add(statusComboBox, 1, 5);

        // Buttons
        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttonBox = new HBox(10, saveBtn, cancelBtn);
        buttonBox.setPadding(new Insets(20, 0, 0, 0)); // Increased top padding
        buttonBox.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the right
        grid.add(buttonBox, 1, 6);

        // Button Actions
        saveBtn.setOnAction(e -> {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String category = categoryComboBox.getValue();
            String priority = priorityComboBox.getValue();
            LocalDate deadline = deadlinePicker.getValue();
            String status = statusComboBox.getValue();

            // Validate Inputs
            if (title.isEmpty() || description.isEmpty() || deadline == null || status == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all fields.");
                return;
            }

            // If the user doesn't select a category, set category to "Default"
            if (category == null || category.isEmpty()) {
                category = "Default";

                // Ensure "Default" category exists in DataStore
                boolean hasDefaultCategory = dataStore.getAllCategories().stream()
                        .anyMatch(cat -> cat.getName().equalsIgnoreCase("Default"));
                if (!hasDefaultCategory) {
                    dataStore.addCategory(new Category("Default"));
                }
            }

            // If the user doesn't select a priority, set priority to "Default"
            if (priority == null || priority.isEmpty()) {
                priority = "Default";

                // Ensure "Default" category exists in DataStore
                boolean hasDefaultPriority = dataStore.getAllPriorities().stream()
                        .anyMatch(pri -> pri.getName().equalsIgnoreCase("Default"));
                if (!hasDefaultPriority) {
                    dataStore.addPriority(new Priority("Default"));
                }
            }

            // Additional Date Validation
            if (deadline.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Invalid Deadline", "Deadline cannot be in the past.");
                return;
            }

            // Create and add new task
            Task newTask = new Task(title, description, category, priority, deadline, status);
            dataStore.getAllTasks().add(newTask);
            dataStore.saveAllData();

            // Refresh task list
            taskManagementView.refreshTaskList(dataStore.getAllTasks());

            // Update main statistics
            MainController.getInstance().updateStatistics();

            dialog.close();
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 500, 450);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // Method to open Edit Task Dialog
    private void openEditTaskDialog() {
        Task selectedTask = taskManagementView.getTaskListView().getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a task to edit.");
            return;
        }

        Stage dialog = new Stage();
        dialog.setTitle("Edit Task");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(taskManagementView.getStage());
        dialog.setResizable(true); // Make dialog resizable

        GridPane grid = createTaskFormGrid();

        // Define column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30); // Labels take 30% width
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70); // Fields take 70% width
        grid.getColumnConstraints().addAll(col1, col2);

        // Form Fields
        TextField titleField = new TextField(selectedTask.getTitle());
        TextArea descriptionArea = new TextArea(selectedTask.getDescription());
        descriptionArea.setPrefRowCount(3); // Set preferred row count for better visibility
        ComboBox<String> categoryComboBox = new ComboBox<>();
        ComboBox<String> priorityComboBox = new ComboBox<>();
        DatePicker deadlinePicker = new DatePicker(selectedTask.getDeadline());
        ComboBox<String> statusComboBox = new ComboBox<>();

        // Disable past dates in DatePicker
        deadlinePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // Optional: Highlight disabled dates
                }
            }
        });

        // Populate Category and Priority ComboBoxes
        List<Category> categories = dataStore.getAllCategories();
        for (Category cat : categories) {
            categoryComboBox.getItems().add(cat.getName());
        }
        categoryComboBox.setValue(selectedTask.getCategory());

        List<Priority> priorities = dataStore.getAllPriorities();
        for (Priority pri : priorities) {
            priorityComboBox.getItems().add(pri.getName());
        }
        priorityComboBox.setValue(selectedTask.getPriority());

        // Populate Status ComboBox
        statusComboBox.getItems().addAll("Open", "In Progress", "Completed", "Delayed", "Postponed");
        statusComboBox.setValue(selectedTask.getStatus());

        // Add components to grid with proper alignment
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);

        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionArea, 1, 1);

        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryComboBox, 1, 2);

        grid.add(new Label("Priority:"), 0, 3);
        grid.add(priorityComboBox, 1, 3);

        grid.add(new Label("Deadline:"), 0, 4);
        grid.add(deadlinePicker, 1, 4);

        grid.add(new Label("Status:"), 0, 5);
        grid.add(statusComboBox, 1, 5);

        // Buttons
        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttonBox = new HBox(10, saveBtn, cancelBtn);
        buttonBox.setPadding(new Insets(20, 0, 0, 0)); // Increased top padding
        buttonBox.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the right
        grid.add(buttonBox, 1, 6);

        // Button Actions
        saveBtn.setOnAction(e -> {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String category = categoryComboBox.getValue();
            String priority = priorityComboBox.getValue();
            LocalDate deadline = deadlinePicker.getValue();
            String status = statusComboBox.getValue();

            // Validate Inputs
            if (title.isEmpty() || description.isEmpty() || category == null || priority == null || deadline == null
                    || status == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all fields.");
                return;
            }

            // Additional Date Validation
            if (deadline.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Invalid Deadline", "Deadline cannot be in the past.");
                return;
            }

            // Assign default priority if cleared
            if (priority == null || priority.isEmpty()) {
                priority = "Default";
            }

            // Assign default category if cleared
            if (category == null || category.isEmpty()) {
                category = "Default";
            }

            // Update task attributes
            selectedTask.setTitle(title);
            selectedTask.setDescription(description);
            selectedTask.setCategory(category);
            selectedTask.setPriority(priority);
            selectedTask.setDeadline(deadline);
            selectedTask.setStatus(status);

            dataStore.saveAllData();

            // Refresh task list
            taskManagementView.refreshTaskList(dataStore.getAllTasks());

            // Update main statistics
            MainController.getInstance().updateStatistics();

            dialog.close();
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 500, 450);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // Method to delete selected task
    private void deleteSelectedTask() {
        Task selectedTask = taskManagementView.getTaskListView().getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a task to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete the selected task?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dataStore.getAllTasks().remove(selectedTask);
            dataStore.saveAllData();

            // Refresh task list
            taskManagementView.refreshTaskList(dataStore.getAllTasks());

            // Update main statistics
            MainController.getInstance().updateStatistics();
        }
    }

    // Method to view selected task details
    private void viewSelectedTaskDetails() {
        Task selectedTask = taskManagementView.getTaskListView().getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a task to view.");
            return;
        }

        Stage dialog = new Stage();
        dialog.setTitle("Task Details");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(taskManagementView.getStage());
        dialog.setResizable(true); // Make dialog resizable

        GridPane grid = createTaskFormGrid();

        // Define column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30); // Labels take 30% width
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70); // Fields take 70% width
        grid.getColumnConstraints().addAll(col1, col2);

        // Display Fields (Read-Only)
        Label titleLabel = new Label(selectedTask.getTitle());
        Label categoryLabel = new Label(selectedTask.getCategory());
        Label priorityLabel = new Label(selectedTask.getPriority());
        Label deadlineLabel = new Label(selectedTask.getDeadline().toString());
        Label statusLabel = new Label(selectedTask.getStatus());

        // Wrap description in TextArea for better readability
        TextArea descriptionArea = new TextArea(selectedTask.getDescription());
        descriptionArea.setEditable(false);
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefHeight(100); // Set preferred height

        // Add components to grid with proper alignment
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleLabel, 1, 0);

        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionArea, 1, 1);

        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryLabel, 1, 2);

        grid.add(new Label("Priority:"), 0, 3);
        grid.add(priorityLabel, 1, 3);

        grid.add(new Label("Deadline:"), 0, 4);
        grid.add(deadlineLabel, 1, 4);

        grid.add(new Label("Status:"), 0, 5);
        grid.add(statusLabel, 1, 5);

        // Close Button
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> dialog.close());
        HBox buttonBox = new HBox(closeBtn);
        buttonBox.setPadding(new Insets(20, 0, 0, 0)); // Increased top padding
        buttonBox.setAlignment(Pos.CENTER_RIGHT); // Align button to the right
        grid.add(buttonBox, 1, 6);

        Scene scene = new Scene(grid, 500, 500); // Increased dialog size
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // Helper method to create a GridPane for forms
    private GridPane createTaskFormGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(15); // Increased vertical gap
        grid.setHgap(10);
        return grid;
    }

    // Helper method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
