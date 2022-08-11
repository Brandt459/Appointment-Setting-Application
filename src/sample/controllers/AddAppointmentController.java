package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.classes.Appointment;
import sample.classes.JDBC;
import sample.classes.SwitchScene;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AddAppointmentController {
    @FXML private Label addOrUpdateAppointment;
    @FXML private TextField id;
    @FXML private TextField title;
    @FXML private TextField description;
    @FXML private TextField locationValue;
    @FXML private ComboBox<String> contact;
    @FXML private TextField type;
    @FXML private DatePicker startDate;
    @FXML private TextField startTime;
    @FXML private DatePicker endDate;
    @FXML private TextField endTime;
    @FXML private TextField customerId;
    @FXML private TextField userId;
    private String username = JDBC.getLoggedInUsername();

    public void setScene(String addOrUpdateAppointmentText, Appointment appointment) {
        addOrUpdateAppointment.setText(addOrUpdateAppointmentText);
        id.setText(String.valueOf(appointment.getId()));
        title.setText(appointment.getTitle());
        description.setText(appointment.getDescription());
        locationValue.setText(appointment.getLocation());
        contact.setValue(appointment.getContactName());
        type.setText(appointment.getType());
        startDate.setValue(appointment.getStartDate());
        startTime.setText(appointment.getStartTime());
        endDate.setValue(appointment.getEndDate());
        endTime.setText(appointment.getEndTime());
        customerId.setText(String.valueOf(appointment.getCustomerId()));
        userId.setText(String.valueOf(appointment.getUserId()));
    }

    @FXML
    public void initialize() throws Exception {
        Connection conn = JDBC.getConnection();
        JDBC.makePreparedStatement("SELECT Contact_Name FROM contacts", conn);
        ResultSet result = JDBC.getPreparedStatement().executeQuery();
        ObservableList<String> contacts = FXCollections.observableArrayList();
        while (result.next()) {
            contacts.add(result.getString("Contact_Name"));
        }
        contact.setItems(contacts);
    }

    @FXML
    public void addAppointment(ActionEvent event) throws Exception {
        Timestamp instant = Timestamp.from(Instant.now());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

        ZonedDateTime start = LocalDateTime.parse(startDate.getValue() + "T" + startTime.getText()).atZone(ZoneId.systemDefault()).toInstant().atZone(ZoneId.of("Etc/UTC"));
        ZonedDateTime startEst = start.toInstant().atZone(ZoneId.of("EST5EDT"));
        if (startEst.getHour() < 8 || startEst.getHour() > 22) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Cannot Create Appointment");
            errorAlert.setContentText("Start time must be between 8:00 am and 10:00 pm EST.");
            errorAlert.show();
            return;
        }
        String startValue = start.format(dtf);

        ZonedDateTime end = LocalDateTime.parse(endDate.getValue() + "T" + endTime.getText()).atZone(ZoneId.systemDefault()).toInstant().atZone(ZoneId.of("Etc/UTC"));
        ZonedDateTime endEst = end.toInstant().atZone(ZoneId.of("EST5EDT"));
        if (endEst.getHour() < 8 || endEst.getHour() > 22) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Cannot Create Appointment");
            errorAlert.setContentText("End time must be between 8:00 am and 10:00 pm EST.");
            errorAlert.show();
            return;
        }
        String endValue = end.format(dtf);

        if (end.compareTo(start) <= 0) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Cannot Create Appointment");
            errorAlert.setContentText("End time must be after start time.");
            errorAlert.show();
            return;
        }

        String contactId = "0";
        Connection conn = JDBC.getConnection();
        JDBC.makePreparedStatement("SELECT Contact_ID FROM contacts WHERE Contact_Name = '" + contact.getSelectionModel().getSelectedItem() + "'", conn);
        ResultSet result = JDBC.getPreparedStatement().executeQuery();
        while (result.next()) {
            contactId = result.getString("Contact_ID");
        }

        JDBC.makePreparedStatement("SELECT Appointment_ID, Start, End FROM appointments WHERE Customer_ID = '" + customerId.getText() + "'", conn);
        result = JDBC.getPreparedStatement().executeQuery();
        while (result.next()) {
            if (!(id.getText().equals(result.getString("Appointment_ID")))) {
                String s = result.getString("Start");
                String e = result.getString("End");

                String[] sArr = s.split(" ");
                String[] eArr = e.split(" ");

                ZonedDateTime startTime = LocalDateTime.parse(sArr[0] + "T" + sArr[1]).atZone(ZoneId.of("Etc/UTC"));
                ZonedDateTime endTime = LocalDateTime.parse(eArr[0] + "T" + eArr[1]).atZone(ZoneId.of("Etc/UTC"));

                if (start.compareTo(endTime) < 0 && startTime.compareTo(end) < 0) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Cannot Create Appointment");
                    errorAlert.setContentText("There is an appointment already scheduled to start or end between the times submitted. \nStart: " + startTime.toInstant().atZone(ZoneId.systemDefault()).format(dtf) + "\nEnd: " + endTime.toInstant().atZone(ZoneId.systemDefault()).format(dtf));
                    errorAlert.show();
                    return;
                }
            }
        }
        JDBC.makePreparedStatement("SELECT Appointment_ID, Start, End FROM appointments WHERE Contact_ID = '" + contactId + "'", conn);
        result = JDBC.getPreparedStatement().executeQuery();
        while (result.next()) {
            if (!(id.getText().equals(result.getString("Appointment_ID")))) {
                String s = result.getString("Start");
                String e = result.getString("End");

                String[] sArr = s.split(" ");
                String[] eArr = e.split(" ");

                ZonedDateTime startTime = LocalDateTime.parse(sArr[0] + "T" + sArr[1]).atZone(ZoneId.of("Etc/UTC"));
                ZonedDateTime endTime = LocalDateTime.parse(eArr[0] + "T" + eArr[1]).atZone(ZoneId.of("Etc/UTC"));

                if (start.compareTo(endTime) < 0 && startTime.compareTo(end) < 0) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Cannot Create Appointment");
                    errorAlert.setContentText("There is an appointment already scheduled to start or end between the times submitted. \nStart: " + startTime.toInstant().atZone(ZoneId.systemDefault()).format(dtf) + "\nEnd: " + endTime.toInstant().atZone(ZoneId.systemDefault()).format(dtf));
                    errorAlert.show();
                    return;
                }
            }
        }

        if (addOrUpdateAppointment.getText().equals("Add Appointment")) {
            int idValue = UUID.randomUUID().hashCode();
            JDBC.makePreparedStatement("INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                    "VALUES ('" + idValue + "', '" + title.getText() + "', '" + description.getText() + "', '" + locationValue.getText() + "', '" + type.getText() + "', '" +
                    startValue + "', '" + endValue + "', '" + instant + "', '" + username + "', '" + instant + "', '" + username + "', '" + customerId.getText() + "', '" + userId.getText() + "', '" + contactId + "')", conn);
        } else {
            JDBC.makePreparedStatement("UPDATE appointments SET Title = '" + title.getText() + "', Description = '" + description.getText() + "', Location = '" +
                    locationValue.getText() + "', Type = '" + type.getText() + "', Start = '" + startValue + "', End = '" + endValue + "', Last_Update = '" + instant + "', Last_Updated_By = '" +
                    username + "', Customer_ID = '" + customerId.getText() + "', User_ID = '" + userId.getText() + "', Contact_ID = '" + contactId + "' WHERE Appointment_ID = '" + id.getText() + "'", conn);
        }
        JDBC.getPreparedStatement().execute();
        switchToMain(event);
    }

    @FXML
    public void switchToMain(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, "../fxml/Main.fxml");
    }
}
