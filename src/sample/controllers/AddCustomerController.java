package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.classes.Customer;
import sample.classes.JDBC;
import sample.classes.SwitchScene;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class AddCustomerController {
    @FXML private Label addOrUpdateCustomer;
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private TextField postalCode;
    @FXML private TextField phoneNumber;
    @FXML private ComboBox country;
    @FXML private ComboBox state;
    private String username = JDBC.getLoggedInUsername();

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

    @FXML
    public void initialize() throws Exception {
        setStates(new ActionEvent());
    }

    @FXML
    public void addCustomer(ActionEvent event) throws Exception {
        Timestamp instant = Timestamp.from(Instant.now());
        Connection conn = JDBC.getConnection();
        String division = "";
        JDBC.makePreparedStatement("SELECT Division_ID FROM first_level_divisions WHERE Division = '" + state.getSelectionModel().getSelectedItem() + "'", conn);
        ResultSet result = JDBC.getPreparedStatement().executeQuery();
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

    @FXML
    public void setStates(ActionEvent event) throws Exception {
        Connection conn = JDBC.getConnection();
        JDBC.makePreparedStatement("SELECT Country_ID, Division FROM `FIRST_LEVEL_DIVISIONS`", conn);
        ResultSet result = JDBC.getPreparedStatement().executeQuery();
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

    @FXML
    public void switchToMain(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, "../fxml/Main.fxml");
    }
}
