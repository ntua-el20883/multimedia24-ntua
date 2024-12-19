package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create the root layout
            BorderPane root = new BorderPane();

            // Create the header
            Label headerLabel = new Label("MediaLab Assistant");
            headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            HBox header = new HBox(headerLabel);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setPadding(new Insets(10));
            root.setTop(header);

            // Create the central grid
            GridPane centralGrid = new GridPane();
            centralGrid.setPadding(new Insets(10));
            centralGrid.setHgap(10);
            centralGrid.setVgap(10);

            // Top Half Cells (1-4)
            for (int i = 0; i < 4; i++) {
                VBox cell = createCell("Cell " + (i + 1));
                centralGrid.add(cell, i, 0);
            }

            // Bottom Half Cells (5-8)
            for (int i = 4; i < 8; i++) {
                Button button = new Button("Manage " + getEntityName(i + 1));
                button.setPrefSize(150, 50);
                // Placeholder for button actions
                button.setOnAction(e -> {
                    System.out.println(button.getText() + " button clicked.");
                    // TODO: Implement navigation to management pages
                });
                VBox cell = new VBox(button);
                cell.setAlignment(Pos.CENTER);
                centralGrid.add(cell, i - 4, 1);
            }

            root.setCenter(centralGrid);

            // Create the scene
            Scene scene = new Scene(root, 800, 600);

            // Apply styles directly (since we're not using CSS)
            scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Arial");

            // Set up the stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("MediaLab Assistant");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to create a cell with a label
    private VBox createCell(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 16px;");
        VBox cell = new VBox(label);
        cell.setAlignment(Pos.CENTER);
        cell.setPrefSize(150, 100);
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        return cell;
    }

    // Helper method to get entity name based on cell number
    private String getEntityName(int cellNumber) {
        switch(cellNumber) {
            case 5:
                return "Task Management";
            case 6:
                return "Category Management";
            case 7:
                return "Priority Management";
            case 8:
                return "Reminder Management";
            default:
                return "Unknown";
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
