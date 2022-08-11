package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.classes.Appointment;
import sample.classes.FormatPath;
import sample.classes.JDBC;
import sample.classes.SwitchScene;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;

/**
 * Controller fo the Appointmenr Records FXML file
 * @author Brandt Davis
 * @version 1.0
 */
public class AppointmentRecordsController {
    /**
     * Pointer to the FXML appointment table table view
     */
    @FXML private TableView<Appointment> appointmentTable;
    /**
     * Pointer to the FXML id table column
     */
    @FXML private TableColumn idColumn;
    /**
     * Pointer to the FXML title table column
     */
    @FXML private TableColumn titleColumn;
    /**
     * Pointer to the FXML description table column
     */
    @FXML private TableColumn descriptionColumn;
    /**
     * Pointer to the FXML location table column
     */
    @FXML private TableColumn locationColumn;
    /**
     * Pointer to the FXML contact table column
     */
    @FXML private TableColumn contactColumn;
    /**
     * Pointer to the FXML type table column
     */
    @FXML private TableColumn typeColumn;
    /**
     * Pointer to the FXML start date table column
     */
    @FXML private TableColumn startDateColumn;
    /**
     * Pointer to the FXML star time table column
     */
    @FXML private TableColumn startTimeColumn;
    /**
     * Pointer to the FXML end date table column
     */
    @FXML private TableColumn endDateColumn;
    /**
     * Pointer to the FXML end time table column
     */
    @FXML private TableColumn endTimeColumn;
    /**
     * Pointer to the FXML customer id table column
     */
    @FXML private TableColumn customerIdColumn;
    /**
     * Pointer to the FXML user id table column
     */
    @FXML private TableColumn userIdColumn;
    /**
     * Pointer to the FXML filter by month radio button
     */
    @FXML private RadioButton filterMonth;
    /**
     * Pointer to the FXML filter by week radio button
     */
    @FXML private RadioButton filterWeek;
    /**
     * Pointer to the FXML filter by month text field
     */
    @FXML private TextField filterMonthText;
    /**
     * Pointer to the FXML filter by week text field
     */
    @FXML private TextField filterWeekText;
    /**
     * List that will hold all appointment objects
     */
    private ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

    /**
     * Sets the appointment table upon initialization
     * @throws Exception Exception if encountered
     */
    @FXML
    public void initialize() throws Exception {
        setAppointmentTable();
    }

    /**
     * Sets the appointment table
     * @throws Exception Exception if encountered
     */
    public void setAppointmentTable() throws Exception {
        Connection conn = JDBC.getConnection();
        JDBC.makePreparedStatement("SELECT * FROM appointments", conn);
        ResultSet result = JDBC.getPreparedStatement().executeQuery();
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        while (result.next()) {
            int id = Integer.parseInt(result.getString("Appointment_ID"));
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            String[] startStringArray = result.getString("Start").split(" ");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

            ZonedDateTime start = LocalDateTime.parse(startStringArray[0] + "T" + startStringArray[1]).atZone(ZoneId.of("Etc/UTC")).toInstant().atZone(ZoneId.systemDefault());
            String startValue = start.format(dtf);
            startStringArray = startValue.split(" ");

            String [] endStringArray = result.getString("end").split(" ");

            ZonedDateTime end = LocalDateTime.parse(endStringArray[0] + "T" + endStringArray[1]).atZone(ZoneId.of("Etc/UTC")).toInstant().atZone(ZoneId.systemDefault());
            String endValue = end.format(dtf);
            endStringArray = endValue.split(" ");

            LocalDate startDate = LocalDate.parse(startStringArray[0]);
            String startTime = startStringArray[1];
            LocalDate endDate = LocalDate.parse(endStringArray[0]);
            String endTime = endStringArray[1];
            int customerId = Integer.parseInt(result.getString("Customer_ID"));
            int userId = Integer.parseInt(result.getString("User_ID"));
            int contactId = Integer.parseInt(result.getString("Contact_ID"));
            Appointment appointment = new Appointment(id, title, description, location, contactId, type, startDate, startTime, endDate, endTime, customerId, userId);
            appointments.add(appointment);
            appointmentsList.addAll(appointment);
        }
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        appointmentTable.setItems(appointments);
    }

    /**
     * Sets the appointment table when No Filter is selected
     * @param event ActionEvent Object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void setAppointmentTable(ActionEvent event) throws Exception {
        setAppointmentTable();
    }

    /**
     * Filters the appointment table by month or week based on which is selected
     */
    public void filter() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        if (filterMonth.isSelected()) {
            for (Appointment appointment : appointmentsList) {
                if (String.valueOf(appointment.getStartDate().getMonthValue()).equals(filterMonthText.getText())) {
                    appointments.addAll(appointment);
                }
            }
            appointmentTable.setItems(appointments);
        } else if (filterWeek.isSelected()) {
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            for (Appointment appointment : appointmentsList) {
                int weekNumber = appointment.getStartDate().get(weekFields.weekOfWeekBasedYear());
                if (String.valueOf(weekNumber).equals(filterWeekText.getText())) {
                    appointments.addAll(appointment);
                }
            }
            appointmentTable.setItems(appointments);
        }
    }

    /**
     * Calls the filter() method when a character is entered into one of the filter text views
     * @param event KeyEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void filter(KeyEvent event) throws Exception {
        filter();
    }

    /**
     * Calls the filter() method when one of the filter radio buttons is selected
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    private void filterRadio(ActionEvent event) throws Exception {
        filter();
    }

    /**
     * Switches to the main scene
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void switchToMain(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, FormatPath.format().run("Main"));
    }

    /**
     * Switches to the Add Appointment scene
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void switchToAddAppointment(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, FormatPath.format().run("AddAppointment"));
    }

    /**
     * Switches to the Add Appointment scene and passes the Appointment object
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void switchToUpdateAppointment(ActionEvent event) throws Exception {
        if (appointmentTable.getSelectionModel().getSelectedItem() == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("No appointment selected");
            errorAlert.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FormatPath.format().run("AddAppointment")));
            Parent root = loader.load();
            AddAppointmentController controller = loader.getController();
            controller.setScene("Update Appointment", appointmentTable.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Deletes an appointment
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void deleteAppointment(ActionEvent event) throws Exception {
        if (appointmentTable.getSelectionModel().getSelectedItem() == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("No appointment selected");
            errorAlert.show();
        } else {
            Alert confirmationAlert = new Alert(Alert.AlertType.ERROR);
            confirmationAlert.setHeaderText("Confirmation");
            confirmationAlert.setContentText("Are you sure you would like to delete this appointment?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
                    Connection conn = JDBC.getConnection();

                    JDBC.makePreparedStatement("DELETE FROM appointments WHERE Appointment_ID = '" + appointment.getId() + "'", conn);
                    JDBC.getPreparedStatement().execute();

                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setHeaderText("Info");
                    informationAlert.setContentText("Successfully deleted Appointment \nAppointment ID: " + appointment.getId() + ", Appointment Type: " + appointment.getType());
                    informationAlert.show();
                    setAppointmentTable();
                }
            }
        }
    }
}
