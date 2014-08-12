/*
 * Class Title:         DCMProfile
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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.DECAPConstants;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.model.dcm.IAttribute;
import NASUF.src.uk.ac.livjm.model.dcm.IDCM;
import NASUF.src.uk.ac.livjm.model.dcm.IDefaultAttribute;
import NASUF.src.uk.ac.livjm.model.writer.impl.cccp_writer.CCCPWriter;
import NASUF.src.uk.ac.livjm.model.writer.WriterFactory;

//Log4j API imports 
import org.apache.log4j.Level;

public class DCMProfile implements IDCM {
    
    //This object contains a reference to the DCM 
    //writer 
    private CCCPWriter dw;
    
    //These collections contain the attributes
    //components and default attributes associated
    //with a DCM Profile.
    private List attributes;
    private List components;
    private List defaultAttributes;
    
    //This variable contains the profile name.
    private String profileName = "";
   
    //Default constructor
    public DCMProfile() {
	//Create a new DCM Writer.
        dw = (CCCPWriter)
	    WriterFactory
		.createDCMWriter();
	//Create the collections.
        components = new ArrayList();
        attributes = new ArrayList();
        defaultAttributes = new ArrayList();
    }
       
    //This setter sets the profile name
    public void setProfileName(String name){
        this.profileName = name;
    }
    
    //Thie getter returns the profile name.
    public String getProfileName(){
        return this.profileName;
    }
    
    //This method adds a component to the components
    //collection.
    public void addComponent(String componentName){
        components.add(componentName);
    }
    
    public void addAttribute(IAttribute attribute){
        attributes.add(attribute);
    }
    
    //This method adds a default attribute to the 
    //default attributes collection.
    public void addDefaultAttribute(IDefaultAttribute attribute){
        defaultAttributes.add(attribute);
    }
    
    //This method creates a new profile.
    private void createDCMDescription_Profile(){
    
	//Iterator through all the components.
        Iterator iter = components.iterator();
        while(iter.hasNext()){
	    //Extract the next component.
            String comp = (String) iter.next();
	    //Using the writer add the statement
            dw.addStatement(
                new DCMStatement(
                    DECAPConstants.DECAPNAMESPACE,
                    this.getProfileName())
                        .addProperty(
                            new DCMProperty(
                                DECAPConstants.CCPPSCHEMA20021108_NAMESPACE,
                                "component", 
                                DECAPConstants.DECAPNAMESPACE + comp)));
        }
    }
    
    //This method creates a new attribute.
    private void createDCMAttribute(){
        //Itterate through the attributes collection.
        Iterator iter = attributes.iterator();
        while(iter.hasNext()){
            
            IAttribute attrib = (IAttribute) iter.next();
	    //Using the writer add the next attribute.
            dw.addStatement(
                new DCMStatement(
                    DECAPConstants.DECAPNAMESPACE,
                    attrib.getResourceName())
                        .addProperty(
                            new DCMProperty(
                                DECAPConstants.CCPPSCHEMA20021108_NAMESPACE,
                                "defaults",
                                attrib.getDefaults()))
                        .addProperty(
                            new DCMProperty(
                                com.hp.hpl.jena.vocabulary.RDF.getURI(), 
                                "type", 
                                attrib.getType())));
        }
    }
    
    //This method creates the defaults for the attribute.
    private void createDCMDefault(){
	//Iterate through the default attributes.
        Iterator iter = defaultAttributes.iterator();
        while(iter.hasNext()){
	    //Get next default attribute
            IDefaultAttribute attrib = (IDefaultAttribute)iter.next();
	    //Add the default attribute using the writer.
            dw.addStatement(
                new DCMStatement(
                    DECAPConstants.DECAPNAMESPACE,
                    attrib.getResourceName())
                    .addProperty(
                        new DCMProperty(
                            DECAPConstants.DECAPNAMESPACE,
                            attrib.getAttributeName(), 
                            attrib.getAttributeValue())));
        }
    }
    
    //This is the toString() method for this object.
    public String toString(){
        try{
	    //Create the profile
            createDCMDescription_Profile();
	    //Create the attributes
            createDCMAttribute();
	    //Create teh default attributes.
            createDCMDefault();
         
	    //Create a string writer
            StringWriter out = new StringWriter();
	    //Using the writer write the model to the StringWriter
            dw.write(out);
	    //Return a string representation of the StringWriter
	    //to the calling object.
            return out.toString();
        }catch(Exception e){
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("DCMProfile: toString: " + 
		    e.toString());
            return null;
        }
    }
}