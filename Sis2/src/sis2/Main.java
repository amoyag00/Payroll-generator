package sis2;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @version 2.0 05/04/2018
 * @author Alejandro Moya García
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger logger = MyLogger.getLogger();
        try {
            logger.info("Starting application");
            String path = System.getProperty("user.dir") + 
                    "/resources/SistemasInformacionII.xlsx";
            ExcelController eC = new ExcelController(path);
            logger.info("Reading file: " + path);
            eC.readFile();
            logger.info("Validating IDs");
            eC.iterateDocument();
            logger.info("Updating IDs");
            eC.updateFile();
            logger.info("Generating XML");
            eC.generateXML();

        } catch (IOException ex) {
            logger.info("An IOException occured");
        }
    }

}
