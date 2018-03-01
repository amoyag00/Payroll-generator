/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis2;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author alex
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger logger= MyLogger.getLogger();
       try {
            logger.info("Starting application");
            String path=System.getProperty("user.dir") +"/resources/SistemasInformacionII.xlsx";
            ExcelController eC=new ExcelController(path);
            logger.info("Reading file: "+path);
            eC.readFile();
            logger.info("Validating IDs");
            eC.validateIDs();
            logger.info("Updating IDs");
            eC.updateFile();
            logger.info("Generating XML");
            eC.generateXML(System.getProperty("user.dir") +
            "/resources/Errores.xml");
            
        } catch (IOException ex) {
            logger.info("An IOException occured");
        }
    }
    
}
