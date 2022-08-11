package sample.classes;

import sample.interfaces.StringFunction;

/**
 * Class to format the path for a file
 * @author Brandt Davis
 * @version 1.0
 */
public class FormatPath {
    /**
     * Returns a lambda function to format a fxml file path (../fxml/[filename].fxml)
     * @return lambda function
     */
    public static StringFunction format() {
        StringFunction fxml = (s) -> "../fxml/" + s + ".fxml";
        return fxml;
    }
}
