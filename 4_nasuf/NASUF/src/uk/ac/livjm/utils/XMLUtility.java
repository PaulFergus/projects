/*
 * Class Title:         XMLUtility
 * Class Description:   Need to do
 * Author:              Sun Microsystems
 * Project:             Wireless PnP Home Network
 * Team Members:        Paul Fergus
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Date:                20-08-03
 * Email:               cmppferg@livjm.ac.uk
 * Achnowledgements:    
 */
package NASUF.src.uk.ac.livjm.utils;

public class XMLUtility {
    
    public static String replaceAllXMLCharacters(
        String document){
        String returnDocument = "";
        returnDocument = document.replaceAll("<", "&lt;");
        return returnDocument.replaceAll(">" , "&gt;");
    }
    
    public static String replaceAllXMLEscapeCharacters(
        String document){
        String returnDocument = "";
        returnDocument = document.replaceAll("&lt;", "<");
        return returnDocument.replaceAll("&gt;", ">");
    }
}
