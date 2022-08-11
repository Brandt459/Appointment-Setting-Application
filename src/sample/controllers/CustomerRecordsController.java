package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.classes.Customer;
import sample.classes.JDBC;
import sample.classes.SwitchScene;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;

public class CustomerRecordsController {
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn idColumn;
    @FXML private TableColumn nameColumn;
    @FXML private TableColumn addressColumn;
    @FXML private TableColumn stateColumn;
    @FXML private TableColumn countryColumn;
    @FXML private TableColumn postalColumn;
    @FXML private TableColumn phoneColumn;

    @FXML
    public void initialize() throws Exception {
        setCustomerTables();
    }

    public void setCustomerTables() throws Exception {
        Connection conn = JDBC.getConnection();
        JDBC.makePreparedStatement("SELECT * FROM customers", conn);
        ResultSet result = JDBC.getPreparedStatement().executeQuery();
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        while (result.next()) {
            int id = Integer.parseInt(result.getString("Customer_ID"));
            String name = result.getString("Customer_Name");
            String address = result.getString("Address");
            int divisionId = Integer.parseInt(result.getString("Division_ID"));
            String postalCode = result.getString("Postal_Code");
            String phone = result.getString("Phone");
            Customer customer = new Customer(id, name, address, divisionId, postalCode, phone);
            customers.add(customer);
        }
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerTable.setItems(customers);
    }

    @FXML
    public void switchToMain(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, "../fxml/Main.fxml");
    }

    @FXML
    public void switchToAddCustomer(ActionEvent event) throws Exception {
        SwitchScene.switchScene(event, "../fxml/AddCustomer.fxml");
    }

    @FXML
    public void switchToUpdateCustomer(ActionEvent event) throws Exception {
        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("No customer selected");
            errorAlert.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/AddCustomer.fxml"));
            Parent root = loader.load();
            AddCustomerController controller = loader.getController();
            controller.setScene("Update Customer", customerTable.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    public void deleteCustomer(ActionEvent event) throws Exception {
        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("No customer selected");
            errorAlert.show();
        } else {
            Alert confirmationAlert = new Alert(Alert.AlertType.ERROR);
            confirmationAlert.setHeaderText("Confirmation");
            confirmationAlert.setContentText("Are you sure you would like to delete this customer?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    Customer customer = customerTable.getSelectionModel().getSelectedItem();
                    Connection conn = JDBC.getConnection();

                    JDBC.makePreparedStatement("DELETE FROM appointments WHERE Customer_ID = '" + customer.getId() + "'", conn);
                    JDBC.getPreparedStatement().execute();

                    JDBC.makePreparedStatement("DELETE FROM customers WHERE Customer_ID = '" + customer.getId() + "'", conn);
                    JDBC.getPreparedStatement().execute();

                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setHeaderText("Info");
                    informationAlert.setContentText("Customer was successfully deleted");
                    informationAlert.show();
                    setCustomerTables();
                }
            }
        }
    }
}
