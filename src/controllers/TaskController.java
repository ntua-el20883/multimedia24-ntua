package controllers;

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
import model.Reminder;
import model.Task;
import storage.DataStore;
import view.TaskView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for handling task-related operations in the Task Management
 * window.
 * It includes task creation, editing, deletion, viewing details, and searching.
 */
public class TaskController {

    private TaskView taskView;
    private DataStore dataStore;

    /**
     * Constructs a TaskController with the specified TaskView.
     * 
     * @param view The TaskView associated with this controller.
     */
    public TaskController(TaskView view) {
        this.taskView = view;
        this.dataStore = DataStore.getInstance();
        initialize();
    }

    /**
     * Initializes the event listeners for task-related UI components.
     */
    private void initialize() {
        taskView.getAddTaskBtn().setOnAction(e -> openAddTaskDialog());
        taskView.getEditTaskBtn().setOnAction(e -> openEditTaskDialog());
        taskView.getDeleteTaskBtn().setOnAction(e -> deleteSelectedTask());
        taskView.getViewTaskBtn().setOnAction(e -> viewSelectedTaskDetails());
        taskView.getSearchBtn().setOnAction(e -> searchTasks());
    }

    /**
     * Searches for tasks based on the user-defined criteria.
     * Filters tasks by title (partial match), category (exact match), and priority
     * (exact match).
     * Updates the task list in the view.
     */
    private void searchTasks() {
        String titleQuery = taskView.getTitleSearchField().getText().trim().toLowerCase();
        String categoryQuery = taskView.getCategorySearchBox().getValue();
        String priorityQuery = taskView.getPrioritySearchBox().getValue();
        String statusQuery = taskView.getStatusSearchBox().getValue();
        LocalDate deadlineQuery = taskView.getDeadlineSearchPicker().getValue(); 

        List<Task> filteredTasks = dataStore.getAllTasks().stream()
                .filter(task -> titleQuery.isEmpty() || task.getTitle().toLowerCase().contains(titleQuery))
                .filter(task -> "Any".equals(categoryQuery) || task.getCategory().equalsIgnoreCase(categoryQuery))
                .filter(task -> "Any".equals(priorityQuery) || task.getPriority().equalsIgnoreCase(priorityQuery))
                .filter(task -> "Any".equals(statusQuery) || task.getStatus().equalsIgnoreCase(statusQuery))
                .filter(task -> deadlineQuery == null || task.getDeadline().isBefore(deadlineQuery))
                .toList();

        taskView.refreshTaskList(filteredTasks);
    }

    /**
     * Opens a dialog for adding a new task.
     * Ensures valid user input and prevents duplicate task titles.
     * Saves the new task to the DataStore.
     */
    private void openAddTaskDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Task");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(taskView.getStage());
        dialog.setResizable(true);

        GridPane grid = createTaskFormGrid();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
        grid.getColumnConstraints().addAll(col1, col2);

        TextField titleField = new TextField();
        
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(5);
        descriptionArea.setWrapText(true); 
        descriptionArea.setPrefHeight(100);

        ComboBox<String> categoryComboBox = new ComboBox<>();
        ComboBox<String> priorityComboBox = new ComboBox<>();
        DatePicker deadlinePicker = new DatePicker();
        ComboBox<String> statusComboBox = new ComboBox<>();

        deadlinePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

        List<Category> categories = dataStore.getAllCategories();
        for (Category cat : categories) {
            categoryComboBox.getItems().add(cat.getName());
        }

        List<Priority> priorities = dataStore.getAllPriorities();
        for (Priority pri : priorities) {
            priorityComboBox.getItems().add(pri.getName());
        }

