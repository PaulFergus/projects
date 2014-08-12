/*
 * Class Title:		IOPE
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.owls.iope.impl;

public class IOPE {
    
    //This class variables contain the iope name,
    //namespace, type and datatype information.
    private String iopeName;
    private String iopeNamespace;
    private String iopeType;
    private String iopeDataType;
    
    //Default constructor
    public IOPE() {
    }
    
    //This setter sets the name of the iope
    public void setName(String name){
        this.iopeName = name;
    }
    
    //This getter returns the name of the iope
    public String getName(){
        return this.iopeName;
    }
    
    //This setter sets the namespace associated with
    //the iope.
    public void setNamespace(String namespace){
        this.iopeNamespace = namespace;
    }
    
    //This getter returns the namespace assigned to the 
    //iope.
    public String getNamespace(){
        return this.iopeNamespace;
    }
    
    //This setter sets the type of iope it is.
    //i.e. input, output, precondition or effect.
    public void setType(String type){
        this.iopeType = type;
    }
    
    //This getter returns the type of iope is it,
    //i.e. input, output, precondition or effect.
    public String getType(){
        return this.iopeType;
    }
    
    //This setter sets the iope datatype.
    public void setIOPEDataType(String type){
        this.iopeDataType = type;
    }
    
    //This getter returns the data type associated
    //with the iope.
    public String getIOPEDataType(){
        return this.iopeDataType;
    }
}