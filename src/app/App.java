package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import controllers.MainController;

/**
 * The entry point of the JavaFX application.
 * <p>
 * This class initializes the main controller, sets up the main view, and
 * periodically updates task statuses. It also handles application closure
 * by saving data and shutting down the scheduler.
 */
public class App extends Application {

    /**
     * Called by the JavaFX runtime to start the application.
     * <p>
     * Initializes the MainController, links it with MainView, loads all
     * application data, and schedules periodic updates for task statuses
     * and statistics.
     *
     * @param primaryStage The primary stage provided by the JavaFX runtime.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Print the current working directory
            System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
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
            }, 0, 1, TimeUnit.HOURS);

            // Handle application closure to save data and shutdown scheduler
            primaryStage.setOnCloseRequest(e -> {
                mainController.saveData();
                scheduler.shutdown();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The main entry point for the application.
     * <p>
     * Invokes the JavaFX runtime to launch the app, which in turn calls
     * {@link #start(Stage)}.
     *
     * @param args The command-line arguments passed at launch time (unused).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
