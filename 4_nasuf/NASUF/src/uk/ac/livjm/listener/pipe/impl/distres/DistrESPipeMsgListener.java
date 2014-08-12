/*
 * Class Title:         DistrES
 * Class Description:   Need to do
 * Author:              Sun Microsystems
 * Project:             Wireless PnP Home Network
 * Team Members:        Paul Fergus
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Date:                20-08-03
 * Email:               cmppferg@livjm.ac.uk
 * Achnowledgements:    
 */
package NASUF.src.uk.ac.livjm.listener.pipe.impl.distres;

//JXTA SDK imports
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.util.JxtaBiDiPipe;
import net.jxta.endpoint.StringMessageElement;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.distres.IDistrESAlgorithm;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.listener.pipe.ServicePipeMsgListener;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.constant.DistrESConstants;


//Log4J API imports
import org.apache.log4j.Level;

public class DistrESPipeMsgListener extends ServicePipeMsgListener{
    
    private IDistrESAlgorithm distres = null;
    
    public DistrESPipeMsgListener(IDistrESAlgorithm distres) {
        if(NASUFLogger.isEnabledFor(Level.INFO)){
            NASUFLogger.info("DistrES Service Started - Waiting for Connections");
        }
	this.distres = distres;
    }
    
    public void pipeMsgEvent(net.jxta.pipe.PipeMsgEvent pipeMsgEvent) {
        if(NASUFLogger.isEnabledFor(Level.DEBUG)){
            NASUFLogger.debug("I am in the DistrESPipeMsgListener pipeMsgEvent");
        }
        
	Message msg;
	//grab the message from the event and assign it to our 
        //message object
        msg = pipeMsgEvent.getMessage();
            
        //If the message object is null then just
        //exit the event procedure.
        if (msg == null)
	    return;
            
        //Get the service request device capability model
        MessageElement msgRequestType = 
	    msg.getMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    "DistrESRequestType");
	
	//If any of these models are null then exit the 
        //event procedure.
        if (msgRequestType == null) {
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("null msg received");    
	    return;
	}
	
	MessageElement msgXTerm = null;
	MessageElement msgYTerm = null;
	
	if(msgRequestType.toString().equalsIgnoreCase("SemInterop")){
	    //Do something
	    //Get the device capability model
            msgXTerm = 
                msg.getMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    DistrESConstants.X_TERM);
	    
	    //Get the device capability model
            msgYTerm = 
                msg.getMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    DistrESConstants.Y_TERM);
	
            
	    //If any of these models are null then exit the 
	    //event procedure.
	    if ((msgXTerm == null)
		|| (msgYTerm == null)) {
		if(NASUFLogger.isEnabledFor(Level.ERROR))
		    NASUFLogger.error("null msg received");
		    return;
	    }

	    try{
		//Call the Term Semantic Interopability method.
		pipe.sendMessage(
		(Message) distres
		    .performTermSemInterop(
			msgXTerm.toString(), 
			msgYTerm.toString()));
	    }catch(Exception e){
		if(NASUFLogger.isEnabledFor(Level.ERROR)){
		    NASUFLogger.error("DistrESPipeMessageListener Error: " + toString());
		}
	    }
	}else if(msgRequestType.toString().equalsIgnoreCase("extractConcept")){
	    //Do something
	    //Get the device capability model
            msgXTerm = 
                msg.getMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    DistrESConstants.X_TERM);
	    try{
		pipe.sendMessage(
		    (Message) distres
			.extractConcept(msgXTerm.toString()));
	    }catch(Exception e){
		if(NASUFLogger.isEnabledFor(Level.ERROR)){
		    NASUFLogger.error("DistrESPipeMessageListener Error: " + toString());
		}
	    }
	}
    }
    
    public void cleanUp(){
        //There is nothing to clean up in this class
        //Just clean up the parent class
        super.cleanUp();
    }
}