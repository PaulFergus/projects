/*
 * Class Title:         DCMFactory
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.dcm;

//NASUF API imports
import NASUF.src.uk.ac.livjm.model.dcm.impl.DCMProfile;
import NASUF.src.uk.ac.livjm.model.dcm.impl.DCMProperty;
import NASUF.src.uk.ac.livjm.model.dcm.impl.DCMStatement;

public class DCMFactory{
    
    //This method returns a new instance of a DCMProfile
    public static IDCM createDCMProfile(){
        return new DCMProfile();
    }
    
    //This method returns a new instance of a DCMProperty
    public static IDCMProperty createDCMProperty(){
        return new DCMProperty();
    }
    
    //This method returns a new instance of a DCMStatement.
    public static IDCMStatement createDCMStatement(){
        return new DCMStatement();
    }
}