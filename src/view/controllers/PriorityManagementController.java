package view.controllers;

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
import view.PriorityManagementView;

import java.util.List;
import java.util.Optional;

public class PriorityManagementController {

    private PriorityManagementView priorityView;
    private DataStore dataStore;

    public PriorityManagementController(PriorityManagementView view) {
        this.priorityView = view;
        this.dataStore = DataStore.getInstance();
        initialize();
    }

    private void initialize() {
        // Set up button actions
        priorityView.getAddPriorityBtn().setOnAction(e -> openAddPriorityDialog());
        priorityView.getEditPriorityBtn().setOnAction(e -> openEditPriorityDialog());
        priorityView.getDeletePriorityBtn().setOnAction(e -> deleteSelectedPriority());
    }

    // Method to open Add Priority Dialog
    private void openAddPriorityDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Priority");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(priorityView.getStage());
        dialog.setResizable(false); // Fixed size

        GridPane grid = createPriorityFormGrid();

        // Form Fields
        Label nameLabel = new Label("Priority Name:");
        TextField nameField = new TextField();

        // Add components to grid
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);

        // Buttons
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

    // Method to open Edit Priority Dialog
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

        // Form Fields
        Label nameLabel = new Label("New Priority Name:");
        TextField nameField = new TextField(selectedPriority.getName());

        // Add components to grid
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);

        // Buttons
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

    // Method to delete selected priority
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
        confirmation.setContentText("Are you sure you want to delete the selected priority and its associated tasks?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dataStore.deletePriority(selectedPriority);
            priorityView.refreshPriorityList(dataStore.getAllPriorities());
        }
    }

    // Helper method to create a GridPane for forms
    private GridPane createPriorityFormGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        return grid;
    }

    // Helper method to show alerts
    private void showAlert(AlertType alertType, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // Method to update tasks with the new priority name
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
            // Optionally, refresh task list or notify user
        }
    }
}
