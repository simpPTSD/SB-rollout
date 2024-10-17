package demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class ExpenseTrackerApp extends Application {

    private ExpenseTrackerController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Ensure the path to the FXML file is correct
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/demo/ExpenseTracker.fxml"));
        Parent root = loader.load();

        controller = loader.getController();
        VBox vbox = (VBox) root.lookup(".root");
        vbox.setPadding(new Insets(20, 20, 20, 20));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/demo/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // Save data when the application is closed
        controller.saveExpensesToFile();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
