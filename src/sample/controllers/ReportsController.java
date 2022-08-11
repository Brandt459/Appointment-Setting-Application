package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
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

/**
 * Controller for the Reports FXML file
 * @author Brandt Davis
 * @version 1.0
 */
public class ReportsController {
    /**
     * Pointer to the FXML total by type and month label
     */
    @FXML private Label totalTypeMonth;
    /**
     * Pointer to the FXML type text field
     */
    @FXML private TextField type;
    /**
     * Pointer to the FXML month text field
     */
    @FXML private TextField month;
    /**
     * Pointer to the FXML contact combo box
     */
    @FXML private ComboBox<String> contact;
    /**
     * Pointer to the FXML appointment table view
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
     * Pointer to the FXML type table column
     */
    @FXML private TableColumn typeColumn;
    /**
     * Pointer to the FXML description table column
     */
    @FXML private TableColumn descriptionColumn;
    /**
     * Pointer to the FXML start date table column
     */
    @FXML private TableColumn startDateColumn;
    /**
     * Pointer to the FXML start time table column
     */
    @FXML private TableColumn startTimeColumn;
    /**
     * Pointer to the FXML end date table column
     */
    @FXML private TableColumn endDateColumn;
    /**
     * Pointer to theFXML end time table column
     */
    @FXML private TableColumn endTimeColumn;
    /**
     * Pointer to the FXML customer id table column
     */
    @FXML private TableColumn customerIdColumn;
    /**
     * Pointer to the FXML total by date label
     */
    @FXML private Label totalDate;
    /**
     * Pointer to the FXML start date picker
     */
    @FXML private DatePicker startDatePicker;
    /**
     * Pointer to the FXML end date picker
     */
    @FXML private DatePicker endDatePicker;

    /**
     * Sets the contact combo box upon initialization
     * @throws Exception Exception if encountered
     */
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

    /**
     * Sets the total amount of appointments by type and month
     * @param event KeyEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void setTotalTypeMonth(KeyEvent event) throws Exception {
        if (!(type.getText().equals("") || month.getText().equals(""))) {
            Connection conn = JDBC.getConnection();
            JDBC.makePreparedStatement("SELECT * FROM appointments WHERE Type = '" + type.getText() + "'", conn);
            ResultSet result = JDBC.getPreparedStatement().executeQuery();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
            int t = 0;
            while (result.next()) {
                String[] startStringArray = result.getString("Start").split(" ");

                ZonedDateTime start = LocalDateTime.parse(startStringArray[0] + "T" + startStringArray[1]).atZone(ZoneId.of("Etc/UTC")).toInstant().atZone(ZoneId.systemDefault());
                String startValue = start.format(dtf);
                startStringArray = startValue.split(" ");

                LocalDate startDate = LocalDate.parse(startStringArray[0]);

                if (String.valueOf(startDate.getMonthValue()).equals(month.getText())) {
                    t++;
                }
            }
            totalTypeMonth.setText(String.valueOf(t));
        } else {
            totalTypeMonth.setText("");
        }
    }

    /**
     * Sets the total amount of appointments between 2 dates
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void setTotalDate(ActionEvent event) throws Exception {
        if (!(startDatePicker.getValue() == null || endDatePicker.getValue() == null)) {
            Connection conn = JDBC.getConnection();
            JDBC.makePreparedStatement("SELECT * FROM appointments", conn);
            ResultSet result = JDBC.getPreparedStatement().executeQuery();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
            int t = 0;
            while (result.next()) {
                String[] sArr = result.getString("Start").split(" ");
                String[] eArr = result.getString("End").split(" ");

                ZonedDateTime startTime = LocalDateTime.parse(sArr[0] + "T" + sArr[1]).atZone(ZoneId.of("Etc/UTC")).toInstant().atZone(ZoneId.systemDefault());
                String startValue = startTime.format(dtf);
                String[] startArray = startValue.split(" ");
                LocalDate startDate = LocalDate.parse(startArray[0]);
                ZonedDateTime endTime = LocalDateTime.parse(eArr[0] + "T" + eArr[1]).atZone(ZoneId.of("Etc/UTC")).toInstant().atZone(ZoneId.systemDefault());
                String endValue = endTime.format(dtf);
                String[] endArray = endValue.split(" ");
                LocalDate endDate = LocalDate.parse(endArray[0]);

                if (startDatePicker.getValue().compareTo(startDate) <= 0 && endDatePicker.getValue().compareTo(startDate) >= 0) {
                    t++;
                } else if (startDatePicker.getValue().compareTo(endDate) <= 0 && endDatePicker.getValue().compareTo(endDate) >= 0 ) {
                    t++;
                }
            }
            totalDate.setText(String.valueOf(t));
        } else {
            totalDate.setText("");
        }
    }

    /**
     * Sets the schedule for a contact upon selecting a contact
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void setTable(ActionEvent event) throws Exception {
        Connection conn = JDBC.getConnection();
        String contactId = "";
        JDBC.makePreparedStatement("SELECT Contact_ID FROM contacts WHERE Contact_Name = '" + contact.getValue() + "'", conn);
        ResultSet result = JDBC.getPreparedStatement().executeQuery();
        while (result.next()) {
            contactId = result.getString("Contact_ID");
        }

        JDBC.makePreparedStatement("SELECT * FROM appointments WHERE Contact_ID = '" + contactId + "'", conn);
        result = JDBC.getPreparedStatement().executeQuery();
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
            Appointment appointment = new Appointment(id, title, description, location, Integer.parseInt(contactId), type, startDate, startTime, endDate, endTime, customerId, userId);
            appointments.add(appointment);
        }
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentTable.setItems(appointments);
    }

    /**
     * Switches to the Main scene
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void switchToMain(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, FormatPath.format().run("Main"));
    }
}
