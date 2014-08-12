/*
 * Class Title:		PSM
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.model.psm.impl;

//Java SDK imports 
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Jena API imports
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

//NASUF API imports
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.model.owls.iope.Effect;
import NASUF.src.uk.ac.livjm.model.owls.iope.impl.IOPE;
import NASUF.src.uk.ac.livjm.model.owls.iope.Precondition;
import NASUF.src.uk.ac.livjm.model.owls.OWLServicesFactory;
import NASUF.src.uk.ac.livjm.model.psm.IPSM;
import NASUF.src.uk.ac.livjm.model.writer.impl.owls_writer.OWLSWriter;
import NASUF.src.uk.ac.livjm.model.writer.WriterFactory;

//OWL-S API imports
import org.mindswap.owl.impl.OWLResourceImpl;
import org.mindswap.owl.Util;
import org.mindswap.owls.process.Input;
import org.mindswap.owls.process.Output;
import org.mindswap.owls.process.Parameter;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.vocabulary.OWLS_1_0;

//Log4J API imports
import org.apache.log4j.Level;

public class PSM implements IPSM{
    
    //This variable contains an intance of an 
    //OWLS model
    private OWLS_1_0 OWLS = OWLS_1_0.instance;
    
    //This varaible contains the peer service model.
    private OntModel model;
    //This variable contains the profile that comprises
    //the peer service model.
    private Profile profile;
    
    //These collections contain the IOPEs used to 
    //describe the profile.
    private List inputs;
    private List outputs;
    private List preconditions ;
    private List effects;
    
    //Default constructor.
    public PSM() {
	//Create a new ontology model.
        model = ModelFactory.createOntologyModel();       
	//Create a new profile.
        profile = OWLServicesFactory.createProfile(
            Util
                .toResource(OWLS.Profile.URI + "PSMProfile"));
	//Create the IOPE collections.
	inputs = new ArrayList();
	outputs = new ArrayList();
	preconditions = new ArrayList();
	effects = new ArrayList();
    }
    
    //This getter method returns the profile
    public Profile getProfile(){
        return profile;
    }
    
    //This setter sets the Profile label.
    public void setProfileLabel(String label){
        profile.setLabel(label);
    }
    
    //This getter returns the Profile label. 
    public String getProfileLabel(){
        return profile.getLabel();
    }
    
    //This setter sets the Profile Text Description.
    public void setProfileTextDescription(String description) {
        profile.setTextDescription(description);
    }
    
    //Thie getter returns the Profile Text Description.
    public String getProfileTextDescription(){
        return profile.getTextDescription();
    }
    
    //This method adds an input to the profile.
    private void addProfileInput(Input input) {
        profile.getInputs().add(input);
    }
    
    //This method adds a collection of inputs to the 
    //profile.
    private void addProfileInputs(){
        Iterator iter = inputs.iterator();
        
	//Iterator through each item in the 
	//collection and add it to the profile.
        while(iter.hasNext()){
            profile.getInputs().add((Input)iter.next());
        }
    }
    
    //This method adds an output to the profile.
    public void addProfileOutput(Output output){
        profile.getOutputs().add(output);
    }
    
    //This method adds a collection of outputs to the
    //profile.
    private void addProfileOutputs(){
        Iterator iter = outputs.iterator();
	
	//Iterate through each item and add it to
	//the profile.
        while(iter.hasNext()){
            profile.getOutputs().add((Output)iter.next());
        }
    }
    
    //This method adds a precondition to the profile.
    private void addProfilePrecondition(Parameter precondition){
        profile.getPreconditions().add(precondition);
    }
    
    //This method adds a collection of preconditions to the
    //profile.
    private void addProfilePreconditions(){
        Iterator iter = preconditions.iterator();
	
	//Iterator through each item and add it to
	//the profile.
        while(iter.hasNext()){
            profile.getPreconditions().add((Parameter)iter.next());
        }
    }
    
    //This method adds an effect to the profile
    private void addProfileEffect(Parameter effect){
        profile.getEffects().add(effect);
    }
    
    //This method adds a collection of efffects to the 
    //profile.
    private void addProfileEffects(){
        Iterator iter = effects.iterator();
        
	//Iterator through the collection and add each 
	//item to the profile.
	while(iter.hasNext()){
            profile.getEffects().add((Parameter)iter.next());
        }
    }
    
    //This method adds an input to the inputs collection.
    public void addInput(Input input){
        inputs.add(input);
    }
    
    //This getter returns the collection of inputs.
    public List getInputs(){
        return inputs;
    }
    
    //This method adds an output to the outputs collection.
    public void addOutput(Output output){
        outputs.add(output);
    }
    
    //This getter returns a collection of outputs.
    public List getOutputs(){
        return outputs;
    }
    
    //This method adds a precondition to the preconditions
    //collection
    public void addPrecondition(Parameter condition){
        preconditions.add(condition);
    }
    
    //This getter returns the collection of preconditions.
    public List getPreconditions(){
        return preconditions;
    }
    
    //This method adds an effect to the effects collection
    public void addEffect(Parameter effect){
        effects.add(effect);
    }
    
    //This getter returns the effects collection.
    public List getEffects(){
        return effects;
    }
    
    //This method creates an IOPE and returns an 
    //IOPE instance. The method takes three parameters -
    //the first parameter is the iope type and the
    //second parameter is the resource name. The last 
    //parameter is the resource type assicated with 
    //the IOPE.
    private Object createIOPE(String type,
        String resourceName,
        Resource resourceType){
        Object iope_Instance = null;
        
	//If the type is input then create an input iope
        if(type.equalsIgnoreCase("Input")){
            iope_Instance = 
                (Object)OWLServicesFactory
                    .createInput(
                        model.createResource(resourceName));
	//If the type is output then create an output iope
        }else if(type.equalsIgnoreCase("Output")){
            iope_Instance = 
                (Object)OWLServicesFactory
                    .createOutput(
                        model.createResource(resourceName));
	//If the type is precondition then create a precondition iope
        }else if(type.equalsIgnoreCase("Precondition")){
            //This is a frig because the OWLSFactory does not have
            //a factory method for Preconditions.
            iope_Instance = 
                (Object)OWLServicesFactory
                    .createNASUFPrecondition(
                        model.createResource(resourceName));
	//If the type is an effect tehn create an effect iope.
        }else if(type.equalsIgnoreCase("Effect")){
	    //This is a frig because the OWLSFactory does not have
            //a factory method for Effects.
            iope_Instance = 
                (Object)OWLServicesFactory
                    .createNASUFEffect(
                        model.createResource(resourceName));
        }
	//If the iope_instance is not null then set the resource
	//type.
        if(iope_Instance != null){
            ((Parameter)iope_Instance).setType(new OWLResourceImpl(resourceType));
        }
	//return the iope to the caller.
        return iope_Instance;
    }
    
    //This method creates a new profile that only contains inputs.
    public void createProfile(
        List inputs){
	//If the collection is not null then add each input 
	//to the profile.
        if(inputs != null){
	    //Iterate through the inputs collection and add them
	    //to the profile.
            Iterator iter = inputs.iterator();
            while(iter.hasNext()){
                IOPE iope = (IOPE)iter.next();
                addInput(
                    (Input)createIOPE(
                        iope.getType(), 
                        iope.getName(),
                            model.createResource(iope.getIOPEDataType())));
            }
	    //Add inputs to profile
            addProfileInputs();
        }
    }
    
    //This method creates a new profile that contains inputs
    //and outputs
    public void createProfile(
        List inputs, List outputs){
        
	//Add the inputs to the profile.
        this.createProfile(inputs);
	//If the collection is not null then add each output 
	//to the profile.
        if(outputs != null){
	    //Iterate through the outputs collection and add them
	    //to the profile.
            Iterator iter = outputs.iterator();
            while(iter.hasNext()){
                IOPE iope = (IOPE)iter.next();
                addOutput(
                    (Output)createIOPE(
                        iope.getType(), 
                        iope.getName(),
                            model.createResource(iope.getIOPEDataType())));
            }
	    //Add outputs to profile
            addProfileOutputs();
        }
    }
    
    //This method creates a new profile that contains inputs
    //outputs and preconditions
    public void createProfile(
        List inputs, List outputs, List preconditions){
        
	//Add inputs and outputs to the profile
        this.createProfile(inputs, outputs);
	//If the collection is not null then add each precondition 
	//to the profile.
        if(preconditions != null){
	    //Iterate through the preconditions collection and add them
	    //to the profile.
            Iterator iter = preconditions.iterator();
            while(iter.hasNext()){
                IOPE iope = (IOPE)iter.next();
                addPrecondition(
                    (Precondition)createIOPE(
                        iope.getType(), 
                        iope.getName(),
                            model.createResource(iope.getIOPEDataType())));
            }
	    //Add preconditions to profile
            addProfilePreconditions(); 
        }
    }
    
    //This method creates a new profile that contains inputs
    //outputs, preconditions and effects
    public void createProfile(
        List inputs, 
        List outputs, 
        List preconditions,
        List effects){
        
	//Add inputs, outputs and preconditions to the profile
        this.createProfile(inputs, outputs, preconditions);
        //If the collection is not null then add each effect 
	//to the profile.
        if(effects != null){
	    //Iterate through the effects collection and add them
	    //to the profile.
            Iterator iter = effects.iterator();
            while(iter.hasNext()){
                IOPE iope = (IOPE)iter.next();
       
                addEffect(
                    (Effect)createIOPE(
                        iope.getType(), 
                        iope.getName(),
                            model.createResource(iope.getIOPEDataType())));
            }
	    //Add effects to profile
            addProfileEffects();
        }
    }
    
    //This method writes the output to the output stream.
    public void write(OutputStream out){
        try{
	    //Create an OWLSWriter. Remember that the value
	    //returned from the WriterFactory is a IWriter 
	    //interface so it needs to be casted into a 
	    //OWLSWriter so that the OWLS specific write
	    //method can be accessed.
            OWLSWriter writer = (OWLSWriter)
		WriterFactory
		    .createOWLSWriter();
            //Write the profile to the output stream.
            writer.write(profile, out);
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("PSM: write: " +
		    e.toString());
        }    
    }
    
    //This is the overriden toString method for this object.
    public String toString(){
        try{
	    //Create a string writer, which will contain a 
	    //string representation of the profile.
            StringWriter out = new StringWriter();
   
	    //Create an owls writer.
            OWLSWriter writer = (OWLSWriter)
		WriterFactory
		    .createOWLSWriter();  
	    //Write the profile to the output stream.
            writer.write(profile, out);
	    //Return a string representation of the profile.
            return out.toString();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("PSM: toString: " + 
		    e.toString());
        }
        return null;
    }
}