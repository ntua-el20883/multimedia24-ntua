package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Category;
import java.util.List; // Added import

public class CategoryManagementView {

    private Stage stage;
    private BorderPane root;
    private Scene scene;

    // UI Components
    private ListView<Category> categoryListView;
    private Button addCategoryBtn;
    private Button editCategoryBtn;
    private Button deleteCategoryBtn;

    public CategoryManagementView(Stage owner, List<Category> categories) {
        stage = new Stage();
        stage.setTitle("Category Management");
        stage.initOwner(owner);
        stage.setResizable(true); // Allow resizing

        root = new BorderPane();
        scene = new Scene(root, 400, 300); // Initial size

        // Initialize UI Components
        initializeUI(categories);

        stage.setScene(scene);
    }

    private void initializeUI(List<Category> categories) {
        // Top: Header
        Label headerLabel = new Label("Manage Categories");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox header = new HBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        root.setTop(header);

        // Center: Category List
        categoryListView = new ListView<>();
        categoryListView.getItems().addAll(categories);
        root.setCenter(categoryListView);

        // Bottom: Buttons
        addCategoryBtn = new Button("Add");
        editCategoryBtn = new Button("Edit");
        deleteCategoryBtn = new Button("Delete");

        // Set Button Sizes
        addCategoryBtn.setPrefWidth(80);
        editCategoryBtn.setPrefWidth(80);
        deleteCategoryBtn.setPrefWidth(80);

        // Button Layout
        HBox buttonBox = new HBox(10, addCategoryBtn, editCategoryBtn, deleteCategoryBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        root.setBottom(buttonBox);
    }

    // Getters for UI Components
    public Stage getStage() {
        return stage;
    }

    public ListView<Category> getCategoryListView() {
        return categoryListView;
    }

    public Button getAddCategoryBtn() {
        return addCategoryBtn;
    }

    public Button getEditCategoryBtn() {
        return editCategoryBtn;
    }

    public Button getDeleteCategoryBtn() {
        return deleteCategoryBtn;
    }

    // Method to refresh the category list
    public void refreshCategoryList(List<Category> categories) {
        categoryListView.getItems().clear();
        categoryListView.getItems().addAll(categories);
    }
}
