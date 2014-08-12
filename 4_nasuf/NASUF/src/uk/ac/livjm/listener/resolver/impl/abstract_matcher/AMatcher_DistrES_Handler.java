/*
 * Class Title:         DiSUS_DistrES_Handler
 * Class Description:   Need to do
 * Author:              Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Team Members:        Paul Fergus
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               cmppferg@livjm.ac.uk
 * Achnowledgements:    
 */
package NASUF.src.uk.ac.livjm.listener.resolver.impl.abstract_matcher;

//JXTA API imports
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.sism.abstract_matcher.IAbstractMatcher;
import NASUF.src.uk.ac.livjm.listener.resolver.ResolverHandler;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.constant.DistrESConstants;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;

//Log4J API imports
import org.apache.log4j.Level;

public class AMatcher_DistrES_Handler extends ResolverHandler implements Runnable{
   
    private IAbstractMatcher aMatcher;
    private String srTerm;
    private String spTerm;
    
    public AMatcher_DistrES_Handler(
	IDiSUS disus,
	IAbstractMatcher aMatcher, 
	String srTerm,
	String spTerm){
	this.disus = disus;
	this.aMatcher = aMatcher;
	this.srTerm = srTerm;
	this.spTerm = spTerm;
    }
    
    //this class is threaded consequently when the thread
    //is started this method is executed.
    public void run() {
        //Connect to a DistrES core service.
        pipe = disus
            .bindToService(
                disus.discoverCoreService(
                    DistrESConstants.DISTRES_SPEC).toString(),
                    this);
        //If the pipe is not bound then exit the method.
        if(!pipe.isBound()){
            if(NASUFLogger.isEnabledFor(Level.INFO)){
                NASUFLogger.info("Failed to Connect to Pipe");
            }
            return;
        }
	
	//Create a message object and add the term from the 
	//service request to it - the X Term.
        Message dcmMsg = new Message();
	
	//Add the type of message type. In this instance we are
	//trying to determine whether a term x is semantically
	//related to a term y.
	dcmMsg.addMessageElement(
	    ServiceDescriptionConstants.NASUF_NAMESPACE,
		new StringMessageElement(
		    "DistrESRequestType", "SemInterop", null));

        dcmMsg.addMessageElement(
            ServiceDescriptionConstants.NASUF_NAMESPACE, 
                new StringMessageElement(
                    DistrESConstants.X_TERM, 
			srTerm, null));

        //Add the term from the service profile - the Y Term
        dcmMsg.addMessageElement(
            ServiceDescriptionConstants.NASUF_NAMESPACE, 
                new StringMessageElement(
                    DistrESConstants.Y_TERM, 
			spTerm, null));

	//Display that the message is being sent.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Sending DistrES Message");
        
        try{
	    //Send the message and wait for five seconds for
	    //a response and then close the pipe.
            pipe.sendMessage(dcmMsg);
            Thread.sleep(10000);
	    pipe.close();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("AMatcher_DECAP_Handler: run: " + 
                    e.toString());
	    try{
		//If an error occurs ensure that the pipe
		//is closed.
		pipe.close();
	    }catch(Exception ioe){
		if(NASUFLogger.isEnabledFor(Level.ERROR))
		    NASUFLogger.error("AMatcher_DECAP_Handler: run: " + 
			ioe.toString());
	    }
        }
    }
     
     public void pipeMsgEvent(net.jxta.pipe.PipeMsgEvent pipeMsgEvent) {
        if(NASUFLogger.isEnabledFor(Level.DEBUG)){
            NASUFLogger.debug("I am in the AMatcher_DistrES_Handler pipeMsgEvent");
        }
        Message msg = null;
       
        //grab the message from the event
        msg = pipeMsgEvent.getMessage();
        if (msg == null) {
            return;
        }
        
	aMatcher.distrESResult(
	    msg.getMessageElement(
		ServiceDescriptionConstants.NASUF_NAMESPACE,
		DistrESConstants.DISTRES_SEMINTEROP_RESULT));
     }
     
     public void finalize(){
        if(NASUFLogger.isEnabledFor(Level.DEBUG)){
            NASUFLogger.debug("AMatcher_DistrES_Handler: finalize: Destroying thread ");
        }
    }
}
