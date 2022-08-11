package sample.classes;

import java.sql.Connection;
import java.sql.ResultSet;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String state;
    private String country;
    private String postalCode;
    private String phoneNumber;

    public Customer(int id, String name, String address, int divisionId, String postalCode, String phoneNumber) throws Exception {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        Connection conn = JDBC.getConnection();
        JDBC.makePreparedStatement("SELECT Division, Country_ID FROM first_level_divisions WHERE Division_ID = '" + divisionId + "'", conn);
        ResultSet result = JDBC.getPreparedStatement().executeQuery();
        String countryId = "";
        while (result.next()) {
            state = result.getString("Division");
            countryId = result.getString("Country_ID");
        }
        JDBC.makePreparedStatement("SELECT Country FROM countries WHERE Country_ID = '" + countryId + "'", conn);
        result = JDBC.getPreparedStatement().executeQuery();
        while (result.next()) {
            country = result.getString("Country");
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
