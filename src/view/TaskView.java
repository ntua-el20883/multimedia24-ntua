package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Task;

import java.util.List;

/**
 * A JavaFX view for managing tasks in the application.
 * <p>
 * Displays a list of tasks along with search filters (title, category,
 * priority),
 * and provides buttons to add, edit, delete, or view detailed information about
 * a task.
 * Typically used with {@link controllers.TaskController}.
 */
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

    /**
     * Constructs a new TaskView for task management. Initializes search panels,
     * a list view of tasks, and action buttons.
     *
     * @param owner      The parent {@link Stage} (usually the main application
     *                   window).
     * @param tasks      The initial list of tasks to display.
     * @param categories The list of category names for filtering.
     * @param priorities The list of priority names for filtering.
     */
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

    /**
     * Sets up the layout for viewing and searching tasks, including:
     * a header, search panel (title, category, priority), task list, and action
     * buttons.
     *
     * @param tasks      The initial list of tasks to display in the list view.
     * @param categories The list of categories for the search filter.
     * @param priorities The list of priorities for the search filter.
     */
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
                    // Show some key info in a single line
                    setText(task.getTitle() + " | " + task.getCategory() + " | " + task.getPriority() + " | "
                            + task.getDeadline());
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

    /**
     * Returns the {@link Stage} for this view.
     *
     * @return The JavaFX Stage used for Task Management.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Retrieves the list view that displays the tasks.
     *
     * @return A {@link ListView} of {@link Task} objects.
     */
    public ListView<Task> getTaskListView() {
        return taskListView;
    }

    /**
     * Returns the button for adding a new task.
     *
     * @return The 'Add Task' {@link Button}.
     */
    public Button getAddTaskBtn() {
        return addTaskBtn;
    }

    /**
     * Returns the button for editing the selected task.
     *
     * @return The 'Edit Task' {@link Button}.
     */
    public Button getEditTaskBtn() {
        return editTaskBtn;
    }

    /**
     * Returns the button for deleting the selected task.
     *
     * @return The 'Delete Task' {@link Button}.
     */
    public Button getDeleteTaskBtn() {
        return deleteTaskBtn;
    }

    /**
     * Returns the button for viewing more details about the selected task.
     *
     * @return The 'View Details' {@link Button}.
     */
    public Button getViewTaskBtn() {
        return viewTaskBtn;
    }

    /**
     * Retrieves the text field used for searching tasks by title.
     *
     * @return A {@link TextField} for entering a title search query.
     */
    public TextField getTitleSearchField() {
        return titleSearchField;
    }

    /**
     * Retrieves the combo box for filtering tasks by category.
     *
     * @return A {@link ComboBox} of category strings.
     */
    public ComboBox<String> getCategorySearchBox() {
        return categorySearchBox;
    }

    /**
     * Retrieves the combo box for filtering tasks by priority.
     *
     * @return A {@link ComboBox} of priority strings.
     */
    public ComboBox<String> getPrioritySearchBox() {
        return prioritySearchBox;
    }

    /**
     * Returns the button used to trigger the search operation.
     *
     * @return The 'Search' {@link Button}.
     */
    public Button getSearchBtn() {
        return searchBtn;
    }

    /**
     * Clears the current list of tasks and repopulates it with the specified list.
     *
     * @param tasks The new list of tasks to display.
     */
    public void refreshTaskList(List<Task> tasks) {
        taskListView.getItems().clear();
        taskListView.getItems().addAll(tasks);
    }
}
