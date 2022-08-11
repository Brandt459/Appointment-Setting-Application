package sample.classes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class for switching scenes
 * @author Brandt Davis
 * @version 1.0
 */
public class SwitchScene {
    /**
     * Switches the scene to the fxml file provided
     * @param event ActionEvent object
     * @param fxmlFile Path of the fxml file to load
     * @throws Exception Exception if encountered
     */
    public static void switchScene(ActionEvent event, String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(SwitchScene.class.getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
