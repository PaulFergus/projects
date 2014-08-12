/*
 * Class Title:         AbstractMatcher
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm.sism.abstract_matcher.impl;

//Java SDK imports
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

//Jxta API imports
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;

//Jena API imports
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdql.Query;
import com.hp.hpl.jena.rdql.QueryEngine;
import com.hp.hpl.jena.rdql.QueryExecution;
import com.hp.hpl.jena.rdql.QueryResults;
import com.hp.hpl.jena.rdql.ResultBinding;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.Algorithm;
import NASUF.src.uk.ac.livjm.algorithm.sism.abstract_matcher.IAbstractMatcher;
import NASUF.src.uk.ac.livjm.constant.SISMConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceMatchingQueryConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.listener.resolver.ResolverMsgHandlerFactory;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;

import NASUF.src.uk.ac.livjm.model.owl.IOntology;

//Log4J API imports
import org.apache.log4j.Level;

public class AbstractMatcher extends Algorithm implements IAbstractMatcher{
    
    //This object contains the devices local ontological structures.
    //This object is used to perform semantic interoperability between
    //different terminology.
    private IOntology distres;
    private int tempCount;
    private IDiSUS disus;
    
    //Default constructor
    public AbstractMatcher(IDiSUS disus) {
	this.disus = disus;
    }
    
    //This is a call back method used by the AMatcher_DistrES_Handler. Results 
    //returned by some remote/local DistrES service are passed to the 
    //abstract matcher algorithm using this call back method. Note the
    //returned result may be true or false.
    public void distrESResult(Object result){
	if(((String)result).equalsIgnoreCase("true"))
	    tempCount = tempCount + 1;
    }
    
    //This method uses an array of IOPEs for the service request and the 
    //service description to determine if a level of match exists between
    //the two
    public Object match(String sr, String sd){
        
        //These models contain the profile described in the service
        //request and the profile defined for a particular application
        //peer service the devices provides.
        Model srModel = ModelFactory.createDefaultModel();
        Model sdModel = ModelFactory.createDefaultModel();
        
        //Read the string representation into the model
        srModel.read(new StringReader(sr), null);
        sdModel.read(new StringReader(sd), null);
        
        //Create a new return message.
        Message msg = new Message();
        //Add the abstract matcher result tag.
        msg.addMessageElement(
                ServiceDescriptionConstants.NASUF_NAMESPACE,
                    new StringMessageElement(
                        ServiceRequestConstants.REQUEST_TYPE,
                        "AbstractMatcherResult", null));
        
        //These are place markers to indicate that we have 
        //matched all the outputs
        if(matchIOPE(getInputs(srModel), getInputs(sdModel)) &&
            matchIOPE(getOutputs(srModel), getOutputs(sdModel)) &&
            matchIOPE(getPreconditions(srModel), getPreconditions(sdModel)) &&
            matchIOPE(getEffects(srModel), getEffects(sdModel))){
          
            //If all the IOPES are matched add the true value 
            //in order to indicate that the service request
            //profile matches 
            msg.addMessageElement(
                ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    new StringMessageElement(
                        SISMConstants.ABSTRACT_MATCHER_TAG,
                        "True", null));
        }else{
            //If any of the IOPES are not matched add the false 
            //value in order to indicate that the service request
            //profile does not match 
            msg.addMessageElement(
                ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    new StringMessageElement(
                        SISMConstants.ABSTRACT_MATCHER_TAG,
                        "False", null));
        }
        return msg;
    }
    
    //This method takes one particular set of iopes, i.e. 
    //inputs or outputs, from the service request and the
    //service description and tries to determine if all
    //of the service requests iopes match iopes in the 
    //service description.
    private boolean matchIOPE(ArrayList sr, ArrayList sd){
        Iterator iter = sr.iterator();
        int numMatched = 0;
        //Iterate through the collection of a particular
        //iope in the service request
        while(iter.hasNext()){
            Object srValue = iter.next();
            //Iterate through the collection of a particular
            //iope in the service description
            //Iterator sdIter = sd.iterator();
	    if(sd.contains(srValue)){
		sd.remove(srValue);
		
		numMatched = numMatched + 1;
	    }else{
		Iterator sdIter = sd.iterator();
		while(sdIter.hasNext()){
		    Object sdValue = sdIter.next();
		    if(relateConcepts((String)srValue, (String)sdValue)> 0){
			System.out.println("relatedConcepts: " + srValue + sdValue);
			numMatched = numMatched + 1;
			break;
		    }
		}
	    }
        }
        //If the numMatched variable is equal to the size of the IOPEs in the
        //service description then return a true value indicating that all
        //the iopes where matched, else return false indicating that the service
        //does not provide the required capabilities.
	System.out.println(sr.size());
	System.out.println(numMatched);
        if(sr.size() == numMatched)
            return true;
        else
            return false;
    }
    
    //This method retrieves all the inputs from the service profile.
    private ArrayList getInputs(Object ontModel){
        Model model = (Model)ontModel;
        return getIOPEs(
            model, 
            ServiceMatchingQueryConstants
                .HAS_INPUT_QUERY, "input");
    }
    //This method retrieves all the outputs from the service profile
    private ArrayList getOutputs(Object ontModel){
        Model model = (Model) ontModel;
        return getIOPEs(
            model, 
            ServiceMatchingQueryConstants
                .HAS_OUTPUT_QUERY, "output");
    }
    //This method retrieves all the preconditions from the service profile
    private ArrayList getPreconditions(Object ontModel){
        Model model = (Model)ontModel;
        return getIOPEs(
            model, 
            ServiceMatchingQueryConstants
                .HAS_PRECONDITION_QUERY, "precondition");
    }
    //This method retrieves all the effects from the service profile.
    private ArrayList getEffects(Object ontModel){
        Model model = (Model)ontModel;
        return getIOPEs(
            model, 
            ServiceMatchingQueryConstants
                .HAS_EFFECT_QUERY, "effect");
    }
    
    //This method retrieves a particular IOPE from a model. The fisrt
    //parameter is the model that contains the metadat, the second
    //parameter is the query used to extract the parameter and the 
    //third is the IOPE type.
    private ArrayList getIOPEs(Model model, String query, String iope){
        //This variable contains the results for the exectured
        //query.
        QueryResults result;
        
        //Execute the query.
        result = (QueryResults)executeQuery((Object)model, query);
        
        ArrayList sp = new ArrayList();
        //Iterate through the results and retrieve the operation name.
        //In this instance there is only one parameter name we are
        //interested in and there is only on instance of the element 
        //operationName in the metadat file, however this method is generic
        for(Iterator iter = result; iter.hasNext();){
            //Extract the iope value.
            ResultBinding res = (ResultBinding)iter.next();
            if(!res.get(iope).toString().equals("")){
                 sp.add(res.get(iope).toString().trim().toLowerCase());
            }
        }
        //Return the collection of iope values.
        return sp;
    }
    
    //This method executes a query using some model and a query
    //string and returns the results back to the caller.
    public QueryResults executeQuery(Object ontModel, String queryString) {
        Query query = new Query(queryString);
        query.setSource((Model)ontModel);
        QueryExecution qe = new QueryEngine(query);
        QueryResults result = qe.exec();
        return result;
    }
    
    //If there is a relationship between two iopes return a value
    //of one to indicate a match has been found.
    private int relateConcepts(String srValue, String sdValue){
	
	System.out.println("srValue: " + (String)srValue + "sdValue: " + (String)sdValue);
	//Create a new SISM handler. This will discover an available SISM
	//servic and pass the PSCM along with the current application
	//peer service profile for assessment.
	new Thread((Runnable)
	    ResolverMsgHandlerFactory
		.createAMatcher_DistrES_Handler(
		    disus, this, srValue, sdValue))
			.start();
	
	//Give the distres service time to respond.
	for(int i = 0; i < 10; i++){
	    try{
		//Wait for 1 seconds to see if a response is recieved.
		Thread.sleep(1000);
	    }catch(java.lang.InterruptedException ie){
		if(NASUFLogger.isEnabledFor(Level.ERROR))
		    NASUFLogger.error("AbstractMatcher: relateConcepts: " + 
			ie.toString());
	    }
//	    //If a response is received initialise tempCount and 
//	    //return a value of one to the calling method. This 
//	    //indicates that a semantic relation between the two 
//	    //terms exists.
	    if(tempCount > 0){
		tempCount = 0;
		System.out.println("I am in here the abstract matcher");
		return 1;
	    }
	}
	//Return the number of matches found
	return 0;
    }
    
    //Need to do.
//    private int matchFound(){
//	return 0;
//    }
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        super.cleanUp();
        distres.cleanUp();
        distres = null;
    }
}