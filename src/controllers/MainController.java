package controllers;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Task;
import model.Category;
import model.Priority;
import storage.DataStore;
import view.MainView;
import view.PriorityView;
import view.ReminderView;
import view.TaskView;
import view.CategoryView;

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
        updateTaskStatuses(); // Ensure delayed tasks are updated
        checkForDelayedTasks(); // Show warning if needed
    }

    // Set the main view
    public void setMainView(MainView mainView) {
        this.mainView = mainView;
        // Assign button actions
        this.mainView.getTaskManagementBtn().setOnAction(e -> openTaskManagementWindow());
        this.mainView.getCategoryManagementBtn().setOnAction(e -> openCategoryManagementWindow());
        this.mainView.getPriorityManagementBtn().setOnAction(e -> openPriorityManagementWindow());
        this.mainView.getReminderManagementBtn().setOnAction(e -> openReminderManagementWindow());
    }

    private void checkForDelayedTasks() {
        long delayedTasks = dataStore.getAllTasks().stream()
                .filter(task -> task.getStatus().equalsIgnoreCase("Delayed"))
                .count();

        if (delayedTasks > 0) {
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Overdue Tasks");
                alert.setHeaderText("Attention: Delayed Tasks Found");
                alert.setContentText("There are " + delayedTasks + " overdue tasks that require your attention.");
                alert.showAndWait();
            });
        }
    }

    // Method to open Task Management Window
    private void openTaskManagementWindow() {
        TaskView taskView = new TaskView(
                mainView.getStage(),
                dataStore.getAllTasks(),
                dataStore.getAllCategories().stream().map(Category::getName).toList(),
                dataStore.getAllPriorities().stream().map(Priority::getName).toList());
        TaskController taskController = new TaskController(taskView);
        taskView.getStage().showAndWait();
    }

    // Method to open Category Management Window
    private void openCategoryManagementWindow() {
        CategoryView categoryView = new CategoryView(mainView.getStage(),
                dataStore.getAllCategories());
        CategoryController categoryController = new CategoryController(categoryView);
        categoryView.getStage().showAndWait();
    }

    // Method to open Priority Management Window
    private void openPriorityManagementWindow() {
        PriorityView priorityView = new PriorityView(mainView.getStage(),
                dataStore.getAllPriorities());
        PriorityController priorityController = new PriorityController(priorityView);
        priorityView.getStage().showAndWait();
    }

    // Method to open Reminder Management Window
    private void openReminderManagementWindow() {
        ReminderView reminderView = new ReminderView(mainView.getStage(),
                dataStore.getAllReminders());
        ReminderController reminderController = new ReminderController(reminderView);
        reminderView.getStage().showAndWait();
    }

    // Getter for singleton instance
    public static MainController getInstance() {
        return instance;
    }

    // Update statistics labels
    public void updateStatistics() {
        if (mainView == null)
            return;
        Platform.runLater(() -> {
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
        });
    }

    // Handle Priority Management Button (if not used anymore, can be removed)
    public void handlePriorityManagement() {
        // Placeholder for Priority Management window
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Priority Management");
        alert.setHeaderText(null);
        alert.setContentText("Priority Management feature is not yet implemented.");
        alert.showAndWait();
    }

    // Handle Reminder Management Button (if using openReminderManagementWindow, can
    // remove this)
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
