/*
 * Class Title:         NASUFLogger
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.logger.impl;

//Log4j API imports
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class NASUFLogger {
    
    //This variable contains creates an instance of a 
    //logger using the NASUFLogger class name 
    public static Logger logger = 
        Logger.getLogger(
            NASUFLogger.class.getName());
    
    //This method allows the priority level
    //to be determined.
    public static boolean isEnabledFor(Priority level){
        return logger.isEnabledFor(level);
    }
    
    //This method allows a message to be logged as
    //a information message.
    public static void info(Object object){
        logger.info(object);
    }
    
    //This method allows a message to be logged as 
    //a warning.
    public static void warn(Object object){
        logger.warn(object);
    }
    
    //This method allows a message to be logged as
    //an error message.
    public static void error(Object object){
        logger.error(object);
    }
    
    //This method allows a message to be logged as
    //a debug message.
    public static void debug(Object object){
        logger.debug(object);
    }
}