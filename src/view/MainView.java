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
    private Button categoryManagementBtn; // New Button
    private Button priorityManagementBtn;
    private Button reminderManagementBtn;

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
        categoryManagementBtn.setPrefSize(150, 50); // Set size
        priorityManagementBtn.setPrefSize(150, 50);
        reminderManagementBtn.setPrefSize(150, 50);

        // Create Cells with Buttons
        VBox cell5 = createButtonCell(taskManagementBtn);
        VBox cell6 = createButtonCell(categoryManagementBtn); // Add new button
        VBox cell7 = createButtonCell(priorityManagementBtn);
        VBox cell8 = createButtonCell(reminderManagementBtn);

        // Add Cells to GridPane (Bottom Row)
        centralGrid.add(cell5, 0, 1);
        centralGrid.add(cell6, 1, 1); // Add new button to grid
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

    // Helper method to create a statistics cell
    private VBox createStatCell(Label label) {
        VBox cell = new VBox(label);
        cell.setAlignment(Pos.CENTER);
        cell.setPrefSize(180, 100);
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        return cell;
    }

    // Helper method to create a button cell
    private VBox createButtonCell(Button button) {
        VBox cell = new VBox(button);
        cell.setAlignment(Pos.CENTER);
        cell.setPrefSize(180, 100);
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        return cell;
    }

    // Getters for Labels and Buttons to allow controller access
    public Label getTotalTasksLabel() {
        return totalTasksLabel;
    }

    public Label getCompletedTasksLabel() {
        return completedTasksLabel;
    }

    public Label getDelayedTasksLabel() {
        return delayedTasksLabel;
    }

    public Label getUpcomingTasksLabel() {
        return upcomingTasksLabel;
    }

    public Button getTaskManagementBtn() {
        return taskManagementBtn;
    }

    public Button getCategoryManagementBtn() {
        return categoryManagementBtn;
    }

    public Button getPriorityManagementBtn() {
        return priorityManagementBtn;
    }

    public Button getReminderManagementBtn() {
        return reminderManagementBtn;
    }

    // **New Getter Method for Stage**
    public Stage getStage() {
        return stage;
    }
}
