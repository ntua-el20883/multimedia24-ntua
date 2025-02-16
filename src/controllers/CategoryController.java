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
import model.Category;
import model.Task;
import storage.DataStore;
import view.CategoryView;

import java.util.List;
import java.util.Optional;

public class CategoryController {

    private CategoryView categoryView;
    private DataStore dataStore;

    public CategoryController(CategoryView view) {
        this.categoryView = view;
        this.dataStore = DataStore.getInstance();
        initialize();
    }

    private void initialize() {
        // Set up button actions
        categoryView.getAddCategoryBtn().setOnAction(e -> openAddCategoryDialog());
        categoryView.getEditCategoryBtn().setOnAction(e -> openEditCategoryDialog());
        categoryView.getDeleteCategoryBtn().setOnAction(e -> deleteSelectedCategory());
    }

    // Method to open Add Category Dialog
    private void openAddCategoryDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Category");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(categoryView.getStage());
        dialog.setResizable(false); // Fixed size

        GridPane grid = createCategoryFormGrid();

        // Form Fields
        Label nameLabel = new Label("Category Name:");
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
                showAlert(AlertType.ERROR, "Form Error!", "Please enter a category name.");
                return;
            }

            // Check for duplicate category names
            boolean exists = dataStore.getAllCategories().stream()
                    .anyMatch(cat -> cat.getName().equalsIgnoreCase(name));
            if (exists) {
                showAlert(AlertType.ERROR, "Duplicate Category", "This category already exists.");
                return;
            }

            // Create and add new category
            Category newCategory = new Category(name);
            dataStore.addCategory(newCategory);

            // Refresh category list in UI
            categoryView.refreshCategoryList(dataStore.getAllCategories());

            dialog.close();
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 300, 150);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // Method to open Edit Category Dialog
    private void openEditCategoryDialog() {
        Category selectedCategory = categoryView.getCategoryListView().getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert(AlertType.WARNING, "No Selection", "Please select a category to edit.");
            return;
        }

        // Prevent "Default" Category edition
        if (selectedCategory.getName().equalsIgnoreCase("Default")) {
            showAlert(Alert.AlertType.ERROR, "Edit Not Allowed", "The 'Default' category cannot be renamed.");
            return;
        }

        Stage dialog = new Stage();
        dialog.setTitle("Edit Category");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(categoryView.getStage());
        dialog.setResizable(false); // Fixed size

        GridPane grid = createCategoryFormGrid();

        // Form Fields
        Label nameLabel = new Label("New Category Name:");
        TextField nameField = new TextField(selectedCategory.getName());

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
                showAlert(AlertType.ERROR, "Form Error!", "Please enter a category name.");
                return;
            }

            // Check for duplicate category names
            boolean exists = dataStore.getAllCategories().stream()
                    .anyMatch(cat -> cat.getName().equalsIgnoreCase(newName) && cat != selectedCategory);
            if (exists) {
                showAlert(AlertType.ERROR, "Duplicate Category", "This category already exists.");
                return;
            }

            // Update category name
            dataStore.editCategory(selectedCategory, newName);

            // Refresh category list in UI
            categoryView.refreshCategoryList(dataStore.getAllCategories());

            // Update tasks that use this category
            updateTasksWithCategory(selectedCategory.getName(), newName);

            dialog.close();
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 300, 150);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // Method to delete selected category
    private void deleteSelectedCategory() {
        Category selectedCategory = categoryView.getCategoryListView().getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a category to delete.");
            return;
        }

        if (selectedCategory.getName().equalsIgnoreCase("Default")) {
            showAlert(Alert.AlertType.ERROR, "Delete Not Allowed", "The 'Default' category cannot be deleted.");
            return;
        }

        // Confirmation Dialog
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete the selected category and its associated tasks (along with their respective reminders)?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dataStore.deleteCategory(selectedCategory);
            categoryView.refreshCategoryList(dataStore.getAllCategories());
        }
    }

    // Helper method to create a GridPane for forms
    private GridPane createCategoryFormGrid() {
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

    // Method to update tasks with the new category name
    private void updateTasksWithCategory(String oldName, String newName) {
        List<Task> tasks = dataStore.getAllTasks();
        boolean updated = false;

        for (Task task : tasks) {
            if (task.getCategory().equalsIgnoreCase(oldName)) {
                task.setCategory(newName);
                updated = true;
            }
        }

        if (updated) {
            dataStore.saveAllData();
            // Optionally, refresh task list or notify user
        }
    }
}
