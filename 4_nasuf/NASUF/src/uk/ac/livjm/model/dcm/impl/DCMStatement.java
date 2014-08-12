/*
 * Class Title:         DCMStatement
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

//Java SDK imports
import java.util.List;
import java.util.ArrayList;

//Jena API imports
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

//NASUF API imports
import NASUF.src.uk.ac.livjm.model.dcm.IDCMStatement;
import NASUF.src.uk.ac.livjm.model.dcm.IDCMProperty;

public class DCMStatement implements IDCMStatement {
    
    //This model is used to create model constructs - 
    //in this instance it is used for resource 
    //constructs.
    private Model model;
    //This construct holds the resource.
    private Resource resource = null;
    //This collection contains the properties
    //of a collection.
    private List properties = null;
    
    //Default Constructor.
    public DCMStatement() {
	//Create a new default model
	model = ModelFactory.createDefaultModel();
	//Create a collection for the properties.
        properties = new ArrayList();
    }
    
    //This constructor takes two parameters - the first
    //parameter contains the namespace value and the 
    //second parameter contains the resource.
    public DCMStatement(
        String namespace,
        String resource){
        this();
        this.createResource(namespace, resource);
    }
    public DCMStatement(
        String namespace,
        String resource,
        IDCMProperty property){
	//Assign the values to the class variables.
        this(namespace, resource);
        this.addProperty(property);
    }
    
    //This getter returns the resource.
    public Resource getResource(){
        return resource;
    }
    
    //This method creates the resource using the model
    public void createResource(String namespace, String value){
        resource = model.createResource(namespace + value);
    }
    
    //The method returns the collection of properties.
    public List getProperties(){
        return properties;
    }
    
    //This method adds a property and returns the new 
    //collection.
    public IDCMStatement addProperty(IDCMProperty property){
        properties.add(property);
        return this;
    }
}