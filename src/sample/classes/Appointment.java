package sample.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String contactName;
    private String type;
    private LocalDate startDate;
    private String startTime;
    private LocalDate endDate;
    private String endTime;
    private int customerId;
    private int userId;

    public Appointment(int id, String title, String description, String location, int contactId, String type, LocalDate startDate, String startTime, LocalDate endDate, String endTime, int customerId, int userId) throws Exception {
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
        this.userId = userId;

        Connection conn = JDBC.getConnection();
        JDBC.makePreparedStatement("SELECT Contact_Name FROM contacts WHERE Contact_ID = '" + contactId + "'", conn);
        ResultSet result = JDBC.getPreparedStatement().executeQuery();
        while (result.next()) {
            this.contactName = result.getString("Contact_Name");
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getContactName() {
        return contactName;
    }

    public String getType() {
        return type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getUserId() {
        return userId;
    }

}
