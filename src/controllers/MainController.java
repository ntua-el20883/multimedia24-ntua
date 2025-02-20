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

/**
 * The primary controller for managing the application's overall flow and state.
 * It initializes data, updates statistics, manages delayed-task checks, and
 * opens
 * separate windows for Task, Category, Priority, and Reminder management.
 */
public class MainController {

    private static MainController instance;

    private DataStore dataStore;
    private MainView mainView;

    /**
     * Constructs a new MainController and initializes the singleton instance.
     * Retrieves the {@link DataStore} for application-wide data access.
     */
    public MainController() {
        dataStore = DataStore.getInstance();
        instance = this;
    }

    /**
     * Loads all data from storage, updates task statuses for overdue tasks,
     * and checks for delayed tasks to potentially display a warning popup.
     */
    public void initializeData() {
        dataStore.loadAllData();
        updateTaskStatuses(); // Ensure delayed tasks are updated
        checkForDelayedTasks(); // Show warning if needed
    }

    /**
     * Associates the provided {@link MainView} with this controller and
     * sets up button actions for Task, Category, Priority, and Reminder
     * management.
     *
     * @param mainView The main application view that this controller will manage.
     */
    public void setMainView(MainView mainView) {
        this.mainView = mainView;
        this.mainView.getTaskManagementBtn().setOnAction(e -> openTaskManagementWindow());
        this.mainView.getCategoryManagementBtn().setOnAction(e -> openCategoryManagementWindow());
        this.mainView.getPriorityManagementBtn().setOnAction(e -> openPriorityManagementWindow());
        this.mainView.getReminderManagementBtn().setOnAction(e -> openReminderManagementWindow());
    }

    /**
     * Checks for any tasks in the 'Delayed' status and displays a warning
     * dialog if any are found.
     */
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

    /**
     * Opens the Task Management Window in a new stage, passing in the current
     * list of tasks, categories, and priorities for display and searching.
     */
    private void openTaskManagementWindow() {
        TaskView taskView = new TaskView(
                mainView.getStage(),
                dataStore.getAllTasks(),
                dataStore.getAllCategories().stream().map(Category::getName).toList(),
                dataStore.getAllPriorities().stream().map(Priority::getName).toList());
        TaskController taskController = new TaskController(taskView);
        taskView.getStage().showAndWait();
    }

    /**
     * Opens the Category Management Window in a new stage, allowing the user
     * to add, edit, or delete categories.
     */
    private void openCategoryManagementWindow() {
        CategoryView categoryView = new CategoryView(mainView.getStage(),
                dataStore.getAllCategories());
        CategoryController categoryController = new CategoryController(categoryView);
        categoryView.getStage().showAndWait();
    }

    /**
     * Opens the Priority Management Window in a new stage, allowing the user
     * to add, edit, or delete priority levels.
     */
    private void openPriorityManagementWindow() {
        PriorityView priorityView = new PriorityView(mainView.getStage(),
                dataStore.getAllPriorities());
        PriorityController priorityController = new PriorityController(priorityView);
        priorityView.getStage().showAndWait();
    }

    /**
     * Opens the Reminder Management Window in a new stage, allowing the user
     * to add, edit, or delete reminders for tasks.
     */
    private void openReminderManagementWindow() {
        ReminderView reminderView = new ReminderView(mainView.getStage(),
                dataStore.getAllReminders());
        ReminderController reminderController = new ReminderController(reminderView);
        reminderView.getStage().showAndWait();
    }

    /**
     * Returns the singleton instance of MainController.
     *
     * @return The active {@link MainController} instance.
     */
    public static MainController getInstance() {
        return instance;
    }

    /**
     * Updates the statistics labels in the {@link MainView},
     * including total tasks, completed tasks, delayed tasks,
     * and tasks due within 7 days.
     */
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

    /**
     * Placeholder for an alternative Priority Management action
     * (if the user does not use the {@code openPriorityManagementWindow} method).
     */
    public void handlePriorityManagement() {
        // Placeholder for Priority Management window
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Priority Management");
        alert.setHeaderText(null);
        alert.setContentText("Priority Management feature is not yet implemented.");
        alert.showAndWait();
    }

    /**
     * Placeholder for an alternative Reminder Management action
     * (if the user does not use the {@code openReminderManagementWindow} method).
     */
    public void handleReminderManagement() {
        // Placeholder for Reminder Management window
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Reminder Management");
        alert.setHeaderText(null);
        alert.setContentText("Reminder Management feature is not yet implemented.");
        alert.showAndWait();
    }

    /**
     * Saves all application data to JSON files by calling
     * {@link DataStore#saveAllData()}.
     */
    public void saveData() {
        dataStore.saveAllData();
    }

    /**
     * Updates the status of tasks whose deadlines have passed to "Delayed."
     * Skips tasks already marked "Completed."
     * 
     * If any task statuses are updated, the changes are saved to storage.
     */
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
