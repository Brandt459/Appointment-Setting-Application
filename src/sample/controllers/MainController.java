package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import sample.classes.FormatPath;
import sample.classes.SwitchScene;

/**
 * Controller for the Main FXML file
 * @author Brandt Davis
 * @version 1.0
 */
public class MainController {
    /**
     * Switches to the Customer Records FXML file
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void switchToCustomerRecords(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, FormatPath.format().run("CustomerRecords"));
    }

    /**
     * Switches to the Appointment Records FXML file
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void switchToAppointmentRecords(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, FormatPath.format().run("AppointmentRecords"));
    }

    /**
     * Switches to the Reports FXML file
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void switchToReports(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, FormatPath.format().run("Reports"));
    }
}
