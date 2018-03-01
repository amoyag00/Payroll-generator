package sis2;


import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import sis2.Main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public class MyLogger {
    private static final String LOGFILE_PATH=System.getProperty("user.dir")+"/resources/sis2.log";
    private static Logger instance;
    
    /**
     * Returns a logger with its settings established
     * @param name
     * @return the logger
     */
    public static Logger getLogger() {
        if(instance==null){
            instance= Logger.getLogger(MyLogger.class.getName());
            FileHandler fileHandler=null;        
            try {
                fileHandler = new FileHandler(LOGFILE_PATH, true);
            } catch (IOException ex) {
                Logger.getLogger(MyLogger.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(MyLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
            instance.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            instance.setUseParentHandlers(false);//For removing console logging
        }
        
        return instance;
    }
}
