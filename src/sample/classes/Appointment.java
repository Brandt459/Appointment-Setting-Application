package sample.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Class to hold appointment details
 * @author Brandt Davis
 * @version 1.0
 */
public class Appointment {
    /**
     * ID of Appointment
     */
    private final int id;
    /**
     * Title of Appointment
     */
    private final String title;
    /**
     * Description of Appointment
     */
    private final String description;
    /**
     * Location of Appointment
     */
    private final String location;
    /**
     * Name of the contact assigned to the Appointment
     */
    private String contactName;
    /**
     * Type of Appointment
     */
    private final String type;
    /**
     * Start date of Appointment
     */
    private final LocalDate startDate;
    /**
     * Start tIme of Appointment
     */
    private final String startTime;
    /**
     * End date of Appointment
     */
    private final LocalDate endDate;
    /**
     * End time of Appointment
     */
    private final String endTime;
    /**
     * Customer ID for Appointment
     */
    private final int customerId;

    /**
     * Appointment constructor assigning all class variables
     * @param id ID of Appointment
     * @param title Title of Appointment
     * @param description Description of Appointment
     * @param location Location of Appointment
     * @param contactId Contact ID of Appointment
     * @param type Type of Appointment
     * @param startDate Start date of Appointment
     * @param startTime Start time of Appointment
     * @param endDate End date of Appointment
     * @param endTime End time of Appointment
     * @param customerId Customer ID for Appointment
     * @throws Exception Exception if encountered
     */
    public Appointment(int id, String title, String description, String location, int contactId, String type, LocalDate startDate, String startTime, LocalDate endDate, String endTime, int customerId) throws Exception {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.customerId = customerId;

        Connection conn = JDBC.getConnection();
        JDBC.makePreparedStatement("SELECT Contact_Name FROM contacts WHERE Contact_ID = '" + contactId + "'", conn);
        ResultSet result = Objects.requireNonNull(JDBC.getPreparedStatement()).executeQuery();
        while (result.next()) {
            this.contactName = result.getString("Contact_Name");
        }
    }

    /**
        * Returns the id of the appointment
        * @return ID of the appointment
     */
    public int getId() {
        return id;
    }

    /**
        * Returns the title of the appointment
        * @return Title of the appointment
     */
    public String getTitle() {
        return title;
    }

    /**
        * Returns the description of the appointment
        * @return Description of the appointment
     */
    public String getDescription() {
        return description;
    }

    /**
        * Returns the location of the appointment
        * @return Location the appointment
     */
    public String getLocation() {
        return location;
    }

    /** * Returns the contact name for the appointment
        * @return Contact name for the appointment
     */
    public String getContactName() {
        return contactName;
    }

    /**
        * Returns the type of appointment
        * @return The type of appointment
     */
    public String getType() {
        return type;
    }

    /**
        * Returns the start date of the appointment
        * @return The start date of the appointment
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
        * Returns the start time of the appointment
        * @return The start time of the appointment
     */
    public String getStartTime() {
        return startTime;
    }
    /**
      * Returns the end date of the appointment
      * @return The end date of the appointment
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
        * Returns the end time of the appointment
        * @return The end time of the appointment
    */
    public String getEndTime() {
        return endTime;
    }

    /**
        * Returns the customer id of the appointment
        * @return The customer id of the appointment
     */
    public int getCustomerId() {
         return customerId;
    }
}
