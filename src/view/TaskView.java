package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Task;

import java.util.List;

public class TaskView {

    private Stage stage;
    private BorderPane root;
    private Scene scene;

    // UI Components
    private ListView<Task> taskListView;
    private Button addTaskBtn;
    private Button editTaskBtn;
    private Button deleteTaskBtn;
    private Button viewTaskBtn;

    public TaskView(Stage owner, List<Task> tasks) {
        stage = new Stage();
        stage.setTitle("Task Management");
        stage.initOwner(owner);

        root = new BorderPane();
        scene = new Scene(root, 600, 400);

        // Initialize UI Components
        initializeUI(tasks);

        stage.setScene(scene);
    }

    private void initializeUI(List<Task> tasks) {
        // Top: Header
        Label headerLabel = new Label("Manage Tasks");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox header = new HBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        root.setTop(header);

        // Center: Task List
        taskListView = new ListView<>();
        taskListView.getItems().addAll(tasks);
        taskListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                } else {
                    setText(task.getTitle() + " (" + task.getStatus() + ")");
                }
            }
        });
        root.setCenter(taskListView);

        // Bottom: Buttons
        addTaskBtn = new Button("Add Task");
        editTaskBtn = new Button("Edit Task");
        deleteTaskBtn = new Button("Delete Task");
        viewTaskBtn = new Button("View Details");

        HBox buttonBox = new HBox(10, addTaskBtn, editTaskBtn, deleteTaskBtn, viewTaskBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        root.setBottom(buttonBox);
    }

    // Getters for UI Components
    public Stage getStage() {
        return stage;
    }

    public ListView<Task> getTaskListView() {
        return taskListView;
    }

    public Button getAddTaskBtn() {
        return addTaskBtn;
    }

    public Button getEditTaskBtn() {
        return editTaskBtn;
    }

    public Button getDeleteTaskBtn() {
        return deleteTaskBtn;
    }

    public Button getViewTaskBtn() {
        return viewTaskBtn;
    }

    // Method to refresh the task list
    public void refreshTaskList(List<Task> tasks) {
        taskListView.getItems().clear();
        taskListView.getItems().addAll(tasks);
    }
}
