/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author eobregon
 */
public class AppLogger {
    
    public static Logger appLogger = Logger.getLogger("appLoger");

    public static void initLogger() {
        FileHandler fh;
        try {
            fh = new FileHandler(AppStaticValues.loggerPath, true);
            appLogger.addHandler(fh);
            SimpleFormatter formater = new SimpleFormatter();
            fh.setFormatter(formater);
            
            appLogger.info("Logger initialized");
        } catch (Exception e) {
            appLogger.log(Level.WARNING, "Exception :: ", e);
        }
    }

    public boolean createAppLogFile() {
        boolean created = false;
        File logFile = new File(AppStaticValues.loggerPath);

        try {
            if (logFile.createNewFile()) {
                System.out.println("Log file created: " + logFile.getName());
                appLogger.info("Logger created!");
            } else {
                System.out.println("Log file already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return created;
    }
    
}
