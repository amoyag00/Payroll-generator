/**
 * Class used for logging issues.
 */
package sis2;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @version 4.0 02/06/2018
 * @author Alejandro Moya García
 */
public class MyLogger {

    private static final String LOGFILE_PATH = System.getProperty("user.dir") + 
            "/resources/sis2.log";
    private static Logger instance;

    /**
     * Returns a logger with its settings established
     *
     * @return the logger
     */
    public static Logger getLogger() {
        if (instance == null) {
            instance = Logger.getLogger(MyLogger.class.getName());
            FileHandler fileHandler = null;
            try {
                fileHandler = new FileHandler(LOGFILE_PATH, true);
            } catch (IOException ex) {
                Logger.getLogger(MyLogger.class.getName()).log(Level.SEVERE,
                        null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(MyLogger.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
            instance.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            instance.setUseParentHandlers(false);//For removing console logging
        }

        return instance;
    }
}
