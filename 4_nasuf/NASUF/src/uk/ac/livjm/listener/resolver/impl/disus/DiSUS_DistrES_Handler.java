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
package NASUF.src.uk.ac.livjm.listener.resolver.impl.disus;

//JXTA API imports
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.protocol.ResolverQueryMsg;

//NASUF API imports
import NASUF.src.uk.ac.livjm.listener.resolver.ResolverHandler;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.constant.DistrESConstants;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;

//Log4J API imports
import org.apache.log4j.Level;

public class DiSUS_DistrES_Handler extends ResolverHandler implements Runnable{
   
    private String term;
    
    //This class variable contains the query message sent
    //to this device.
    private ResolverQueryMsg rqMsg;
   
    public DiSUS_DistrES_Handler(
        IDiSUS disus,
	ResolverQueryMsg msg,
        String term){
        this.disus = disus;
	this.term = term;
    }
    
    //this class is threaded consequently when the thread
    //is started this method is executed.
    public void run() {
        
        pipe = disus
            .bindToService(
                disus.discoverCoreService(
                    DistrESConstants.DISTRES_SPEC).toString(),
                    this);
        
        if(!pipe.isBound()){
            if(NASUFLogger.isEnabledFor(Level.INFO)){
                NASUFLogger.info("Failed to Connect to Pipe");
            }
            return;
        }
	
	//Add the servic request Device Capability Model - 
        //this model describes the capabilities a candidate
        //device must have before the service is provides can
        //be used.
        Message ecMsg = new Message();
	
	 
	    ecMsg.addMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    new StringMessageElement(
			"DistrESRequestType",
			"extractConcept", null));

        ecMsg.addMessageElement(
            ServiceDescriptionConstants.NASUF_NAMESPACE, 
                new StringMessageElement(
                    DistrESConstants.X_TERM, 
                    term, null));

	//Display that the message is being sent.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Sending DistrES Message");
        
        try{
	    //Send the message and wait for five seconds for
	    //a response and then close the pipe.
            pipe.sendMessage(ecMsg);
            Thread.sleep(5000);
	    pipe.close();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS_DISTRES_Handler: run: " + 
                    e.toString());
	    try{
		//If an error occurs ensure that the pipe
		//is closed.
		pipe.close();
	    }catch(Exception ioe){
		if(NASUFLogger.isEnabledFor(Level.ERROR))
		    NASUFLogger.error("DiSUS_DISTRES_Handler: run: " + 
			ioe.toString());
	    }
        }
    }
     
     public void pipeMsgEvent(net.jxta.pipe.PipeMsgEvent pipeMsgEvent) {
        if(NASUFLogger.isEnabledFor(Level.DEBUG)){
            NASUFLogger.debug("I am in the DiSUS_DistrES_Handler pipeMsgEvent");
        }
        Message msg = null;
       
        //grab the message from the event
        msg = pipeMsgEvent.getMessage();
        if (msg == null) {
            return;
        }
        
        drh.setDistrESResult(
            msg.getMessageElement(
                ServiceDescriptionConstants.NASUF_NAMESPACE,
                ServiceRequestConstants.DISTRES_RESULT).toString(),
		rqMsg);
     }
     
     public void finalize(){
        if(NASUFLogger.isEnabledFor(Level.DEBUG)){
            NASUFLogger.debug("DiSUS_DistrES_Handler: finalize: Destroying thread ");
        }
    }
}
