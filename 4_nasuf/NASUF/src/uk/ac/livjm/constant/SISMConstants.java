/*
 * Class Title:         SISMConstants
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.constant;

public class SISMConstants {
    
    //Core Service Advertisements
    public static final String SISM_MOD =               "JXTAMOD:SISM";
    public static final String SISM_SPEC =              "JXTASPEC:SISM";
    
    //Core Service Pipe Advertisement
    public static final String SISMPIPE =               "sismPipe.xml";
    
    //SISM XML Tags
    public static final String ABSTRACT_MATCHER_TAG =   "AbstractMatcherTag";
    public static final String ABSTRACT_RESULT =        "AbstractResult";
    
    //SISM Namespaces
    public static final String DECAPNAMESPACE = 
        ServiceDescriptionConstants
            .LIVJM_NAMESPACE + "sism#";
}