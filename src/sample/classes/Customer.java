package sample.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Objects;

/**
 * Class to hold customer details
 * @author Brandt Davis
 * @version 1.0
 */
public class Customer {
    /**
     * ID of the customer
     */
    private final int id;
    /**
     * Name of the customer
     */
    private final String name;
    /**
     * Address of the customer
     */
    private final String address;
    /**
     * State/Province/Division the customer is in
     */
    private String state;
    /**
     * Country the customer is in
     */
    private String country;
    /**
     * Postal code of the customer
     */
    private final String postalCode;
    /**
     * Phone number of the customer
     */
    private final String phoneNumber;

    /**
     * Customer constructor assigning all the private variables
     * @param id ID of the customer
     * @param name Name of the customer
     * @param address Address of the customer
     * @param divisionId Division ID for the state/province/division the customer is in
     * @param postalCode Postal code of the customer
     * @param phoneNumber Phone number of the customer
     * @throws Exception Exception if encountered
     */
    public Customer(int id, String name, String address, int divisionId, String postalCode, String phoneNumber) throws Exception {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        Connection conn = JDBC.getConnection();
        JDBC.makePreparedStatement("SELECT Division, Country_ID FROM first_level_divisions WHERE Division_ID = '" + divisionId + "'", conn);
        ResultSet result = Objects.requireNonNull(JDBC.getPreparedStatement()).executeQuery();
        String countryId = "";
        while (result.next()) {
            this.state = result.getString("Division");
            countryId = result.getString("Country_ID");
        }
        JDBC.makePreparedStatement("SELECT Country FROM countries WHERE Country_ID = '" + countryId + "'", conn);
        result = JDBC.getPreparedStatement().executeQuery();
        while (result.next()) {
            this.country = result.getString("Country");
        }
    }

    /**
     * Returns the customer ID
     * @return The customer ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the customer Name
     * @return The customer Name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the customer's address
     * @return The customer's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the state/province/division the customer is in
     * @return The state/province/division the customer is in
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the country the customer is in
     * @return The country the customer is in
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the postal code of the customer
     * @return The postal code of the customer
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Returns the customer's phone number
     * @return The customer's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
