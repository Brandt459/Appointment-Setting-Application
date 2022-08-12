package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.classes.JDBC;

import java.util.Objects;

/**
 * Main class to run program
 * @author Brandt Davis
 * @version 1.0
 */
public class Main extends Application {
    /**
     * Sets the scene to the login scene
     * @param primaryStage Stage object
     * @throws Exception Exception if encountered
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/Login.fxml")));
        primaryStage.setTitle("Scheduling Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Main method makes a connection to the MySQL database and calls the launch() function to start the JavaFX program
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        JDBC.makeConnection();
        launch(args);
    }
}
