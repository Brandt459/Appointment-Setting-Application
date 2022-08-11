package sample.classes;

import sample.interfaces.DateTimeFunction;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Class for converting to UTC time
 * @author Brandt Davis
 * @version 1.0
 */
public class UTC {
    /**
     * Returns a lambda function to convert local date and time to UTC date and time
     * @return lambda function
     */
    public static DateTimeFunction getUTC() {
        DateTimeFunction utcTime = (date, time) -> LocalDateTime.parse(date + "T" + time).atZone(ZoneId.of("Etc/UTC"));
        return utcTime;
    }
}
