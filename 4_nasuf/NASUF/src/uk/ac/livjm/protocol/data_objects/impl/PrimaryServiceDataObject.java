/*
 * Class Title:         PrimaryServiceDataObject
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */

package NASUF.src.uk.ac.livjm.protocol.data_objects.impl;

//NASUF API imports
import NASUF.src.uk.ac.livjm.protocol.data_objects.AbstractDataObject;

public class PrimaryServiceDataObject extends AbstractDataObject{
   
    //This constructor takes two parameters - the fist paramter
    //is the decap value and the second is the module specification.
    public PrimaryServiceDataObject(String decapValue, String moduleSpec){
	//Assign the parameter values to the class variables.
        this.setDecapValue(decapValue);
        this.setModuleSpec(moduleSpec);
    }
}