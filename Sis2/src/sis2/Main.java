package sis2;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @version 4.0 02/06/2018
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
            String path = System.getProperty("user.dir")
                    + "/resources/SistemasInformacionII.xlsx";
            ExcelController eC = new ExcelController(path);
            logger.info("Reading file: " + path);

            eC.readFile();
            eC.fillPayrollInfo();
            eC.iterateDocument();

            Scanner in = new Scanner(System.in);
            System.out.print("Introducir fecha:");
            String input = in.next();
            int month = Integer.valueOf(input.substring(0, 2));
            int year = Integer.valueOf(input.substring(3, 7));

            eC.performOperations(month, year);

            logger.info("Updating");
            eC.updateFile();

            logger.info("Generating XML");
            eC.generateXML();

            eC.closeWorkbook();
            
            eC.saveDatabase();

        } catch (IOException ex) {
            logger.info("An IOException occured");
        }
    }

}