        statusComboBox.getItems().addAll("Open", "In Progress", "Completed", "Delayed", "Postponed");

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

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttonBox = new HBox(10, saveBtn, cancelBtn);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 1, 6);

        saveBtn.setOnAction(e -> {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String category = categoryComboBox.getValue();
            String priority = priorityComboBox.getValue();
            LocalDate deadline = deadlinePicker.getValue();
            String status = statusComboBox.getValue();

            if (title.isEmpty() || deadline == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error!",
                        "Please fill in all mandatory fields: Title, Deadline.");
                return;
            }

            boolean duplicate = dataStore.getAllTasks().stream()
                    .anyMatch(task -> task.getTitle().equalsIgnoreCase(title));
            if (duplicate) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Task", "A task with this title already exists.");
                return;
            }

            if (category == null || category.isEmpty()) {
                category = "Default";

                boolean hasDefaultCategory = dataStore.getAllCategories().stream()
                        .anyMatch(cat -> cat.getName().equalsIgnoreCase("Default"));
                if (!hasDefaultCategory) {
                    dataStore.addCategory(new Category("Default"));
                }
            }

            if (priority == null || priority.isEmpty()) {
                priority = "Default";

                boolean hasDefaultPriority = dataStore.getAllPriorities().stream()
                        .anyMatch(pri -> pri.getName().equalsIgnoreCase("Default"));
                if (!hasDefaultPriority) {
                    dataStore.addPriority(new Priority("Default"));
                }
            }

            if (status == null || status.isEmpty()) {
                status = "Open";
            }

            if (deadline.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Invalid Deadline", "Deadline cannot be in the past.");
                return;
            }

            Task newTask = new Task(title, description, category, priority, deadline, status);
            dataStore.getAllTasks().add(newTask);
            dataStore.saveAllData();

            taskView.refreshTaskList(dataStore.getAllTasks());

            MainController.getInstance().updateStatistics();

            dialog.close();
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 500, 450);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    /**
     * Opens a dialog for editing the selected task. Validates user input,
     * updates the task in the data store, and, if the task is marked as
     * "Completed", deletes its associated reminders.
     * If no task is selected, an alert is displayed.
     */
    private void openEditTaskDialog() {
        Task selectedTask = taskView.getTaskListView().getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a task to edit.");
            return;
        }

        Stage dialog = new Stage();
        dialog.setTitle("Edit Task");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(taskView.getStage());
        dialog.setResizable(true);

        GridPane grid = createTaskFormGrid();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
        grid.getColumnConstraints().addAll(col1, col2);

        TextField titleField = new TextField(selectedTask.getTitle());
        
        TextArea descriptionArea = new TextArea(selectedTask.getDescription());
        descriptionArea.setPrefRowCount(5);
        descriptionArea.setWrapText(true); 
        descriptionArea.setPrefHeight(100);

        ComboBox<String> categoryComboBox = new ComboBox<>();
        ComboBox<String> priorityComboBox = new ComboBox<>();
        DatePicker deadlinePicker = new DatePicker(selectedTask.getDeadline());
        ComboBox<String> statusComboBox = new ComboBox<>();

        deadlinePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

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

        statusComboBox.getItems().addAll("Open", "In Progress", "Completed", "Delayed", "Postponed");
        statusComboBox.setValue(selectedTask.getStatus());

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

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttonBox = new HBox(10, saveBtn, cancelBtn);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 1, 6);

        saveBtn.setOnAction(e -> {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String category = categoryComboBox.getValue();
            String priority = priorityComboBox.getValue();
            LocalDate deadline = deadlinePicker.getValue();
            String status = statusComboBox.getValue();

            if (title.isEmpty() || deadline == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error!",
                        "Please fill in all mandatory fields: Title, Deadline.");
                return;
            }

            boolean duplicate = dataStore.getAllTasks().stream()
                    .anyMatch(task -> task.getTitle().equalsIgnoreCase(title) && task != selectedTask);
            if (duplicate) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Task", "A task with this title already exists.");
                return;
            }

            if (deadline.isBefore(LocalDate.now()) && !selectedTask.getStatus().equalsIgnoreCase("Delayed")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Deadline", "Deadline cannot be in the past.");
                return;
            }

            if (category == null || category.isEmpty()) {
                category = "Default";

                boolean hasDefaultCategory = dataStore.getAllCategories().stream()
                        .anyMatch(cat -> cat.getName().equalsIgnoreCase("Default"));
                if (!hasDefaultCategory) {
                    dataStore.addCategory(new Category("Default"));
                }
            }

            if (priority == null || priority.isEmpty()) {
                priority = "Default";

                boolean hasDefaultPriority = dataStore.getAllPriorities().stream()
                        .anyMatch(pri -> pri.getName().equalsIgnoreCase("Default"));
                if (!hasDefaultPriority) {
                    dataStore.addPriority(new Priority("Default"));
                }
            }

            if (status == null || status.isEmpty()) {
                status = "Open";
            }

            // If task is marked Completed, remove its associated reminders
            if (status.equalsIgnoreCase("Completed")) {
                List<Reminder> remindersToRemove = dataStore.getAllReminders().stream()
                        .filter(reminder -> reminder.getTaskTitle().equalsIgnoreCase(selectedTask.getTitle()))
                        .toList();
                dataStore.getAllReminders().removeAll(remindersToRemove);
                dataStore.saveAllData();

                if (!remindersToRemove.isEmpty()) {
                    showAlert(Alert.AlertType.INFORMATION, "Reminders Removed",
                            "All reminders associated with this task have been removed as the task is now completed.");
                }
            }

            // --- New Reminder Handling for Deadline Changes ---

            // Capture the old deadline before updating the task
            LocalDate oldDeadline = selectedTask.getDeadline();

            // Get all reminders for this task
            List<Reminder> remindersForTask = dataStore.getAllReminders().stream()
                    .filter(reminder -> reminder.getTaskTitle().equalsIgnoreCase(selectedTask.getTitle()))
                    .toList();

            // Lists for relative reminders and those that become past after recalculation
            List<Reminder> relativeReminders = new java.util.ArrayList<>();
            List<Reminder> remindersThatBecomePast = new java.util.ArrayList<>();
            List<Reminder> remindersToRemove = new java.util.ArrayList<>();

            for (Reminder reminder : remindersForTask) {
                LocalDate oldReminderDate = reminder.getDate();
                // Determine if the reminder is set relative to the old deadline
                if (oldReminderDate.equals(oldDeadline.minusDays(1)) ||
                        oldReminderDate.equals(oldDeadline.minusWeeks(1)) ||
                        oldReminderDate.equals(oldDeadline.minusMonths(1))) {
                    relativeReminders.add(reminder);
                } else {
                    // For absolute-date reminders, if they now fall after deadline, mark for
                    // removal
                    if (oldReminderDate.isAfter(deadline)) {
                        remindersToRemove.add(reminder);
                    }
                }
            }

            // Recalculate new dates for relative reminders
            for (Reminder reminder : relativeReminders) {
                LocalDate newReminderDate = null;
                if (reminder.getDate().equals(oldDeadline.minusDays(1))) {
                    newReminderDate = deadline.minusDays(1);
                } else if (reminder.getDate().equals(oldDeadline.minusWeeks(1))) {
                    newReminderDate = deadline.minusWeeks(1);
                } else if (reminder.getDate().equals(oldDeadline.minusMonths(1))) {
                    newReminderDate = deadline.minusMonths(1);
                }
                if (newReminderDate != null) {
                    // If recalculated date is after deadline, mark for removal
                    if (newReminderDate.isAfter(deadline)) {
                        remindersToRemove.add(reminder);
                    }
                    // If recalculated date is before today, add to the warning list
                    else if (newReminderDate.isBefore(LocalDate.now())) {
                        remindersThatBecomePast.add(reminder);
                    } else {
                        // Otherwise, update the reminder with the new date
                        reminder.setDate(newReminderDate);
                    }
                }
            }

            // If any relative reminders are recalculated to a past date, warn the user
            if (!remindersThatBecomePast.isEmpty()) {
                Alert pastAlert = new Alert(Alert.AlertType.CONFIRMATION);
                pastAlert.setTitle("Warning: Reminders in the Past");
                pastAlert.setHeaderText("Some relative reminders will be set to a past date.");
                pastAlert.setContentText("These reminders will be recalculated and may now be in the past. " +
                        "Please check them manually. Do you want to proceed?");
                ButtonType proceedBtn = new ButtonType("Proceed", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                pastAlert.getButtonTypes().setAll(proceedBtn, cancelType);
                Optional<ButtonType> pastResult = pastAlert.showAndWait();
                if (pastResult.isEmpty() || pastResult.get() == cancelType) {
                    // User canceled the operation
                    return;
                } else {
                    // For those reminders, update them even if they are in the past.
                    for (Reminder reminder : remindersThatBecomePast) {
                        if (reminder.getDate().equals(oldDeadline.minusDays(1))) {
                            reminder.setDate(deadline.minusDays(1));
                        } else if (reminder.getDate().equals(oldDeadline.minusWeeks(1))) {
                            reminder.setDate(deadline.minusWeeks(1));
                        } else if (reminder.getDate().equals(oldDeadline.minusMonths(1))) {
                            reminder.setDate(deadline.minusMonths(1));
                        }
                    }
                }
            }

            // If any absolute reminders (or relative ones that couldn't be recalculated
            // properly)
            // are invalid (i.e., fall after deadline), prompt for confirmation and
            // remove them.
            if (!remindersToRemove.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Reminders No Longer Valid");
                alert.setHeaderText(null);
                alert.setContentText("Some reminders occur after the new deadline (" + deadline + ").\n" +
                        "They will be removed if you continue.\n\nDo you want to proceed?");
                ButtonType proceedBtn = new ButtonType("Proceed", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(proceedBtn, cancelType);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isEmpty() || result.get() == cancelType) {
                    // Abort the update if the user cancels
                    return;
                } else {
                    dataStore.getAllReminders().removeAll(remindersToRemove);
                    dataStore.saveAllData();
                }
            }

            // --- End Reminder Handling ---

            selectedTask.setTitle(title);
            selectedTask.setDescription(description);
            selectedTask.setCategory(category);
            selectedTask.setPriority(priority);
            selectedTask.setDeadline(deadline);
            selectedTask.setStatus(status);

            dataStore.saveAllData();

            taskView.refreshTaskList(dataStore.getAllTasks());

            MainController.getInstance().updateStatistics();

            dialog.close();
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 500, 450);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    /**
     * Deletes the selected task after user confirmation. Also removes all
     * associated reminders and updates the main statistics.
     */
    private void deleteSelectedTask() {
        Task selectedTask = taskView.getTaskListView().getSelectionModel().getSelectedItem();
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

            // Remove the task
            dataStore.getAllTasks().remove(selectedTask);

            // Remove associated reminders
            List<Reminder> remindersToRemove = dataStore.getAllReminders().stream()
                    .filter(reminder -> reminder.getTaskTitle().equalsIgnoreCase(selectedTask.getTitle()))
                    .toList();
            dataStore.getAllReminders().removeAll(remindersToRemove);

            // Save the updated data
            dataStore.saveAllData();

            // Refresh task and reminder lists
            taskView.refreshTaskList(dataStore.getAllTasks());

            // Update main statistics
            MainController.getInstance().updateStatistics();

            // Inform the user about the removal of associated reminders
            if (!remindersToRemove.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Reminders Removed",
                        "All reminders associated with the deleted task have been removed.");
            }
        }
    }

    /**
     * Opens a dialog to view details of the selected task in a read-only form.
     * If no task is selected, an alert is displayed.
     */
    private void viewSelectedTaskDetails() {
        Task selectedTask = taskView.getTaskListView().getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a task to view.");
            return;
        }

        Stage dialog = new Stage();
        dialog.setTitle("Task Details");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(taskView.getStage());
        dialog.setResizable(true);

        GridPane grid = createTaskFormGrid();

        // Define column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
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
        descriptionArea.setPrefRowCount(5); 
        descriptionArea.setPrefHeight(100);

        // Add components to grid
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
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 1, 6);

        Scene scene = new Scene(grid, 500, 500);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    /**
     * Creates a GridPane with predefined padding and spacing for forms.
     * 
     * @return A new GridPane instance.
     */
    private GridPane createTaskFormGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(15);
        grid.setHgap(10);
        return grid;
    }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     * 
     * @param alertType The type of alert (e.g., ERROR, WARNING, INFORMATION).
     * @param title     The title of the alert dialog.
     * @param message   The message content of the alert.
     */
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
