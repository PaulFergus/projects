/*
 * Class Title:         DistrESAlgorithm
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.algorithm.distres.impl;

//Java SDK imports
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

//JXTA API imports
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;

//Protege OWL API imports
import edu.stanford.smi.protegex.owl.model.OWLModel; 
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass; 
import edu.stanford.smi.protegex.owl.ProtegeOWL; 
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager; 
import edu.stanford.smi.protegex.owl.inference.protegeowl.ProtegeOWLReasoner; 
import edu.stanford.smi.protegex.owl.inference.dig.reasoner.DIGReasonerIdentity;

//NASUF API imports.
import NASUF.src.uk.ac.livjm.algorithm.AlgorithmFactory;
import NASUF.src.uk.ac.livjm.algorithm.distres.ee.IEE;
import NASUF.src.uk.ac.livjm.algorithm.distres.epe.IEPE;
import NASUF.src.uk.ac.livjm.algorithm.distres.IDistrESAlgorithm;
import NASUF.src.uk.ac.livjm.constant.DistrESConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;

//Log4J API imports
import org.apache.log4j.Level;

public class DistrESAlgorithm implements IDistrESAlgorithm{
      
    //This DistrES ontology model is held in memory using 
    //this OWLModel object.
    public OWLModel distresOntology;
    //This is the reasoner used to reason over the distres
    //ontology. 
    private ProtegeOWLReasoner reasoner;
    
//    //This constant defines the http url used to connect to 
//    //the racer dig reasoner. 
    private final String REASONER_URL = "http://localhost:8080";
    
    //This object allows concepts to be extracted from the devices
    //ontology.
    private IEPE epe;
    
    private IEE ee;
    
    //Default constructur.
    public DistrESAlgorithm() {
//	try{
//	    //Load the ontology.
//	    distresOntology = 
//		ProtegeOWL
//		    .createJenaOWLModelFromInputStream(
//			new FileInputStream(
//			    DistrESConstants.DISTRES_ONTOLOGY));
//	    if(NASUFLogger.isEnabledFor(Level.INFO))
//		NASUFLogger.info("Successfully Loaded Device's DistrES Ontology");
//	}catch(Exception e){
//	    if(NASUFLogger.isEnabledFor(Level.ERROR))
//		NASUFLogger.error("create error: " + e.toString());
//	}
//	
//	//This is a workaround because there is a bug in protege
//	//3.1. Mathew Horridge says that this will be fixed in 
//	//the next release of Protege.
//	Thread.currentThread()
//	    .setContextClassLoader(
//		DistrESAlgorithm
//		    .class
//			.getClassLoader());
//
//	// Get the reasoner manager and obtain a reasoner for 
//        // the OWL model. 
//        ReasonerManager reasonerManager = ReasonerManager.getInstance(); 
//        reasoner = reasonerManager.getReasoner(distresOntology);
//	
//        // Set the reasoner URL and test the connection 
//        reasoner.setURL(REASONER_URL); 
//	if(reasoner.isConnected()) { 
//	    // Get the reasoner identity - this contains information 
//            // about the reasoner, such as its name and version, 
//            // and the tell and ask operations that it supports. 
//            DIGReasonerIdentity reasonerIdentity = reasoner.getIdentity(); 
//            if(NASUFLogger.isEnabledFor(Level.INFO))
//		NASUFLogger.info("Connected to " + reasonerIdentity.getName());
//	    try{
//		//The classifyTaxonomy method bulk queries the reasoner for the consistency, 
//		//infered superclasses, and equivalent classes of very class in the ontology
//		//(using only 3 HTTP request/responses) and then inserts this information
//		//into the Protege-OWL model. The model can then be queried for the stored
//		//inferred information.
//		//reasoner.classifyTaxonomy(null);
//	    }catch(Exception e){
//		if(NASUFLogger.isEnabledFor(Level.ERROR))
//		    NASUFLogger.error("Constructor Error: " + 
//			e.toString());
//	    }
//	}
//	epe = AlgorithmFactory.createEvolutionaryPatternExtractor(distresOntology);
//	//Create a new instance of the extraction engine so that concepts
//	//can be retrieved from the devices ontology.
//	ee = AlgorithmFactory.createExtractionEngine(reasoner, distresOntology);
    }
    //This is a public getter that returns the devices local ontology.
    public Object getDistrESOntology(){
	return distresOntology;
    }
    //This method determines whether the terms are related via 
    //either subclass a subclass relationship or a equivalent to
    //relationship.
    public Object performTermSemInterop(Object xTerm, Object yTerm){
	//Create the return message.
        Message msg = new Message();
        
	System.out.println("I am in the performTermSemInterop");
        //Add the MAUTResult tag to the message.
        msg.addMessageElement(
	    ServiceDescriptionConstants.NASUF_NAMESPACE,
		new StringMessageElement(
		    ServiceRequestConstants.REQUEST_TYPE,
			"DISTRESResult", null));
     
	//Step 1    If the IOPE in the service request is the same 
	//	    as the IOPE in the service description then this 
	//	    constitutes an exact match.
//	if(((String)xTerm).equalsIgnoreCase((String)yTerm)){
//	    if(NASUFLogger.isEnabledFor(Level.INFO))
//		NASUFLogger.info(xTerm + " is the same as " + yTerm);
//	    //Add the result.
//            msg.addMessageElement(
//                ServiceDescriptionConstants.NASUF_NAMESPACE, 
//                    new StringMessageElement(
//                        DistrESConstants.DISTRES_SEMINTEROP_RESULT,
//                        "true", null));
//	    return msg;
//	}
//	//Step 2    If the IOPE in the service request is disjoint 
//	//	    with the IOPE in the service description then
//	//	    return false. 
//	if(epe.isDisjointTo(xTerm, yTerm, distresOntology)){
//	    if(NASUFLogger.isEnabledFor(Level.INFO))
//		NASUFLogger.info(xTerm + " is disjoint with " + yTerm);
//	    //Add the result.
//            msg.addMessageElement(
//                ServiceDescriptionConstants.NASUF_NAMESPACE, 
//                    new StringMessageElement(
//                        DistrESConstants.DISTRES_SEMINTEROP_RESULT,
//                        "false", null));
//	    return msg;
//	}
//	//Step 3    If the IOPE in the service request has an ‘equivalentTo’ 
//	//	    relationship with the IOPE in the service description 
//	//	    then this constitutes an exact match.
//	if(epe.isEquivalentTo(xTerm, yTerm, distresOntology)){
//	    if(NASUFLogger.isEnabledFor(Level.INFO))
//		NASUFLogger.info(xTerm + " is equivalent to " + yTerm);
//	    //NEED TO DO: We need to evalute the semantic distance
//	    //Add the result.
//            msg.addMessageElement(
//                ServiceDescriptionConstants.NASUF_NAMESPACE, 
//                    new StringMessageElement(
//                        DistrESConstants.DISTRES_SEMINTEROP_RESULT,
//                        "true", null));
//	    return msg;
//	}
	try{
	    
	    //Step 4    If the IOPE in the service request is a subclass of the 
	    //	    IOPE in the service description then this constitutes 
	    //	    an exact match.
	    if(epe.isSubclassOf(xTerm, yTerm, distresOntology)){
		if(NASUFLogger.isEnabledFor(Level.INFO))
		    NASUFLogger.info(xTerm + " is a subclass of " + yTerm);
		//NEED TO DO: We need to evalute the semantic distance
		//Add the result.
		msg.addMessageElement(
		    ServiceDescriptionConstants.NASUF_NAMESPACE, 
			new StringMessageElement(
			    DistrESConstants.DISTRES_SEMINTEROP_RESULT,
			    "true", null));
		return msg;
	    }
	    

	    //Step 5    If the IOPE in the service description subsumes the IOPE 
	    //	    in the service request then this constitutes a plug-in 
	    //	    match.
	    if(epe.isSubsumedBy(xTerm, yTerm, distresOntology)){
		if(NASUFLogger.isEnabledFor(Level.INFO))
		    NASUFLogger.info(yTerm + " is subsumed by " + xTerm);
		//NEED TO DO: We need to evalute the semantic distance
		//Add the result.
		msg.addMessageElement(
		    ServiceDescriptionConstants.NASUF_NAMESPACE, 
			new StringMessageElement(
			    DistrESConstants.DISTRES_SEMINTEROP_RESULT,
			    "true", null));
		return msg;
	    }
	    //Step 6    If the IOPE in the service request subsumes the IOPE in 
	    //	    the service description then this constitutes a subsumption 
	    //	    relationship.
	    if(epe.isSubsumedBy(yTerm, xTerm, distresOntology)){
		if(NASUFLogger.isEnabledFor(Level.INFO))
		    NASUFLogger.info(xTerm + " is subsumed by " + yTerm);
		//NEED TO DO: We need to evalute the semantic distance
		//Add the result.
		msg.addMessageElement(
		    ServiceDescriptionConstants.NASUF_NAMESPACE, 
			new StringMessageElement(
			    DistrESConstants.DISTRES_SEMINTEROP_RESULT,
			    "true", null));
		return msg;
	    }
	    //Step 7    Anything else fails.
	    if(NASUFLogger.isEnabledFor(Level.INFO))
		    NASUFLogger.info("No relationship could be found between " + 
			xTerm + " and " + yTerm);
	    //Add the result.
	    msg.addMessageElement(
		ServiceDescriptionConstants.NASUF_NAMESPACE, 
		    new StringMessageElement(
			DistrESConstants.DISTRES_SEMINTEROP_RESULT,
			    "false", null));
	    return msg;
	}catch(Exception e){
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		    NASUFLogger.error("performTemSemInterop Error: " +
			e.toString());
	    //Add the result.
	    msg.addMessageElement(
		ServiceDescriptionConstants.NASUF_NAMESPACE, 
		    new StringMessageElement(
			DistrESConstants.DISTRES_SEMINTEROP_RESULT,
			    "false", null));
	    return msg;
	}
    }
    
    public void addUnknownTerm(Object term) {
    }
    public Object getUnknownTermTable() {
	return null;
    }
    public Object removeUnknownTerm(Object term) {
	return null;
    }
    //This method extracts the concpet surrounding the term passed in as a parameter
    public Object extractConcept(Object term){
	return ee.extractConcept(term);
    }
    //This method extracts the concept surrounding the term passed in as a parameter
    //however the size of the concept is constrained using the distance value.
    public Object extractConcept(Object term, int distance){
	return ee.extractConcept(term, distance);
    }
    
    public static void main(String[] args) {
	DistrESAlgorithm distres = new DistrESAlgorithm();
	distres.performTermSemInterop("Subwoofer", "AudioSpeaker");
    } 
}