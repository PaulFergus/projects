/*
 * Class Title:         DiSUS_SISM_Handler
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.listener.resolver.impl.disus;

//JXTA API imports
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.protocol.ResolverQueryMsg;

//NASUF API imports
import NASUF.src.uk.ac.livjm.listener.resolver.ResolverHandler;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.constant.SISMConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;

//Log4j API imports
import org.apache.log4j.Level;

public class DiSUS_SISM_Handler extends ResolverHandler implements Runnable{

    //This class variables contain the service request 
    //peer service capabiltiy model and the profile for
    //an application specific peer service
    private String srPSCM;
    private String profile;
    //This class variable contains the query message
    private ResolverQueryMsg rqMsg;
    
    //This variable contains the module spec for the
    //application specific peer service.
    private String moduleSpec = "";
    
    //This class variable contains the decap message.
    private Message decapMsg = null;
    
    //Default constructor. This constructor takes 
    //six parameters - the first parameter is the P2P
    //interface and the second is the message from the 
    //decap service. The third is the original query
    //message and the forth and fifth are the service
    //request peer service capabilty model and the 
    //profile for a application peer service.
    public DiSUS_SISM_Handler(
        IDiSUS disus,
        Message decapMsg,
        ResolverQueryMsg msg, 
        String srPSCM, 
        String profile, 
        String moduleSpec){
	//Assign the parameters to the class variables
        this.disus = disus;
        this.decapMsg = decapMsg;
        this.srPSCM = srPSCM;
        this.profile = profile;
        this.rqMsg = msg;
        this.moduleSpec = moduleSpec;
    }
    
    //this class is threaded consequently when the thread
    //is started this method is executed.
    public void run() {
        //Create a pipe to the DeCap service using the module
	//specification for a decap service.
        pipe = disus
            .bindToService(
                disus.discoverCoreService(
                    SISMConstants.SISM_SPEC).toString(),
                    this);
        //If the pipe could not be created display that the 
	//device failed to connect to the required service.
        if(!pipe.isBound()){
            if(NASUFLogger.isEnabledFor(Level.INFO))
                NASUFLogger.info("Failed to Connect to Pipe");
            return;
        }
        
        //Add the service request Peer Service Capability Model -
        //this model describes the IOPES a candidate service must
        //support. If a match is found then the PSCM and the service
        //provided by the device describe the same capabilities.
        Message pscmMsg = new Message();
        pscmMsg.addMessageElement(
            ServiceDescriptionConstants.NASUF_NAMESPACE,
                new StringMessageElement(
                    ServiceRequestConstants.SERVICE_REQUEST_PSCM_TAG,
                    srPSCM, null));

	//Add the profile for the application peer service - this model
	//describes the IOPES the service supports. 
        pscmMsg.addMessageElement(
            ServiceDescriptionConstants.NASUF_NAMESPACE,
                new StringMessageElement(
                    ServiceRequestConstants.PEER_SERVICE_PROFILE_TAG,
                    profile, null));
	
	//Display that DiSUS is sending the message to the SISM
	//service.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Sending SISM Message");
	
        try{
	    //Send message and wait for 5 seconds for a response
	    //and then close the pipe.
            pipe.sendMessage(pscmMsg);
            Thread.sleep(10000);
	    pipe.close();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS_SISM_Handler: run: " + 
                    e.toString());
	    try{
		//If an error occurs ensure that the pipe
		//is closed.
		pipe.close();
	    }catch(Exception ioe){
		if(NASUFLogger.isEnabledFor(Level.ERROR))
		    NASUFLogger.error("DiSUS_SISM_Handler: run: " + 
			ioe.toString());
	    }
        }
    }
    
    //This event listener processes any responses returned from the 
    //SISM service.
    public void pipeMsgEvent(net.jxta.pipe.PipeMsgEvent pipeMsgEvent) {
        //Create a new messsage that will contain the message
	//returned from the SISM service.
        Message msg = null;
       
        //grab the message from the event
        msg = pipeMsgEvent.getMessage();
        if (msg == null)
            return;
        
        try{
            //Set the SISM result in the DiSUS Handler. In the
	    //message call pass the decap message along with
	    //the original query message, the message recieved
	    //from the SISM service and the peer service module
	    //specification.
            drh.setSISMResult(
                decapMsg, 
                rqMsg,
                msg.getMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE,
                    SISMConstants.ABSTRACT_MATCHER_TAG).toString(),
                moduleSpec);
            //Close the pipe the device no longer needs it.
            pipe.close();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS_SISM_Handler: pipeMsgEvent: " + 
		    e.toString());
        }
    }
}