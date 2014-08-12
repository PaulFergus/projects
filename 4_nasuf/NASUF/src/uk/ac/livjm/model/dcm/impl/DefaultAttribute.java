/*
 * Class Title:         DefaultAttribute
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
import NASUF.src.uk.ac.livjm.model.dcm.IDefaultAttribute;

public class DefaultAttribute  implements IDefaultAttribute{
    
    //This class variables contain the attribte name and
    //value including the resource name.
    String attributeName = null;
    String attributeValue = null;
    String resourceName = null;
    
    //Default constructor
    public DefaultAttribute() {
    }
    
    //This constructor takes three parameters - the frist parameter
    //is the resource name, the second is the attribute name and 
    //the last parameter is the attribute value.
    public DefaultAttribute(
        String resourceName,
        String attributeName,
        String attributeValue){
	//Assign the parameters to the class variables.
        this.resourceName = resourceName;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }
    
    //This setter method assigns the name of the attribute
    //to the class variable name.
    public void setAttributeName(String name){
        this.attributeName = name;
    }
    
    //This getter method returns the class variable name.
    public String getAttributeName(){
        return this.attributeName;
    }
    
    //This setter assigns an attribute value to the 
    //class variable value.
    public void setAttributeValue(String value){
        this.attributeValue = value;
    }
    
    //Thie getter returns the class variable value.
    public String getAttributeValue(){
        return this.attributeValue;
    }
    
    //Thie getter returns the resouce name 
    public String getResourceName() {
        return this.resourceName;
    }
    
    //This setter assigns the resoucre name to the
    //class variable resourceName.
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }   
}