package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Priority;
import java.util.List;

public class PriorityView {

    private Stage stage;
    private BorderPane root;
    private Scene scene;

    // UI Components
    private ListView<Priority> priorityListView;
    private Button addPriorityBtn;
    private Button editPriorityBtn;
    private Button deletePriorityBtn;

    public PriorityView(Stage owner, List<Priority> priorities) {
        stage = new Stage();
        stage.setTitle("Priority Management");
        stage.initOwner(owner);
        stage.setResizable(true); // Allow resizing

        root = new BorderPane();
        scene = new Scene(root, 400, 300); // Initial size

        // Initialize UI Components
        initializeUI(priorities);

        stage.setScene(scene);
    }

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

    // Getters for UI Components
    public Stage getStage() {
        return stage;
    }

    public ListView<Priority> getPriorityListView() {
        return priorityListView;
    }

    public Button getAddPriorityBtn() {
        return addPriorityBtn;
    }

    public Button getEditPriorityBtn() {
        return editPriorityBtn;
    }

    public Button getDeletePriorityBtn() {
        return deletePriorityBtn;
    }

    // Method to refresh the priority list
    public void refreshPriorityList(List<Priority> priorities) {
        priorityListView.getItems().clear();
        priorityListView.getItems().addAll(priorities);
    }
}
