package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.classes.FormatPath;
import sample.classes.JDBC;
import sample.classes.SwitchScene;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the Login FXML file
 * @author Brandt Davis
 * @version 1.0
 */
public class LoginController {
    /**
     * Pointer to the FXML login label
     */
    @FXML private Label loginLabel;
    /**
     * Pointer to the FXML username label
     */
    @FXML private Label usernameLabel;
    /**
     * Pointer to the FXML password Label
     */
    @FXML private Label passwordLabel;
    /**
     * Pointer to the FXML username text field
     */
    @FXML private TextField username;
    /**
     * Pointer to the FXML password field
     */
    @FXML private PasswordField password;
    /**
     * Pointer to the FXML zone label
     */
    @FXML private Label zone;
    /**
     * Pointer to the FXML login button
     */
    @FXML private Button loginButton;
    /**
     * Locale of user
     */
    private final Locale locale = Locale.getDefault();

    /**
     * Attempts to log a user in and displays if there is an upcoming appointment within 15 minutes
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void login(ActionEvent event) throws Exception {
        Connection conn = JDBC.getConnection();
        JDBC.makePreparedStatement("SELECT User_ID FROM users WHERE User_Name = '" + username.getText() + "' AND Password = '" + password.getText() + "'", conn);
        ResultSet result = Objects.requireNonNull(JDBC.getPreparedStatement()).executeQuery();
        boolean success = false;
        if (!result.next()) {
            ResourceBundle rb = ResourceBundle.getBundle("sample.resourcebundles.Errors", locale);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText(rb.getString("invalidCredentials"));
            errorAlert.setContentText(rb.getString("invalidCredentialsContent"));
            errorAlert.showAndWait();
        } else {
            JDBC.setLoggedInUsername(username.getText());
            JDBC.makePreparedStatement("SELECT Appointment_ID, Start FROM appointments WHERE Contact_ID = ( SELECT Contact_ID FROM contacts WHERE User_ID = '" + result.getString("User_ID") + "')", conn);
            result = JDBC.getPreparedStatement().executeQuery();
            ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());
            boolean upcomingAppointment = false;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
            ZonedDateTime start;
            ZoneId zone = ZoneId.systemDefault();
            while (result.next()) {
                String[] sArr = result.getString("Start").split(" ");
                try {
                    start = LocalDateTime.parse(sArr[0] + "T" + sArr[1]).atZone(ZoneId.of("Etc/UTC")).toInstant().atZone(zone);
                } catch (Exception e) {
                    start = LocalDateTime.parse(sArr[0] + "T" + sArr[1]).atZone(ZoneId.of("Etc/UTC")).toInstant().atZone(ZoneId.of(zone.toString(), ZoneId.SHORT_IDS));
                }
                String startValue = start.format(dtf);
                sArr = startValue.split(" ");
                if (start.compareTo(now) > 0 && now.plusMinutes(15).compareTo(start) >= 0) {
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                    infoAlert.setHeaderText("Upcoming Appointment");
                    infoAlert.setContentText("You have an upcoming appointment \nAppointment ID: " + sArr[0] + "\nDate: " + sArr[0] + "\nTime: " + sArr[1]);
                    infoAlert.show();
                    upcomingAppointment = true;
                }
            }
            if (!upcomingAppointment) {
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setHeaderText("No Upcoming Appointment");
                infoAlert.setContentText("You have no upcoming appointments.");
                infoAlert.show();
            }
            success = true;
        }

        String text = "Username: " + username.getText() + "\nTime: " + Instant.now() + "\nSuccess: " + success +"\n\n";
        File file = new File("login_activity.txt");
        Path fileName = file.toPath();
        Files.writeString(fileName, text, StandardOpenOption.APPEND);
        if (success) {
            SwitchScene.switchScene(event, FormatPath.format().run("Main"));
        }
    }

    /**
     * Sets the labels to english or french upon initialization
     * @throws Exception Exception in encountered
     */
    @FXML
    public void initialize() throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle("sample.resourcebundles.Labels", locale);
        loginLabel.setText(rb.getString("login"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        zone.setText(rb.getString("zoneid") + ": " + ZoneId.systemDefault());
        loginButton.setText(rb.getString("login"));
    }
}
