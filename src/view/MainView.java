package view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import storage.DataStore;
import controllers.PriorityController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * The primary JavaFX view for the MediaLab Assistant application.
 * <p>
 * Displays high-level statistics (total tasks, completed tasks,
 * delayed tasks, upcoming tasks) and provides buttons for
 * navigating to Task, Category, Priority, and Reminder Management.
 */
public class MainView {

    private BorderPane root;
    private Scene scene;
    private Stage stage;

    // Statistics Labels
    private Label totalTasksLabel;
    private Label completedTasksLabel;
    private Label delayedTasksLabel;
    private Label upcomingTasksLabel;

    // Management Buttons
    private Button taskManagementBtn;
    private Button categoryManagementBtn;
    private Button priorityManagementBtn;
    private Button reminderManagementBtn;

    /**
     * Constructs the main application view with labeled statistics
     * and navigation buttons to different management windows.
     *
     * @param stage The JavaFX {@link Stage} on which this view is displayed.
     */
    public MainView(Stage stage) {
        this.stage = stage; // Assign the passed Stage to the class variable
        root = new BorderPane();

        // Create the header
        Label headerLabel = new Label("MediaLab Assistant");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        HBox header = new HBox(headerLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10));
        root.setTop(header);

        // Create the central grid
        GridPane centralGrid = new GridPane();
        centralGrid.setPadding(new Insets(10));
        centralGrid.setHgap(10);
        centralGrid.setVgap(10);

        // Top Half Cells (1-4) for Task Statistics
        totalTasksLabel = new Label("Total Tasks: 0");
        completedTasksLabel = new Label("Completed Tasks: 0");
        delayedTasksLabel = new Label("Delayed Tasks: 0");
        upcomingTasksLabel = new Label("Tasks Due in 7 Days: 0");

        // Style Labels
        totalTasksLabel.setStyle("-fx-font-size: 16px;");
        completedTasksLabel.setStyle("-fx-font-size: 16px;");
        delayedTasksLabel.setStyle("-fx-font-size: 16px;");
        upcomingTasksLabel.setStyle("-fx-font-size: 16px;");

        // Create Cells
        VBox cell1 = createStatCell(totalTasksLabel);
        VBox cell2 = createStatCell(completedTasksLabel);
        VBox cell3 = createStatCell(delayedTasksLabel);
        VBox cell4 = createStatCell(upcomingTasksLabel);

        // Add Cells to GridPane (Top Row)
        centralGrid.add(cell1, 0, 0);
        centralGrid.add(cell2, 1, 0);
        centralGrid.add(cell3, 2, 0);
        centralGrid.add(cell4, 3, 0);

        // Bottom Half Cells (5-8) with Buttons
        taskManagementBtn = new Button("Task Management");
        categoryManagementBtn = new Button("Category Management");
        priorityManagementBtn = new Button("Priority Management");
        reminderManagementBtn = new Button("Reminder Management");

        // Set Button Sizes
        taskManagementBtn.setPrefSize(150, 50);
        categoryManagementBtn.setPrefSize(150, 50);
        priorityManagementBtn.setPrefSize(150, 50);
        reminderManagementBtn.setPrefSize(150, 50);

        // Create Cells with Buttons
        VBox cell5 = createButtonCell(taskManagementBtn);
        VBox cell6 = createButtonCell(categoryManagementBtn);
        VBox cell7 = createButtonCell(priorityManagementBtn);
        VBox cell8 = createButtonCell(reminderManagementBtn);

        // Add Cells to GridPane (Bottom Row)
        centralGrid.add(cell5, 0, 1);
        centralGrid.add(cell6, 1, 1);
        centralGrid.add(cell7, 2, 1);
        centralGrid.add(cell8, 3, 1);

        // Set Alignment for Bottom Row
        centralGrid.setAlignment(Pos.CENTER);

        root.setCenter(centralGrid);

        // Create the scene
        scene = new Scene(root, 800, 600);

        // Apply styles directly
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Arial");

        // Set the scene to the stage
        stage.setScene(scene);
        stage.setTitle("MediaLab Assistant");
        stage.show();
    }

    /**
     * Creates a styled cell {@link VBox} that displays a statistics label.
     *
     * @param label The {@link Label} containing statistic text (e.g. 'Total Tasks:
     *              0').
     * @return A styled VBox containing the provided label.
     */
    private VBox createStatCell(Label label) {
        VBox cell = new VBox(label);
        cell.setAlignment(Pos.CENTER);
        cell.setPrefSize(180, 100);
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        return cell;
    }

    /**
     * Creates a styled cell {@link VBox} that displays a button
     * for navigation to a specific management window.
     *
     * @param button The {@link Button} to include in this cell.
     * @return A styled VBox containing the provided button.
     */
    private VBox createButtonCell(Button button) {
        VBox cell = new VBox(button);
        cell.setAlignment(Pos.CENTER);
        cell.setPrefSize(180, 100);
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        return cell;
    }

    /**
     * Gets the {@link Label} for displaying total task count.
     *
     * @return A {@link Label} showing total tasks.
     */
    public Label getTotalTasksLabel() {
        return totalTasksLabel;
    }

    /**
     * Gets the {@link Label} for displaying completed task count.
     *
     * @return A {@link Label} showing completed tasks.
     */
    public Label getCompletedTasksLabel() {
        return completedTasksLabel;
    }

    /**
     * Gets the {@link Label} for displaying delayed task count.
     *
     * @return A {@link Label} showing delayed tasks.
     */
    public Label getDelayedTasksLabel() {
        return delayedTasksLabel;
    }

    /**
     * Gets the {@link Label} for displaying upcoming task count.
     *
     * @return A {@link Label} showing tasks due in 7 days.
     */
    public Label getUpcomingTasksLabel() {
        return upcomingTasksLabel;
    }

    /**
     * Gets the {@link Button} used to open Task Management.
     *
     * @return The 'Task Management' button.
     */
    public Button getTaskManagementBtn() {
        return taskManagementBtn;
    }

    /**
     * Gets the {@link Button} used to open Category Management.
     *
     * @return The 'Category Management' button.
     */
    public Button getCategoryManagementBtn() {
        return categoryManagementBtn;
    }

    /**
     * Gets the {@link Button} used to open Priority Management.
     *
     * @return The 'Priority Management' button.
     */
    public Button getPriorityManagementBtn() {
        return priorityManagementBtn;
    }

    /**
     * Gets the {@link Button} used to open Reminder Management.
     *
     * @return The 'Reminder Management' button.
     */
    public Button getReminderManagementBtn() {
        return reminderManagementBtn;
    }

    /**
     * Returns the primary {@link Stage} associated with this view.
     *
     * @return The main stage for the application.
     */
    public Stage getStage() {
        return stage;
    }
}
