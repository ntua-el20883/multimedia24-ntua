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
    private TextField titleSearchField;
    private ComboBox<String> categorySearchBox;
    private ComboBox<String> prioritySearchBox;
    private Button searchBtn;

    public TaskView(Stage owner, List<Task> tasks, List<String> categories, List<String> priorities) {
        stage = new Stage();
        stage.setTitle("Task Management");
        stage.initOwner(owner);

        root = new BorderPane();
        scene = new Scene(root, 600, 450);

        // Initialize UI Components
        initializeUI(tasks, categories, priorities);

        stage.setScene(scene);
    }

    private void initializeUI(List<Task> tasks, List<String> categories, List<String> priorities) {
        // Top: Header
        Label headerLabel = new Label("Manage Tasks");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox header = new HBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));

        // Create search panel
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(10));

        titleSearchField = new TextField();
        titleSearchField.setPromptText("Search by Title");

        categorySearchBox = new ComboBox<>();
        categorySearchBox.getItems().add("Any");
        categorySearchBox.getItems().addAll(categories);
        categorySearchBox.setValue("Any");

        prioritySearchBox = new ComboBox<>();
        prioritySearchBox.getItems().add("Any");
        prioritySearchBox.getItems().addAll(priorities);
        prioritySearchBox.setValue("Any");

        searchBtn = new Button("Search");

        searchBox.getChildren().addAll(
                new Label("Title:"), titleSearchField,
                new Label("Category:"), categorySearchBox,
                new Label("Priority:"), prioritySearchBox,
                searchBtn);

        // Add the search panel above the task list
        VBox topSection = new VBox(10, header, searchBox);
        topSection.setPadding(new Insets(10));
        root.setTop(topSection);

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
                    setText(task.getTitle() + " | " + task.getCategory() + " | " + task.getPriority() + " | " + task.getDeadline());
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

    public TextField getTitleSearchField() {
        return titleSearchField;
    }

    public ComboBox<String> getCategorySearchBox() {
        return categorySearchBox;
    }

    public ComboBox<String> getPrioritySearchBox() {
        return prioritySearchBox;
    }

    public Button getSearchBtn() {
        return searchBtn;
    }

    // Method to refresh the task list
    public void refreshTaskList(List<Task> tasks) {
        taskListView.getItems().clear();
        taskListView.getItems().addAll(tasks);
    }
}
