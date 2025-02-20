package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Priority;
import java.util.List;

/**
 * A JavaFX view for managing priority levels in the application.
 * <p>
 * Displays a list of available priorities and provides buttons
 * to add, edit, or delete priorities. This view is typically
 * controlled by the {@link controllers.PriorityController}.
 */
public class PriorityView {

    private Stage stage;
    private BorderPane root;
    private Scene scene;

    // UI Components
    private ListView<Priority> priorityListView;
    private Button addPriorityBtn;
    private Button editPriorityBtn;
    private Button deletePriorityBtn;

    /**
     * Constructs a new PriorityView window.
     *
     * @param owner      The parent {@link Stage} (usually the main application
     *                   window).
     * @param priorities The initial list of priorities to display.
     */
    public PriorityView(Stage owner, List<Priority> priorities) {
        stage = new Stage();
        stage.setTitle("Priority Management");
        stage.initOwner(owner);
        stage.setResizable(true);

        root = new BorderPane();
        scene = new Scene(root, 400, 300);

        // Initialize UI Components
        initializeUI(priorities);

        stage.setScene(scene);
    }

    /**
     * Sets up the layout and UI elements for displaying and managing priorities.
     *
     * @param priorities The initial list of priorities to display in the list view.
     */
    private void initializeUI(List<Priority> priorities) {
        // Top: Header
        Label headerLabel = new Label("Manage Priorities");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox header = new HBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        root.setTop(header);

        // Center: Priority List
        priorityListView = new ListView<>();
        priorityListView.getItems().addAll(priorities);
        root.setCenter(priorityListView);

        // Bottom: Buttons
        addPriorityBtn = new Button("Add");
        editPriorityBtn = new Button("Edit");
        deletePriorityBtn = new Button("Delete");

        // Set Button Sizes
        addPriorityBtn.setPrefWidth(80);
        editPriorityBtn.setPrefWidth(80);
        deletePriorityBtn.setPrefWidth(80);

        // Button Layout
        HBox buttonBox = new HBox(10, addPriorityBtn, editPriorityBtn, deletePriorityBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        root.setBottom(buttonBox);
    }

    /**
     * Returns the {@link Stage} for this view.
     *
     * @return The JavaFX {@link Stage} used by this view.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Returns the list view that displays the priorities.
     *
     * @return A {@link ListView} containing {@link Priority} objects.
     */
    public ListView<Priority> getPriorityListView() {
        return priorityListView;
    }

    /**
     * Returns the button used to add a new priority.
     *
     * @return The 'Add' {@link Button}.
     */
    public Button getAddPriorityBtn() {
        return addPriorityBtn;
    }

    /**
     * Returns the button used to edit the currently selected priority.
     *
     * @return The 'Edit' {@link Button}.
     */
    public Button getEditPriorityBtn() {
        return editPriorityBtn;
    }

    /**
     * Returns the button used to delete the currently selected priority.
     *
     * @return The 'Delete' {@link Button}.
     */
    public Button getDeletePriorityBtn() {
        return deletePriorityBtn;
    }

    /**
     * Refreshes the list of priorities displayed in the list view.
     *
     * @param priorities The updated list of {@link Priority} objects to display.
     */
    public void refreshPriorityList(List<Priority> priorities) {
        priorityListView.getItems().clear();
        priorityListView.getItems().addAll(priorities);
    }
}
