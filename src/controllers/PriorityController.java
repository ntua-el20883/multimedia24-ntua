package controllers;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Priority;
import model.Task;
import storage.DataStore;
import view.PriorityView;

import java.util.List;
import java.util.Optional;

/**
 * Controller class responsible for handling user actions related to
 * Priority management. It interacts with the {@link PriorityView} to
 * add, edit, and delete priority levels, updating the underlying
 * {@link DataStore} accordingly.
 */
public class PriorityController {

    private PriorityView priorityView;
    private DataStore dataStore;

    /**
     * Constructs a new PriorityController and initializes event handlers
     * for the provided {@link PriorityView}.
     *
     * @param view The {@link PriorityView} to be controlled.
     */
    public PriorityController(PriorityView view) {
        this.priorityView = view;
        this.dataStore = DataStore.getInstance();
        initialize();
    }

    /**
     * Sets up button actions for adding, editing, and deleting priorities
     * in the {@link PriorityView}.
     */
    private void initialize() {
        priorityView.getAddPriorityBtn().setOnAction(e -> openAddPriorityDialog());
        priorityView.getEditPriorityBtn().setOnAction(e -> openEditPriorityDialog());
        priorityView.getDeletePriorityBtn().setOnAction(e -> deleteSelectedPriority());
    }

    /**
     * Opens a dialog that allows the user to create a new priority level.
     * Validates the user input and checks for duplicate priority names
     * before saving.
     */
    private void openAddPriorityDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Priority");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(priorityView.getStage());
        dialog.setResizable(false); // Fixed size

        GridPane grid = createPriorityFormGrid();

        Label nameLabel = new Label("Priority Name:");
        TextField nameField = new TextField();

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttonBox = new HBox(10, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        grid.add(buttonBox, 1, 1);

        // Button Actions
        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showAlert(AlertType.ERROR, "Form Error!", "Please enter a priority name.");
                return;
            }

            // Check for duplicate priority names
            boolean exists = dataStore.getAllPriorities().stream()
                    .anyMatch(pri -> pri.getName().equalsIgnoreCase(name));
            if (exists) {
                showAlert(AlertType.ERROR, "Duplicate Priority", "This priority already exists.");
                return;
            }

            // Create and add new priority
            Priority newPriority = new Priority(name);
            dataStore.addPriority(newPriority);

            // Refresh priority list in UI
            priorityView.refreshPriorityList(dataStore.getAllPriorities());

            dialog.close();
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 300, 150);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    /**
     * Opens a dialog that allows the user to rename the currently selected
     * priority.
     * Prevents renaming of the "Default" priority and checks for duplicates.
     */
    private void openEditPriorityDialog() {
        Priority selectedPriority = priorityView.getPriorityListView().getSelectionModel().getSelectedItem();
        if (selectedPriority == null) {
            showAlert(AlertType.WARNING, "No Selection", "Please select a priority to edit.");
            return;
        }

        // Prevent "Default" Priority edition
        if (selectedPriority.getName().equalsIgnoreCase("Default")) {
            showAlert(Alert.AlertType.ERROR, "Edit Not Allowed", "The 'Default' priority cannot be renamed.");
            return;
        }

        Stage dialog = new Stage();
        dialog.setTitle("Edit Priority");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(priorityView.getStage());
        dialog.setResizable(false); // Fixed size

        GridPane grid = createPriorityFormGrid();

        Label nameLabel = new Label("New Priority Name:");
        TextField nameField = new TextField(selectedPriority.getName());

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        HBox buttonBox = new HBox(10, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        grid.add(buttonBox, 1, 1);

        // Button Actions
        saveBtn.setOnAction(e -> {
            String newName = nameField.getText().trim();
            if (newName.isEmpty()) {
                showAlert(AlertType.ERROR, "Form Error!", "Please enter a priority name.");
                return;
            }

            // Check for duplicate priority names
            boolean exists = dataStore.getAllPriorities().stream()
                    .anyMatch(pri -> pri.getName().equalsIgnoreCase(newName) && pri != selectedPriority);
            if (exists) {
                showAlert(AlertType.ERROR, "Duplicate Priority", "This priority already exists.");
                return;
            }

            // Update priority name
            dataStore.editPriority(selectedPriority, newName);

            // Refresh priority list in UI
            priorityView.refreshPriorityList(dataStore.getAllPriorities());

            // Update tasks that use this priority
            updateTasksWithPriority(selectedPriority.getName(), newName);

            dialog.close();
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 300, 150);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    /**
     * Deletes the currently selected priority after asking for confirmation.
     * Prevents deletion of the "Default" priority. Once deleted, any tasks
     * using this priority are assigned to "Default".
     */
    private void deleteSelectedPriority() {
        Priority selectedPriority = priorityView.getPriorityListView().getSelectionModel().getSelectedItem();
        if (selectedPriority == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a priority to delete.");
            return;
        }

        if (selectedPriority.getName().equalsIgnoreCase("Default")) {
            showAlert(Alert.AlertType.ERROR, "Delete Not Allowed", "The 'Default' priority cannot be deleted.");
            return;
        }

        // Confirmation Dialog
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText(
                "Are you sure you want to delete the selected priority? Its associated tasks will be set to the 'Default' Priority.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dataStore.deletePriority(selectedPriority);
            priorityView.refreshPriorityList(dataStore.getAllPriorities());
        }
    }

    /**
     * Creates a {@link GridPane} with spacing and padding for a priority form
     * dialog.
     *
     * @return A configured {@link GridPane} for priority input dialogs.
     */
    private GridPane createPriorityFormGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        return grid;
    }

    /**
     * Displays an alert dialog on the JavaFX Application Thread with the specified
     * parameters.
     *
     * @param alertType The {@link AlertType} (e.g., ERROR, WARNING, INFORMATION).
     * @param title     The title of the alert dialog.
     * @param message   The main message body displayed within the alert.
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Updates tasks with the old priority name to a newly specified name
     * and persists these changes to {@link DataStore}.
     *
     * @param oldName The old priority name to look for in tasks.
     * @param newName The new priority name to apply to matching tasks.
     */
    private void updateTasksWithPriority(String oldName, String newName) {
        List<Task> tasks = dataStore.getAllTasks();
        boolean updated = false;

        for (Task task : tasks) {
            if (task.getPriority().equalsIgnoreCase(oldName)) {
                task.setPriority(newName);
                updated = true;
            }
        }

        if (updated) {
            dataStore.saveAllData();
        }
    }
}
