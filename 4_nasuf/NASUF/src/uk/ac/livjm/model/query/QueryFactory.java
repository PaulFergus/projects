/*
 * Class Title:         QueryFactory
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.query;

//NASUF API imports
import NASUF.src.uk.ac.livjm.model.query.dcm.impl.MAUTQuery;

public class QueryFactory {
    
    //This static method returns a DCM Query. It takes two parameters
    //- the first parameter is the parameter name, i.e. bandwidth and
    //the second parameter is the importanceRating.
    public static String createDCMQuery(String xValue, String yValue){
        return MAUTQuery.createMAUTQuery(xValue, yValue);
    }
}
