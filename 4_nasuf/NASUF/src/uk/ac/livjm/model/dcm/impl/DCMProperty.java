/*
 * Class Title:         DCMProperty
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

//Jena API imports
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

//NASUF API imports
import NASUF.src.uk.ac.livjm.model.dcm.IDCMProperty;

public class DCMProperty implements IDCMProperty{
    
    //This class variable is used to make model 
    //elements.
    private Model model;
    //This variable contains a model element of 
    //type property.
    private Property property;
    //This variable will hold the corresponding 
    //property value.
    private Object value;
    
    //Default constructor.
    public DCMProperty(){
	//Create a new default model.
	ModelFactory.createDefaultModel();
    }
    
    //This constructor takes three parameters - the first 
    //parameter is the namespace the property will use.
    //The second parameter is the property and the last 
    //variable is the value associated with the property.
    public DCMProperty(
        String namespace,
        String property,
        Object value) {
	//Assign these values to the class variables.
        this.createProperty(namespace, property);
        this.setValue(value);
    }
    
    //The getter returns the property to the 
    //caller.
    public Property getProperty(){
        return property;
    }
    
    //This method creates a new property. It takes two parameters
    //- the first parameter is the namespace and the second 
    //parameter is the property name.
    public void createProperty(String namespace, String property){
	//Using the default model create a property.
        this.property = model.createProperty(namespace + property);
    }
    
    //This getter returns the value associated with the 
    //property.
    public Object getValue(){
        return value;
    }
    
    //This setter sets the value of the property.
    public void setValue(Object value){
        this.value = value;
    }
}