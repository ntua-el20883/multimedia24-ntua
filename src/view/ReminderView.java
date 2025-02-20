package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Reminder;

import java.time.LocalDate;
import java.util.List;

/**
 * A JavaFX view for displaying and managing reminders.
 * <p>
 * Lists existing reminders (each tied to a task title and a date)
 * and provides buttons to add, edit, or delete reminders.
 * Typically controlled by the {@link controllers.ReminderController}.
 */
public class ReminderView {

    private Stage stage;
    private BorderPane root;
    private Scene scene;

    // UI Components
    private ListView<Reminder> reminderListView;
    private Button addReminderBtn;
    private Button editReminderBtn;
    private Button deleteReminderBtn;

    /**
     * Constructs a new ReminderView window.
     *
     * @param owner     The parent {@link Stage}, typically the main application window.
     * @param reminders The initial list of reminders to display.
     */
    public ReminderView(Stage owner, List<Reminder> reminders) {
        stage = new Stage();
        stage.setTitle("Reminder Management");
        stage.initOwner(owner);
        stage.setResizable(true);

        root = new BorderPane();
        scene = new Scene(root, 400, 300);

        // Initialize UI Components
        initializeUI(reminders);

        stage.setScene(scene);
    }

    /**
     * Initializes the UI components, including a header, a list view
     * for reminders, and action buttons for adding, editing, and deleting.
     *
     * @param reminders The list of reminders to display initially.
     */
    private void initializeUI(List<Reminder> reminders) {
        // Top: Header
        Label headerLabel = new Label("Manage Reminders");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox header = new HBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        root.setTop(header);

        // Center: Reminder List
        reminderListView = new ListView<>();
        reminderListView.getItems().addAll(reminders);
        reminderListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reminder reminder, boolean empty) {
                super.updateItem(reminder, empty);
                if (empty || reminder == null) {
                    setText(null);
                } else {
                    setText(reminder.getTaskTitle() + " on " + reminder.getDate());
                }
            }
        });
        root.setCenter(reminderListView);

        // Bottom: Buttons
        addReminderBtn = new Button("Add");
        editReminderBtn = new Button("Edit");
        deleteReminderBtn = new Button("Delete");

        // Set Button Sizes
        addReminderBtn.setPrefWidth(80);
        editReminderBtn.setPrefWidth(80);
        deleteReminderBtn.setPrefWidth(80);

        // Button Layout
        HBox buttonBox = new HBox(10, addReminderBtn, editReminderBtn, deleteReminderBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        root.setBottom(buttonBox);
    }

    /**
     * Returns the {@link Stage} used by this view.
     *
     * @return The JavaFX Stage for the reminder management window.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Returns the list view that displays current reminders.
     *
     * @return A {@link ListView} of {@link Reminder} objects.
     */
    public ListView<Reminder> getReminderListView() {
        return reminderListView;
    }

    /**
     * Returns the button used to add a new reminder.
     *
     * @return The 'Add' {@link Button}.
     */
    public Button getAddReminderBtn() {
        return addReminderBtn;
    }

    /**
     * Returns the button used to edit the selected reminder.
     *
     * @return The 'Edit' {@link Button}.
     */
    public Button getEditReminderBtn() {
        return editReminderBtn;
    }

    /**
     * Returns the button used to delete the selected reminder.
     *
     * @return The 'Delete' {@link Button}.
     */
    public Button getDeleteReminderBtn() {
        return deleteReminderBtn;
    }

    /**
     * Clears and repopulates the list of reminders in the list view.
     *
     * @param reminders The updated list of reminders to display.
     */
    public void refreshReminderList(List<Reminder> reminders) {
        reminderListView.getItems().clear();
        reminderListView.getItems().addAll(reminders);
    }
}
