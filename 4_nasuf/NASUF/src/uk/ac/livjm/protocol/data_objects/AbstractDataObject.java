/*
 * Class Title:         AbstractDataObject
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */

package NASUF.src.uk.ac.livjm.protocol.data_objects;

public abstract class AbstractDataObject implements IDataObject{
    
    //These variables contain the decap value
    //module specification and the service name.
    private String decapValue;
    private String moduleSpec;
    private String name;
    
    //This getter returns the decap value
    public String getDecapValue(){
        return decapValue;
    }
    
    //This setter sets the decap value.
    public void setDecapValue(String decapValue){
        this.decapValue = decapValue;
    }
   
    //This getter returns the dependency service name.
    public String getName(){
        return name;
    }
    
    //This setter sets the dependency service name.
    public void setName(String name){
        this.name = name;
    }
    
    //This getter returns the module specification
    public String getModuleSpec(){
        return moduleSpec;
    }
    
    //This setter sets the module specification.
    public void setModuleSpec(String moduleSpec){
        this.moduleSpec = moduleSpec;
    }
}
