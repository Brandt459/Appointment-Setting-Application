package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.classes.Customer;
import sample.classes.FormatPath;
import sample.classes.JDBC;
import sample.classes.SwitchScene;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Controller for the AddCustomer FXML file
 * @author Brandt Davis
 * @version 1.0
 */
public class AddCustomerController {
    /**
     * Pointer to the FXML label holding "Add Customer" or "Update Customer" text
     */
    @FXML private Label addOrUpdateCustomer;
    /**
     * Pointer to the FXML ID text field
     */
    @FXML private TextField id;
    /**
     * Pointer to the FXML name text field
     */
    @FXML private TextField name;
    /**
     * Pointer to the FXML address text field
     */
    @FXML private TextField address;
    /**
     * Pointer to the postal code text field
     */
    @FXML private TextField postalCode;
    /**
     * Pointer to the phone number text field
     */
    @FXML private TextField phoneNumber;
    /**
     * Pointer to the country combo box
     */
    @FXML private ComboBox<String> country;
    /**
     * Pointer to the state combo box
     */
    @FXML private ComboBox<String> state;
    /**
     * username of the logged in user
     */
    private final String username = JDBC.getLoggedInUsername();

    /**
     * Sets the values for all customer data when updating a customer
     * @param addOrUpdateCustomerText Update Customer text
     * @param customer Customer object
     */
    public void setScene(String addOrUpdateCustomerText, Customer customer) {
        addOrUpdateCustomer.setText(addOrUpdateCustomerText);
        id.setText(String.valueOf(customer.getId()));
        name.setText(customer.getName());
        address.setText(customer.getAddress());
        postalCode.setText(customer.getPostalCode());
        phoneNumber.setText(customer.getPhoneNumber());
        country.setValue(customer.getCountry());
        state.setValue(customer.getState());
    }

    /**
     * Sets the state combo box values upon initialization
     * @throws Exception Exception if encountered
     */
    @FXML
    public void initialize() throws Exception {
        setStates(new ActionEvent());
    }

    /**
     * Adds or updates a customer
     * @param event ActionEvent object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void addCustomer(ActionEvent event) throws Exception {
        Timestamp instant = Timestamp.from(Instant.now());
        Connection conn = JDBC.getConnection();
        String division = "";
        JDBC.makePreparedStatement("SELECT Division_ID FROM first_level_divisions WHERE Division = '" + state.getSelectionModel().getSelectedItem() + "'", conn);
        ResultSet result = Objects.requireNonNull(JDBC.getPreparedStatement()).executeQuery();
        while (result.next()) {
            division = result.getString("Division_ID");
        }
        if (addOrUpdateCustomer.getText().equals("Add Customer")) {
            int idValue = UUID.randomUUID().hashCode();
            JDBC.makePreparedStatement("INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                    "VALUES ('" + idValue + "', '" + name.getText() + "', '" + address.getText() + "', '" + postalCode.getText() + "', '" + phoneNumber.getText() + "', '" +
                    instant + "', '" + username + "', '" + instant + "', '" + username + "', '" + division + "')", conn);
        } else {
            JDBC.makePreparedStatement("UPDATE customers SET Customer_Name = '" + name.getText() + "', Address = '" + address.getText() + "', Postal_Code = '" + postalCode.getText() +
                    "', Phone = '" + phoneNumber.getText() + "', Last_Update = '" + instant + "', Last_Updated_By = '" + username + "', Division_ID = '" + division + "' WHERE Customer_ID = '" + id.getText() + "'", conn);
        }
        JDBC.getPreparedStatement().execute();
        switchToMain(event);
    }

    /**
     * Sets the states combo box values by the selected country
     * @param event ActionEvent Object
     * @throws Exception Exception if encountered
     */
    @FXML
    public void setStates(ActionEvent event) throws Exception {
        Connection conn = JDBC.getConnection();
        JDBC.makePreparedStatement("SELECT Country_ID, Division FROM `FIRST_LEVEL_DIVISIONS`", conn);
        ResultSet result = Objects.requireNonNull(JDBC.getPreparedStatement()).executeQuery();
        ObservableList<String> states = FXCollections.observableArrayList();
        int countryId = 1;
        if (country.getSelectionModel().getSelectedItem() != null) {
            if (country.getSelectionModel().getSelectedItem().equals("United Kingdom")) {
                countryId = 2;
            } else if (country.getSelectionModel().getSelectedItem().equals("Canada")) {
                countryId = 3;
            }
        }
        while (result.next()) {
            if (result.getString("Country_ID").equals(String.valueOf(countryId))) {
                states.add(result.getString("Division"));
            }
        }
        state.setItems(states);
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
}
