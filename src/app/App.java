package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import controllers.MainController;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Print the current working directory
            System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
    
            // Existing initialization code...
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            // Initialize the main controller
            MainController mainController = new MainController();
    
            // Initialize the main view
            MainView mainView = new MainView(primaryStage);
    
            // Link the controller with the view
            mainController.setMainView(mainView);
    
            // Initialize data and update statistics
            mainController.initializeData();
            mainController.updateStatistics();
    
            // Implement periodic status updates based on deadlines
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> {
                mainController.updateTaskStatuses();
                mainController.updateStatistics();
            }, 0, 1, TimeUnit.HOURS); // Adjust the period as needed
    
            // Handle application closure to save data and shutdown scheduler
            primaryStage.setOnCloseRequest(e -> {
                mainController.saveData();
                scheduler.shutdown();
            });
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
