package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import sample.classes.SwitchScene;

public class MainController {
    @FXML
    public void switchToCustomerRecords(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, "../fxml/CustomerRecords.fxml");
    }

    @FXML
    public void switchToAppointmentRecords(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, "../fxml/AppointmentRecords.fxml");
    }

    @FXML
    public void switchToReports(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, "../fxml/Reports.fxml");
    }
}
