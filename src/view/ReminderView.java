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

public class ReminderView {

    private Stage stage;
    private BorderPane root;
    private Scene scene;

    // UI Components
    private ListView<Reminder> reminderListView;
    private Button addReminderBtn;
    private Button editReminderBtn;
    private Button deleteReminderBtn;

    public ReminderView(Stage owner, List<Reminder> reminders) {
        stage = new Stage();
        stage.setTitle("Reminder Management");
        stage.initOwner(owner);
        stage.setResizable(true);

        root = new BorderPane();
        scene = new Scene(root, 400, 300); // Initial size

        // Initialize UI Components
        initializeUI(reminders);

        stage.setScene(scene);
    }

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

    // Getters for UI Components
    public Stage getStage() {
        return stage;
    }

    public ListView<Reminder> getReminderListView() {
        return reminderListView;
    }

    public Button getAddReminderBtn() {
        return addReminderBtn;
    }

    public Button getEditReminderBtn() {
        return editReminderBtn;
    }

    public Button getDeleteReminderBtn() {
        return deleteReminderBtn;
    }

    // Method to refresh the reminder list
    public void refreshReminderList(List<Reminder> reminders) {
        reminderListView.getItems().clear();
        reminderListView.getItems().addAll(reminders);
    }
}
