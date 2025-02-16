package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Reminder;
import model.Task;
import storage.DataStore;
import view.ReminderView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReminderController {

    private ReminderView reminderView;
    private DataStore dataStore;

    public ReminderController(ReminderView view) {
        this.reminderView = view;
        this.dataStore = DataStore.getInstance();
        initialize();
    }

    private void initialize() {
        // Set up button actions
        reminderView.getAddReminderBtn().setOnAction(e -> openAddReminderDialog());
        reminderView.getEditReminderBtn().setOnAction(e -> openEditReminderDialog());
        reminderView.getDeleteReminderBtn().setOnAction(e -> deleteSelectedReminder());
    }

    // Helper method to create a GridPane for forms
    private GridPane createReminderFormGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(15); // Increased vertical gap
        grid.setHgap(10);
        return grid;
    }

    // Helper method to show alerts
    private void showAlert(AlertType alertType, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // Method to open Add Reminder Dialog
    private void openAddReminderDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Reminder");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(reminderView.getStage());
        dialog.setResizable(false); // Fixed size

        GridPane grid = createReminderFormGrid();

        // Form Fields
        Label taskLabel = new Label("Task:");
        ComboBox<String> taskComboBox = new ComboBox<>();

        // Fetch tasks that are not completed
        List<String> activeTaskTitles = dataStore.getAllTasks().stream()
                .filter(task -> !task.getStatus().equalsIgnoreCase("Completed"))
                .map(Task::getTitle)
                .toList();
        taskComboBox.getItems().addAll(activeTaskTitles);

        // Label to display the selected task's deadline
        Label taskDeadlineLabel = new Label("Deadline: ");

        Label optionLabel = new Label("Reminder Option:");
        ComboBox<String> reminderOptionComboBox = new ComboBox<>();

        ObservableList<String> options = FXCollections.observableArrayList(
                "1 day before the Deadline",
                "1 week before the Deadline",
                "1 month before the Deadline",
                "Other Date");
        reminderOptionComboBox.setItems(options);

        Label dateLabel = new Label("Reminder Date:");

        DatePicker datePicker = new DatePicker();
        datePicker.setDisable(true); // Disabled by default
        datePicker.setDayCellFactory(picker -> new DateCell() { // Highlight past dates with red
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

        // Update the deadline label when a task is selected
        taskComboBox.setOnAction(event -> {
            String selectedTitle = taskComboBox.getValue();
            if (selectedTitle != null) {
                Task selectedTask = dataStore.getAllTasks().stream()
                        .filter(task -> task.getTitle().equalsIgnoreCase(selectedTitle))
                        .findFirst().orElse(null);
                if (selectedTask != null) {
                    taskDeadlineLabel.setText("Deadline: " + selectedTask.getDeadline().toString());
                }
            }
        });

        // Add a listener to handle selection changes
        reminderOptionComboBox.setOnAction(event -> {
            String selectedOption = reminderOptionComboBox.getValue();
            if (selectedOption == null)
                return;

            switch (selectedOption) {
                case "1 day before the Deadline":
                    if (taskComboBox.getValue() != null) {
                        Task selectedTask = dataStore.getAllTasks().stream()
                                .filter(task -> task.getTitle().equalsIgnoreCase(taskComboBox.getValue()))
                                .findFirst()
                                .orElse(null);
                        if (selectedTask != null) {
                            LocalDate calculatedDate = selectedTask.getDeadline().minusDays(1);
                            datePicker.setValue(calculatedDate);
                            datePicker.setDisable(true);
                            dateLabel.setText("Reminder Date: " + calculatedDate.toString());
                        }
                    }
                    break;
                case "1 week before the Deadline":
                    if (taskComboBox.getValue() != null) {
                        Task selectedTask = dataStore.getAllTasks().stream()
                                .filter(task -> task.getTitle().equalsIgnoreCase(taskComboBox.getValue()))
                                .findFirst()
                                .orElse(null);
                        if (selectedTask != null) {
                            LocalDate calculatedDate = selectedTask.getDeadline().minusWeeks(1);
                            datePicker.setValue(calculatedDate);
                            datePicker.setDisable(true);
                            dateLabel.setText("Reminder Date: " + calculatedDate.toString());
                        }
                    }
                    break;
                case "1 month before the Deadline":
                    if (taskComboBox.getValue() != null) {
                        Task selectedTask = dataStore.getAllTasks().stream()
                                .filter(task -> task.getTitle().equalsIgnoreCase(taskComboBox.getValue()))
                                .findFirst()
                                .orElse(null);
                        if (selectedTask != null) {
                            LocalDate calculatedDate = selectedTask.getDeadline().minusMonths(1);
                            datePicker.setValue(calculatedDate);
                            datePicker.setDisable(true);
                            dateLabel.setText("Reminder Date: " + calculatedDate.toString());
                        }
                    }
                    break;
                case "Other Date":
                    datePicker.setDisable(false);
                    datePicker.setValue(null);
                    dateLabel.setText("Reminder Date:");
                    break;
                default:
                    datePicker.setDisable(true);
                    dateLabel.setText("Reminder Date:");
                    break;
            }
        });

        // Add components to grid
        grid.add(taskLabel, 0, 0);
        grid.add(taskComboBox, 1, 0);

        grid.add(taskDeadlineLabel, 1, 1);

        grid.add(optionLabel, 0, 2);
        grid.add(reminderOptionComboBox, 1, 2);

        grid.add(dateLabel, 0, 3);
        grid.add(datePicker, 1, 3);

        // Buttons
        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttonBox = new HBox(10, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        grid.add(buttonBox, 1, 4);

        // Button Actions
        saveBtn.setOnAction(event -> {
            String taskTitle = taskComboBox.getValue();
            String selectedOption = reminderOptionComboBox.getValue();
            LocalDate date = datePicker.getValue();

            // Validate Inputs
            if (taskTitle == null || taskTitle.isEmpty() || selectedOption == null || date == null) {
                showAlert(AlertType.ERROR, "Form Error!", "Please select a task, reminder option, and date.");
                return;
            }

            // Check if the date is before the Task's deadline
            Task selectedTask = dataStore.getAllTasks().stream()
                    .filter(task -> task.getTitle().equalsIgnoreCase(taskTitle))
                    .findFirst()
                    .orElse(null);
            if (selectedTask == null) {
                showAlert(AlertType.ERROR, "Error", "Selected task does not exist.");
                return;
            }
            if (date.isAfter(selectedTask.getDeadline())) {
                showAlert(AlertType.ERROR, "Invalid Date", "Reminder date must be before the Task's deadline.");
                return;
            }

            // Create and add new reminder
            Reminder newReminder = new Reminder(taskTitle, date);
            try {
                dataStore.addReminder(newReminder);
                reminderView.refreshReminderList(dataStore.getAllReminders());
                dialog.close();
            } catch (IllegalArgumentException ex) {
                showAlert(AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        cancelBtn.setOnAction(event -> dialog.close());

        Scene scene = new Scene(grid, 400, 250);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // Method to open Edit Reminder Dialog
    private void openEditReminderDialog() {
        Reminder selectedReminder = reminderView.getReminderListView().getSelectionModel().getSelectedItem();
        if (selectedReminder == null) {
            showAlert(AlertType.WARNING, "No Selection", "Please select a reminder to edit.");
            return;
        }

        Stage dialog = new Stage();
        dialog.setTitle("Edit Reminder");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(reminderView.getStage());
        dialog.setResizable(false); // Fixed size

        GridPane grid = createReminderFormGrid();

        // Form Fields
        Label taskLabel = new Label("Task:");
        ComboBox<String> taskComboBox = new ComboBox<>();

        // Fetch tasks that are not completed
        List<String> activeTaskTitles = dataStore.getAllTasks().stream()
                .filter(task -> !task.getStatus().equalsIgnoreCase("Completed"))
                .map(Task::getTitle)
                .toList();
        taskComboBox.getItems().addAll(activeTaskTitles);
        taskComboBox.setValue(selectedReminder.getTaskTitle());

        // Label to display the selected task's deadline
        Label taskDeadlineLabel = new Label("Deadline: ");
        // Immediately update the deadline label using the current task selection
        String initialTask = taskComboBox.getValue();
        if (initialTask != null) {
            Task initialSelectedTask = dataStore.getAllTasks().stream()
                    .filter(task -> task.getTitle().equalsIgnoreCase(initialTask))
                    .findFirst().orElse(null);
            if (initialSelectedTask != null) {
                taskDeadlineLabel.setText("Deadline: " + initialSelectedTask.getDeadline().toString());
            }
        }

        Label optionLabel = new Label("Reminder Option:");
        ComboBox<String> reminderOptionComboBox = new ComboBox<>();

        ObservableList<String> options = FXCollections.observableArrayList(
                "1 day before the Deadline",
                "1 week before the Deadline",
                "1 month before the Deadline",
                "Other Date");
        reminderOptionComboBox.setItems(options);

        // Determine which option was originally selected
        Task associatedTask = dataStore.getAllTasks().stream()
                .filter(task -> task.getTitle().equalsIgnoreCase(selectedReminder.getTaskTitle()))
                .findFirst()
                .orElse(null);
        if (associatedTask != null) {
            LocalDate deadline = associatedTask.getDeadline();
            LocalDate reminderDate = selectedReminder.getDate();
            if (reminderDate.equals(deadline.minusDays(1))) {
                reminderOptionComboBox.setValue("1 day before the Deadline");
            } else if (reminderDate.equals(deadline.minusWeeks(1))) {
                reminderOptionComboBox.setValue("1 week before the Deadline");
            } else if (reminderDate.equals(deadline.minusMonths(1))) {
                reminderOptionComboBox.setValue("1 month before the Deadline");
            } else {
                reminderOptionComboBox.setValue("Other Date");
            }
        }

        // Enable DatePicker if the initial option is "Other Date"
        Label dateLabel = new Label("Reminder Date:");
        DatePicker datePicker = new DatePicker(selectedReminder.getDate());
        if ("Other Date".equals(reminderOptionComboBox.getValue())) {
            datePicker.setDisable(false);
        } else {
            datePicker.setDisable(true);
        }
        
        datePicker.setDayCellFactory(picker -> new DateCell() { // Highlight past dates with red
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

        // Add a listener to handle selection changes
        reminderOptionComboBox.setOnAction(event -> {
            String selectedOption = reminderOptionComboBox.getValue();
            if (selectedOption == null)
                return;

            switch (selectedOption) {
                case "1 day before the Deadline":
                    if (taskComboBox.getValue() != null) {
                        Task selectedTask = dataStore.getAllTasks().stream()
                                .filter(task -> task.getTitle().equalsIgnoreCase(taskComboBox.getValue()))
                                .findFirst()
                                .orElse(null);
                        if (selectedTask != null) {
                            LocalDate calculatedDate = selectedTask.getDeadline().minusDays(1);
                            datePicker.setValue(calculatedDate);
                            datePicker.setDisable(true);
                            dateLabel.setText("Reminder Date: " + calculatedDate.toString());
                        }
                    }
                    break;
                case "1 week before the Deadline":
                    if (taskComboBox.getValue() != null) {
                        Task selectedTask = dataStore.getAllTasks().stream()
                                .filter(task -> task.getTitle().equalsIgnoreCase(taskComboBox.getValue()))
                                .findFirst()
                                .orElse(null);
                        if (selectedTask != null) {
                            LocalDate calculatedDate = selectedTask.getDeadline().minusWeeks(1);
                            datePicker.setValue(calculatedDate);
                            datePicker.setDisable(true);
                            dateLabel.setText("Reminder Date: " + calculatedDate.toString());
                        }
                    }
                    break;
                case "1 month before the Deadline":
                    if (taskComboBox.getValue() != null) {
                        Task selectedTask = dataStore.getAllTasks().stream()
                                .filter(task -> task.getTitle().equalsIgnoreCase(taskComboBox.getValue()))
                                .findFirst()
                                .orElse(null);
                        if (selectedTask != null) {
                            LocalDate calculatedDate = selectedTask.getDeadline().minusMonths(1);
                            datePicker.setValue(calculatedDate);
                            datePicker.setDisable(true);
                            dateLabel.setText("Reminder Date: " + calculatedDate.toString());
                        }
                    }
                    break;
                case "Other Date":
                    datePicker.setDisable(false);
                    datePicker.setValue(null);
                    dateLabel.setText("Reminder Date:");
                    break;
                default:
                    datePicker.setDisable(true);
                    dateLabel.setText("Reminder Date:");
                    break;
            }
        });

        // Add components to grid
        grid.add(taskLabel, 0, 0);
        grid.add(taskComboBox, 1, 0);

        grid.add(taskDeadlineLabel, 1, 1);

        grid.add(optionLabel, 0, 2);
        grid.add(reminderOptionComboBox, 1, 2);

        grid.add(dateLabel, 0, 3);
        grid.add(datePicker, 1, 3);

        // Buttons
        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttonBox = new HBox(10, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        grid.add(buttonBox, 1, 4);

        // Button Actions
        saveBtn.setOnAction(event -> {
            String newTaskTitle = taskComboBox.getValue();
            String selectedOption = reminderOptionComboBox.getValue();
            LocalDate newDate = datePicker.getValue();

            // Validate Inputs
            if (newTaskTitle == null || newTaskTitle.isEmpty() || selectedOption == null || newDate == null) {
                showAlert(AlertType.ERROR, "Form Error!", "Please select a task, reminder option, and date.");
                return;
            }

            // Check if the reminder date is not in the past
            if (newDate.isBefore(LocalDate.now())) {
                showAlert(AlertType.ERROR, "Invalid Date", "Reminder date cannot be in the past.");
                return;
            }

            // Check if the date is before the Task's deadline
            Task selectedTask = dataStore.getAllTasks().stream()
                    .filter(task -> task.getTitle().equalsIgnoreCase(newTaskTitle))
                    .findFirst()
                    .orElse(null);
            if (selectedTask == null) {
                showAlert(AlertType.ERROR, "Error", "Selected task does not exist.");
                return;
            }
            if (newDate.isAfter(selectedTask.getDeadline())) {
                showAlert(AlertType.ERROR, "Invalid Date", "Reminder date must be before the Task's deadline.");
                return;
            }

            // Update the reminder
            selectedReminder.setTaskTitle(newTaskTitle);
            selectedReminder.setDate(newDate);

            // Save the changes
            dataStore.editReminder(selectedReminder, selectedReminder);

            // Refresh the reminder list
            reminderView.refreshReminderList(dataStore.getAllReminders());

            dialog.close();
        });

        cancelBtn.setOnAction(event -> dialog.close());

        Scene scene = new Scene(grid, 400, 250);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // Method to delete selected reminder
    private void deleteSelectedReminder() {
        Reminder selectedReminder = reminderView.getReminderListView().getSelectionModel().getSelectedItem();
        if (selectedReminder == null) {
            showAlert(AlertType.WARNING, "No Selection", "Please select a reminder to delete.");
            return;
        }

        // Confirmation Dialog
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete the selected reminder?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dataStore.deleteReminder(selectedReminder);
            reminderView.refreshReminderList(dataStore.getAllReminders());
        }
    }
}
