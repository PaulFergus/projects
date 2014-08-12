/*
 * Class Title:         MAUTQuery
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.query.dcm.impl;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.DECAPConstants;
import NASUF.src.uk.ac.livjm.model.query.rdql.impl.PrefixMapping;
import NASUF.src.uk.ac.livjm.model.query.rdql.impl.SelectModel;
import NASUF.src.uk.ac.livjm.model.query.rdql.impl.WhereModel;
import NASUF.src.uk.ac.livjm.model.query.rdql.impl.UsingModel;

public class MAUTQuery {
    
    //This static method creates a MAUT Query. It takes two
    //parameters - the first parameter is the qos parameter,
    //i.e. bandwidth and the second parameter is the 
    //importanceRating.
    public static String createMAUTQuery(
        String xValue,
        String yValue){
	    //Create a new select model
            SelectModel sm = 
            new SelectModel()
                .addSelectField("*")
                .addWhereStatement(
                    new WhereModel()
                        .setXValue("dcm:", xValue)
                        .setYValue("dcm:", yValue)
                        .setZValue("?x")
                        .setUsingModel(
                            new UsingModel()
                                .addPrefixMapping(
                                    new PrefixMapping(
                                        "dcm",
                                        DECAPConstants.DECAPNAMESPACE))));
	    //Return the query to the caller.
            return sm.toString();
    }
}
