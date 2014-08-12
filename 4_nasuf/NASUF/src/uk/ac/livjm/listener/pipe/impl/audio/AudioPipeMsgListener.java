/*
 * Class Title:         AudioPipeMsgListener
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.listener.pipe.impl.audio;

//Java SDK imports
import java.net.Inet4Address;

//JXTA API imports
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.listener.pipe.ServicePipeMsgListener;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.service.application_service.media_reciever.impl.reciever.AVReceive3;

//Log4J API imports
import org.apache.log4j.Level;
    
public class AudioPipeMsgListener extends ServicePipeMsgListener{
    
    //The service logic
    private AVReceive3 avReceive;
    
    //This variable stores the port address used to listen
    //for data on.
    private String port = "";
    
    //This variable allows the device to determine what state
    //it is in. If it is running then the state variable will
    //be true otherwise it will be false. This variable is used
    //when services are dynamically registered or unregistered.
    //Depending on the state the device will re-assign a service
    //and automatically execute it or just plug it in for future
    //use.
    private boolean recieveStateVariable = false; 
    
    //Default constructor. This constructor takes one 
    //parameter, which is the port address the audio 
    //device listens for data on.
    public AudioPipeMsgListener(String port) {
        this.port = port;
        //Display that the audio service has been started
        //and waiting for connections.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Audio Service Started - Waiting for Connections");
    }
    
    //This getter method returns the state variable for the audio
    //device.
    public boolean getStateVariable(){
        return recieveStateVariable;
    }
    
    //This method creates the audio service object, which begins
    //listening for data on some pre-determined endpoint.
    public void recieveMedia(
        String ipAddress,
        String portAddress){
        String session[] = {""};
        session[0] = new String("");
        try{
            session[0] = ipAddress + "/" + portAddress;
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("AudioPipeMsgListener: recieveMedia: " + 
                    e.toString());
        }
        //If the reciever service has not been started then
        //start the service
        if(avReceive == null){
	    try{
		//Create a new instance of the audio resiever service
		avReceive = new AVReceive3(session);
		//If the service does not successfully start then
		//exit the application.
		if (!avReceive.initialize()) {
		    if(NASUFLogger.isEnabledFor(Level.ERROR))
			NASUFLogger.error("Failed to initialize the sessions.");
		    return;
		}
	    }catch(Exception e){
		if(NASUFLogger.isEnabledFor(Level.ERROR))
		    NASUFLogger.error("AudioPipeMsgListener: recieveMedia: " 
			+ e.toString());
		return;
	    }
            
	    //Update the state variable to indicate that the 
	    //service is running.
	    recieveStateVariable = true;
		    
            // Check to see if AVReceive3 is done.
            try {
                while (!avReceive.isDone())
                    //Wait for one second at a time before checking that
                    //the data has finished being transmitted.
                    Thread.sleep(1000);
                //Close the audio reciever service and destroy the instance
                avReceive.close();
                avReceive = null;
                //Reset the state variable to indicate that the servcie
                //is no longer running.
                recieveStateVariable = false;
            } catch (Exception e) {
                if(NASUFLogger.isEnabledFor(Level.ERROR)){
                    NASUFLogger.error("AudioPipeMsgListener: recieveMedia: " + 
                        e.toString());
                }
            }
        }
    }
    //When the device is stopped make sure that all the 
    //objects used by the device are stopped and reset.
    public void stop(){
        if(avReceive != null)
            avReceive.close();
        recieveStateVariable = false;
    }
    
    //This is the listener event that recieves commands sent to the 
    //audio service. This event acts as the control entry point.
    public void pipeMsgEvent(net.jxta.pipe.PipeMsgEvent pipeMsgEvent) {
        //Create a new message
        Message msg = null;
        
        try {
            //grab the message from the event and assign it to our
            //message
            msg = pipeMsgEvent.getMessage();
            
            //if the message is null then just return.
            if (msg == null)
                return;
            
            //Extract the command from the message.
            MessageElement msgCommand = 
                msg.getMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    "Command");
            
            //If the command is listen then instruct the service 
            //to start listening for audio data.
            if(msgCommand.toString().equalsIgnoreCase("Listen")){
                if(NASUFLogger.isEnabledFor(Level.INFO))
                    NASUFLogger.info("Listening for media stream ...");
                
                //First check to see that the service is not already 
                //listening - if it isn't then start listening.
                if(!this.getStateVariable())
                    //Start listening for audio data.
                    recieveMedia(
                        Inet4Address.getLocalHost().getHostAddress(),
                        port);
            //If the command is Stop then instruct the service 
            //to stop listening for audio data.
            }else if(msgCommand.toString().equalsIgnoreCase("Stop")){
                if(NASUFLogger.isEnabledFor(Level.INFO))
                    NASUFLogger.info("Stopping listening to media stream ...");
                
                //First check o see if the audio device is listening for
                //audio data - if it is then stop the service.
                if(this.getStateVariable())
                    stop();
            }
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("AudioPipeMsgListener Error: " + 
		    e.toString());
        }
    }
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        super.cleanUp();
        avReceive.close();
        avReceive = null;
        port = "";
    }
}