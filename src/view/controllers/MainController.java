package view.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Task;
import storage.DataStore;
import view.MainView;
import view.TaskManagementView;

import java.time.LocalDate;
import java.util.List;

public class MainController {

    private static MainController instance;

    private DataStore dataStore;
    private MainView mainView;

    public MainController() {
        dataStore = DataStore.getInstance();
        instance = this;
    }

    // Initialize data (load from JSON)
    public void initializeData() {
        dataStore.loadAllData();
    }

    // Set the main view
    public void setMainView(MainView mainView) {
        this.mainView = mainView;
        // Assign button actions
        this.mainView.getTaskManagementBtn().setOnAction(e -> openTaskManagementWindow());
        this.mainView.getCategoryManagementBtn().setOnAction(e -> handleCategoryManagement());
        this.mainView.getPriorityManagementBtn().setOnAction(e -> handlePriorityManagement());
        this.mainView.getReminderManagementBtn().setOnAction(e -> handleReminderManagement());
    }

    // Method to open Task Management Window
    private void openTaskManagementWindow() {
        TaskManagementView taskView = new TaskManagementView(mainView.getStage(), dataStore.getAllTasks());
        TaskManagementController taskController = new TaskManagementController(taskView);
        taskView.getStage().showAndWait();
    }

    // Getter for singleton instance
    public static MainController getInstance() {
        return instance;
    }

    // Update statistics labels
    public void updateStatistics() {
        if (mainView == null)
            return;

        List<Task> tasks = dataStore.getAllTasks();

        mainView.getTotalTasksLabel().setText("Total Tasks: " + tasks.size());

        long completed = tasks.stream()
                .filter(task -> task.getStatus().equalsIgnoreCase("Completed"))
                .count();
        mainView.getCompletedTasksLabel().setText("Completed Tasks: " + completed);

        long delayed = tasks.stream()
                .filter(task -> task.getStatus().equalsIgnoreCase("Delayed"))
                .count();
        mainView.getDelayedTasksLabel().setText("Delayed Tasks: " + delayed);

        long upcoming = tasks.stream()
                .filter(task -> task.getStatus().equalsIgnoreCase("Open") ||
                        task.getStatus().equalsIgnoreCase("In Progress") ||
                        task.getStatus().equalsIgnoreCase("Postponed"))
                .filter(task -> dataStore.isTaskDueInDays(task, 7))
                .count();
        mainView.getUpcomingTasksLabel().setText("Tasks Due in 7 Days: " + upcoming);
    }

    // Handle Task Management Button
    public void handleTaskManagement() {
        // Placeholder for Task Management window
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Task Management");
        alert.setHeaderText(null);
        alert.setContentText("Task Management feature is not yet implemented.");
        alert.showAndWait();
    }

    // Handle Category Management Button
    public void handleCategoryManagement() {
        // Placeholder for Category Management window
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Category Management");
        alert.setHeaderText(null);
        alert.setContentText("Category Management feature is not yet implemented.");
        alert.showAndWait();
    }

    // Handle Priority Management Button
    public void handlePriorityManagement() {
        // Placeholder for Priority Management window
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Priority Management");
        alert.setHeaderText(null);
        alert.setContentText("Priority Management feature is not yet implemented.");
        alert.showAndWait();
    }

    // Handle Reminder Management Button
    public void handleReminderManagement() {
        // Placeholder for Reminder Management window
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Reminder Management");
        alert.setHeaderText(null);
        alert.setContentText("Reminder Management feature is not yet implemented.");
        alert.showAndWait();
    }

    // Public method to save data
    public void saveData() {
        dataStore.saveAllData();
    }

    public void updateTaskStatuses() {
        List<Task> tasks = dataStore.getAllTasks();
        boolean updated = false;

        for (Task task : tasks) {
            if (!task.getStatus().equalsIgnoreCase("Completed") &&
                    task.getDeadline().isBefore(LocalDate.now())) {
                task.setStatus("Delayed");
                updated = true;
            }
        }

        if (updated) {
            dataStore.saveAllData();
        }
    }
}
