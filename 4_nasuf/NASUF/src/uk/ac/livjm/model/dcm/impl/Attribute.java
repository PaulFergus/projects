/*
 * Class Title:         Attribute
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.dcm.impl;

//NASUF API imports
import NASUF.src.uk.ac.livjm.model.dcm.IAttribute;

public class Attribute implements IAttribute {
    
    private String defaults = "";
    private String resourceName = "";
    private String type = "";
    
    //Default constructor
    public Attribute(){
    }
    
    //This constructor takes three parameters - the first parameter
    //is the rescourse name and the second is the default value it
    //supports. The last parameter is the type assiciated with the
    //resource.
    public Attribute(
        String resourceName,
        String defaults,
        String type) {
	//Assign the values to the class variables.
        this.resourceName = resourceName;
        this.defaults = defaults;
        this.type = type;
    }
    
    //This setter sets the default values for the resource.
    public void setDefaults(String defaults){
        this.defaults = defaults;
    }
    
    //This method returns the defaults for the resource
    public String getDefaults(){
        return defaults;
    }
    
    //This setter sets the type associated with the 
    //resource.
    public void setType(String type){
        this.type = type;
    }
    
    //This getter returns the type associated with 
    //the resource.
    public String getType(){
        return this.type;
    }
    
    //This getter returns the resource name.
    public String getResourceName() {
        return this.resourceName;
    }
    
    //This setter sets the resource name.
    public void setResourceName(String name) {
        this.resourceName = name;
    }
}