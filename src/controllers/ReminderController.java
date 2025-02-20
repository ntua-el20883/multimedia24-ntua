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

/**
 * Controller class responsible for managing reminders through the
 * {@link ReminderView}.
 * It allows users to add, edit, and delete reminders. This class handles form
 * dialogs,
 * input validation, and updates the underlying {@link DataStore} accordingly.
 */
public class ReminderController {

    private ReminderView reminderView;
    private DataStore dataStore;

    /**
     * Constructs a new ReminderController tied to the given {@link ReminderView}.
     * Initializes event handlers for adding, editing, and deleting reminders.
     *
     * @param view The {@link ReminderView} to be controlled.
     */
    public ReminderController(ReminderView view) {
        this.reminderView = view;
        this.dataStore = DataStore.getInstance();
        initialize();
    }

    /**
     * Sets up button actions in the {@link ReminderView} to open dialogs
     * for adding, editing, or deleting reminders.
     */
    private void initialize() {
        reminderView.getAddReminderBtn().setOnAction(e -> openAddReminderDialog());
        reminderView.getEditReminderBtn().setOnAction(e -> openEditReminderDialog());
        reminderView.getDeleteReminderBtn().setOnAction(e -> deleteSelectedReminder());
    }

    /**
     * Creates a {@link GridPane} with padding and spacing, suitable for
     * reminder form dialogs.
     *
     * @return A configured {@link GridPane} for reminder dialogs.
     */
    private GridPane createReminderFormGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(15);
        grid.setHgap(10);
        return grid;
    }

    /**
     * Displays an alert dialog on the JavaFX Application Thread.
     *
     * @param alertType The {@link AlertType} (e.g., ERROR, WARNING, INFORMATION).
     * @param title     The title of the alert dialog.
     * @param message   The message body shown in the alert.
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Opens a dialog for creating a new reminder. Allows the user to select a task,
     * choose a reminder option (e.g., 1 day before deadline, or a specific date),
     * and validates that the date is valid and before the task's deadline.
     */
    private void openAddReminderDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Reminder");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(reminderView.getStage());
        dialog.setResizable(false); // Fixed size

        GridPane grid = createReminderFormGrid();

        Label taskLabel = new Label("Task:");
        ComboBox<String> taskComboBox = new ComboBox<>();

        // Load active (non-completed) tasks
        List<String> activeTaskTitles = dataStore.getAllTasks().stream()
                .filter(task -> !task.getStatus().equalsIgnoreCase("Completed"))
                .map(Task::getTitle)
                .toList();
        taskComboBox.getItems().addAll(activeTaskTitles);

        // Display the deadline of the selected task
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
        datePicker.setDisable(true);
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

        // Update deadline label when a user selects a task
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

        // Adjust date based on the reminder option
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

        grid.add(taskLabel, 0, 0);
        grid.add(taskComboBox, 1, 0);

        grid.add(taskDeadlineLabel, 1, 1);

        grid.add(optionLabel, 0, 2);
        grid.add(reminderOptionComboBox, 1, 2);

        grid.add(dateLabel, 0, 3);
        grid.add(datePicker, 1, 3);

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttonBox = new HBox(10, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        grid.add(buttonBox, 1, 4);

        // Save Actin
        saveBtn.setOnAction(event -> {
            String taskTitle = taskComboBox.getValue();
            String selectedOption = reminderOptionComboBox.getValue();
            LocalDate date = datePicker.getValue();

            // Validate Inputs
            if (taskTitle == null || taskTitle.isEmpty() || selectedOption == null || date == null) {
                showAlert(AlertType.ERROR, "Form Error!", "Please select a task, reminder option, and date.");
                return;
            }

            // Validate Date
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

    /**
     * Opens a dialog to edit the currently selected reminder. Allows users to
     * change the associated task and date. Enforces date validation and ensures
     * the reminder date remains valid relative to the task deadline.
     */
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
        dialog.setResizable(false);

        GridPane grid = createReminderFormGrid();

        Label taskLabel = new Label("Task:");
        ComboBox<String> taskComboBox = new ComboBox<>();

        // Load active (non-completed) tasks
        List<String> activeTaskTitles = dataStore.getAllTasks().stream()
                .filter(task -> !task.getStatus().equalsIgnoreCase("Completed"))
                .map(Task::getTitle)
                .toList();
        taskComboBox.getItems().addAll(activeTaskTitles);
        taskComboBox.setValue(selectedReminder.getTaskTitle());

        // Display the selected task's deadline
        Label taskDeadlineLabel = new Label("Deadline: ");
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

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

        // Adjust the date picker based on changes to the reminderOptionComboBox
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

        grid.add(taskLabel, 0, 0);
        grid.add(taskComboBox, 1, 0);

        grid.add(taskDeadlineLabel, 1, 1);

        grid.add(optionLabel, 0, 2);
        grid.add(reminderOptionComboBox, 1, 2);

        grid.add(dateLabel, 0, 3);
        grid.add(datePicker, 1, 3);

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttonBox = new HBox(10, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        grid.add(buttonBox, 1, 4);

        // Save Action
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

    /**
     * Deletes the currently selected reminder after user confirmation.
     * If no reminder is selected, displays a warning alert.
     */
    private void deleteSelectedReminder() {
        Reminder selectedReminder = reminderView.getReminderListView().getSelectionModel().getSelectedItem();
        if (selectedReminder == null) {
            showAlert(AlertType.WARNING, "No Selection", "Please select a reminder to delete.");
            return;
        }

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
