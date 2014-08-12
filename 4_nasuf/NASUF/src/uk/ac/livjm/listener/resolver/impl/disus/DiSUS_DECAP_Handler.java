/*
 * Class Title:         DiSUS_DECAP_Handler
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
import NASUF.src.uk.ac.livjm.constant.DECAPConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.listener.resolver.ResolverHandler;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;
import NASUF.src.uk.ac.livjm.utils.XMLUtility;

//Log4J API imports
import org.apache.log4j.Level;

public class DiSUS_DECAP_Handler extends ResolverHandler implements Runnable {
   
    //These class variables contain the service request 
    //and device capability models
    private String srDCM = "";
    private String deviceDCM = "";
    
    //This class variable contains the query message sent
    //to this device.
    private ResolverQueryMsg rqMsg;
    
    //Default constructor. This constructor takes four
    //parametes - the first parameter is the P2P interface
    //and the second parameter is the query message. The 
    //third and fourth parameter are the capability models.
    public DiSUS_DECAP_Handler(
        IDiSUS disus,
        ResolverQueryMsg msg, 
        String srDCM, 
        String deviceDCM){
	//Assign the parameters the the class variables.
        this.disus = disus;
        this.srDCM = srDCM;
        this.deviceDCM = deviceDCM;
        this.rqMsg = msg;
    }
    
    //this class is threaded consequently when the thread
    //is started this method is executed.
    public void run() {
	//Create a pipe to the DeCap service using the module
	//specification for a decap service.
        pipe = disus
            .bindToService(
                disus.discoverCoreService(
                    DECAPConstants.DCM_SPEC).toString(),
                    this);
        //If the pipe could not be created display that the 
	//device failed to connect to the required service.
        if(!pipe.isBound()){
            if(NASUFLogger.isEnabledFor(Level.INFO))
                NASUFLogger.info("Failed to Connect to Pipe");
            return;
        }
        
        //Add the servic request Device Capability Model - 
        //this model describes the capabilities a candidate
        //device must have before the service is provides can
        //be used.
        Message dcmMsg = new Message();

        dcmMsg.addMessageElement(
            ServiceDescriptionConstants.NASUF_NAMESPACE, 
                new StringMessageElement(
                    DECAPConstants.SERVICE_REQUEST_DECAP_TAG, 
                    XMLUtility.replaceAllXMLCharacters(srDCM), null));

        //Add the device's Device capability model - this model 
        //will be used by the DECAPService, which will compare the
        //service DECAP and the device DECAP to determine whether the 
        //device's capabilites are equal to or above the capability
        //requirements described in the service DECAP.
        dcmMsg.addMessageElement(
            ServiceDescriptionConstants.NASUF_NAMESPACE, 
                new StringMessageElement(
                    DECAPConstants.DEVICE_DECAP_TAG, 
                    XMLUtility.replaceAllXMLCharacters(deviceDCM), null));

	//Display that the message is being sent.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Sending DECAP Message");
        
        try{
	    //Send the message and wait for five seconds for
	    //a response and then close the pipe.
            pipe.sendMessage(dcmMsg);
            Thread.sleep(5000);
	    pipe.close();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS_DECAP_Handler: run: " + 
                    e.toString());
	    try{
		//If an error occurs ensure that the pipe
		//is closed.
		pipe.close();
	    }catch(Exception ioe){
		if(NASUFLogger.isEnabledFor(Level.ERROR))
		    NASUFLogger.error("DiSUS_DECAP_Handler: run: " + 
			ioe.toString());
	    }
        }
    }
    
    //This event recieves the decap result 
    public void pipeMsgEvent(net.jxta.pipe.PipeMsgEvent pipeMsgEvent) {
        //Create a new message object that will contain the message
	//contained in the pipeMsgEvent
        Message msg = null;
       
        //grab the message from the event
        msg = pipeMsgEvent.getMessage();
	
	//If the message is null exit the event method
        if (msg == null)
            return;
	System.out.println("I am about to set the DECAPResult");
	if(drh == null)
	    System.out.println("drh is null");
	
	//Return the result to the DiSUS_Handler
        drh.setDECAPResult(rqMsg, msg);
        
	System.out.println("I have set the DECAPResult");
        try{
	    //Close the pipe it is no longer needed.
            pipe.close();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS_DECAP_Handler: pipeMsgEvent: " + 
		    e.toString());
        }
    }
}