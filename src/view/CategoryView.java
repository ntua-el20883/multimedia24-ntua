package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Category;
import java.util.List;

/**
 * A JavaFX view for displaying and managing categories in the application.
 * <p>
 * Provides a list of categories and buttons to add, edit, or delete categories.
 * This view is controlled by the {@link controllers.CategoryController}.
 */
public class CategoryView {

    private Stage stage;
    private BorderPane root;
    private Scene scene;

    // UI Components
    private ListView<Category> categoryListView;
    private Button addCategoryBtn;
    private Button editCategoryBtn;
    private Button deleteCategoryBtn;

    /**
     * Constructs a new CategoryView window.
     *
     * @param owner      The parent {@link Stage} (usually the main application
     *                   window).
     * @param categories The initial list of categories to display.
     */
    public CategoryView(Stage owner, List<Category> categories) {
        stage = new Stage();
        stage.setTitle("Category Management");
        stage.initOwner(owner);
        stage.setResizable(true);

        root = new BorderPane();
        scene = new Scene(root, 400, 300);

        // Initialize UI Components
        initializeUI(categories);

        stage.setScene(scene);
    }

    /**
     * Initializes the UI layout, including header, category list, and action
     * buttons.
     *
     * @param categories The initial list of categories to display in the list view.
     */
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

    /**
     * Returns the underlying {@link Stage} for this view.
     *
     * @return The JavaFX {@link Stage} used by this view.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Returns the {@link ListView} displaying categories.
     *
     * @return The list view containing categories.
     */
    public ListView<Category> getCategoryListView() {
        return categoryListView;
    }

    /**
     * Returns the button used to add a new category.
     *
     * @return The 'Add' {@link Button}.
     */
    public Button getAddCategoryBtn() {
        return addCategoryBtn;
    }

    /**
     * Returns the button used to edit the selected category.
     *
     * @return The 'Edit' {@link Button}.
     */
    public Button getEditCategoryBtn() {
        return editCategoryBtn;
    }

    /**
     * Returns the button used to delete the selected category.
     *
     * @return The 'Delete' {@link Button}.
     */
    public Button getDeleteCategoryBtn() {
        return deleteCategoryBtn;
    }

    /**
     * Refreshes the list of categories displayed in the list view.
     *
     * @param categories The updated list of categories to display.
     */
    public void refreshCategoryList(List<Category> categories) {
        categoryListView.getItems().clear();
        categoryListView.getItems().addAll(categories);
    }
}
