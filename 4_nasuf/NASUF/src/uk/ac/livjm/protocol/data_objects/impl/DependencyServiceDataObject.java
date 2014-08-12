/*
 * Class Title:         DependencyServiceDataObject
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

public class DependencyServiceDataObject extends AbstractDataObject{
   
    //This variable contains the primary service name
    private String primaryService;
  
    //This constructor takes four parameters - the fist paramter
    //is the decap value and the second is the primary service
    //The third is the name if the dependency service and the last
    //is the module specification.
    public DependencyServiceDataObject(
	    String decapValue, 
	    String primaryService, 
	    String name, 
	    String moduleSpec){
	//Assign the parameter values to the class variables.
        this.setDecapValue(decapValue);
	this.setModuleSpec(moduleSpec);
	this.setName(name);
        this.setPrimaryService(primaryService);
    }
    
    //This getter returns the primary service.
    public String getPrimaryService(){
        return primaryService;
    }
    
    //This setter sets the primary service.
    public void setPrimaryService(String primaryService){
        this.primaryService = primaryService;
    }
}