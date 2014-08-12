/*
 * Class Title:         MediaTransmitterPipeMsgListener
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.listener.pipe.impl.player;

//Jxta API imports
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.util.JxtaBiDiPipe;

//JMF API imports
import javax.media.MediaLocator;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.AdaptionConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.listener.pipe.ServicePipeMsgListener;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;
import NASUF.src.uk.ac.livjm.service.application_service.media_transmitter.impl.av_transmitter.AVTransmit3;

//Log4J API imports
import org.apache.log4j.Level;
    
public class MediaTransmitterPipeMsgListener extends ServicePipeMsgListener{
    
    //The variable contains the service logic for 
    //transmitting media content
    private AVTransmit3 transmit = null;
    
    //The file to be transmitted and the broadcast 
    //ip and port address are stored in the following
    //variables
    private String filename;
    private String ipAddress;
    private String port;
    
    //The device uses dependency services for processing
    //audio and video data. These variable contain the 
    //audio and video module specifications for the current
    //services being used. 
    private String audioModuleSpec;
    private String videoModuleSpec;
    
    //This variable contains the P2P interface.
    private IDiSUS disus = null;
    
    //This variable allows the device to determine what state
    //it is in. If it is running then the state variable will
    //be true otherwise it will be false. This variable is used
    //when services are dynamically registered or unregistered.
    //Depending on the state the device will re-assign a service
    //and automatically execute it or just plug it in for future
    //use.
    private boolean transmitStateVariable = false;
    
    //Default constructor. The first parameter contains 
    //the audio module specification, the second the 
    //video specification. The third contains the file
    //location to be transmitted, the fourth contains the 
    //ip address and the fifth contains the port. The ip
    //and port address are used to create a session which
    //will be used to broadcast audio and video data within
    //the network. The last parameter contains a reference 
    //to the P2P interface.
    public MediaTransmitterPipeMsgListener(
        String audioModuleSpec,
        String videoModuleSpec,
        String filename,
        String ipAddress,
        String port,
        IDiSUS disus) {
        //Assign the parameter values to the internal 
        //class variables.
        this.filename = filename;
        this.ipAddress = ipAddress;
        this.port = port;
        this.audioModuleSpec = audioModuleSpec;
        this.videoModuleSpec = videoModuleSpec;
        this.disus = disus;
        
        //Display that the service has started and is waiting for
        //incomming connections.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Player Service Started - Waiting for Connections");
    }
    
    //This getter variable returns the state the device is in
    public boolean getStateVariable(){
        return transmitStateVariable;
    }
    
    //This framework supports self-adaptive mechanisms that 
    //allows alternative services to be selected in the
    //advent of a current service failling. The method
    //takes two parameters - the first is the qos value
    //which indicates whether a better service has become
    //available or whether the next best service needs 
    //to be stated because the current service has failed.
    //The second parameter is the module specification for 
    //the new service.
    public void changeAudioService(
        String qos,
        String moduleSpec){
        
        //If the module spec is the same as we have already got 
        //then just return.
        if(moduleSpec.equalsIgnoreCase(audioModuleSpec))
            return;
        
        //Check to see if the composition is currently being executed
        //If it is then ensure that the new service is started.
        if(transmitStateVariable){
            //Check to see if the change is the result of a better 
            //service being registered. This being the case the 
            //current service must be stopped and the new service started.
            //Imagine the following scenario: if you are watching a DVD 
            //and you are using your surround sound system. For some 
            //reason your surround sound service becomes unavailable so
            //the TV speakers are automatically used as a solution, but 
            //which provides a less quality of service. Now if your 
            //surround sound service becomes available again, it is 
            //seen as a better service than your TV audio service, so
            //it stops the TV audio service and runs the surround sound
            //audio service.
            if(qos.equalsIgnoreCase(
                AdaptionConstants.REGISTER_BEST_SERVICE)){
                //Stop the current audio service
                runDependencyService("Stop", audioModuleSpec);
                //Put the thread to sleep to give the current
                //service time to be stopped.
                try{
                    Thread.sleep(2000);
                }catch(Exception e){
                    if(NASUFLogger.isEnabledFor(Level.ERROR))
                        NASUFLogger.error("MediaTransmitterPipeMsgListener: changeAudioService: " + 
                            e.toString());
                }
                //Once the thread has awaken start the new
                //service.
                runDependencyService("Listen", moduleSpec);
            }
            
            //If the changeAudioService is the result of the 
            //current service failing then just start the next
            //service. Note that we do not need to stop a 
            //current service because it has failed.
            if(qos.equalsIgnoreCase(
                AdaptionConstants.REGISTER_NEXT_BEST_SERVICE))
                runDependencyService("Listen", moduleSpec);
        }
        //Change the old audio module specification for the 
        //new module specification.
        audioModuleSpec = moduleSpec;
        //Display that the service has been successfully 
        //changed.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Successfully changed service");
    }
    
    //This framework supports self-adaptive mechanisms that 
    //allows alternative services to be selected in the
    //advent of a current service failling. The method
    //takes two parameters - the first is the qos value
    //which indicates whether a better service has become
    //available or whether the next best service needs 
    //to be stated because the current service has failed.
    //The second parameter is the module specification for 
    //the new service.
    public void changeVideoService(
        String qos,
        String moduleSpec){
            
        //If the module spec is the same as we have already got 
        //then just return.
        if(moduleSpec.equalsIgnoreCase(videoModuleSpec))
            return;
        
        //Check to see if the composition is currently being executed
        //If it is then ensure that the new service is started.
        if(transmitStateVariable){
            //Check to see if the change is the result of a better 
            //service being registered. This being the case the 
            //current service must be stopped and the new service started.
            //Imagine the following scenario: if you are watching a news 
            //report on your plazma TV. For some reason your plasma visual 
            //service becomes unavailable so the visual service on your 
            //mobile phone is automatically used as a solution, but 
            //which provides a less quality of service. Now if your 
            //plazma TV visual service becomes available again, it is 
            //seen as a better service than your mobile phone visual service,
            //so it stops the mobile phone visual service and runs the plazma
            //visual service.
            if(qos.equalsIgnoreCase(
                AdaptionConstants.REGISTER_BEST_SERVICE)){
                //Stop the current video service
                runDependencyService("Stop", videoModuleSpec);
                //Put the thread to sleep to give the current
                //service time to be stopped.
                try{
                    Thread.sleep(2000);
                }catch(Exception e){
                    if(NASUFLogger.isEnabledFor(Level.ERROR))
                        NASUFLogger.error("MediaTransmitterPipeMsgListener: videoAudioService: " + 
                            e.toString());
                }
                //Once the thread has awaken start the new
                //service.
                runDependencyService("Listen", moduleSpec);
            }
            //If the changeVideoService is the result of the 
            //current service failing then just start the next
            //service. Note that we do not need to stop a 
            //current service because it has failed.
            if(qos.equalsIgnoreCase(
                AdaptionConstants.REGISTER_NEXT_BEST_SERVICE))
                runDependencyService("Listen", moduleSpec);
        }
        //Change the old video module specification for the 
        //new module specification.
        videoModuleSpec = moduleSpec;
        //Display that the service has been successfully 
        //changed.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Successfully changed service");
    }
    
    //This method starts all the dependency services used
    //by the device.
    private void runDependencyServices(){
        //Start the dependency services.
        runDependencyService("Listen", audioModuleSpec);
        runDependencyService("Listen", videoModuleSpec);
    }
    
    //This method runs a dependency service. The method
    //takes two parameters - the first parameter is the 
    //command to be submitted to the dependency service.
    //The second paramter is the module specification 
    //used to connect to the dependency service.
    private void runDependencyService(
        String command,
        String moduleSpec){
        
        //This variable is the pipe used to connect to the 
        //dependency service.
        JxtaBiDiPipe pipe = null;
        try{
            //Using the module specification and this class
            //as the listener, try and bind to the dependency
            //service
            pipe = disus.bindToService(
                moduleSpec, (PipeMsgListener)this);
            
            //First check that the pipe is not null.
            if(pipe != null){
                //If the pipe is not null check to see if it has a 
                //binding before a command is sent.
                if(pipe.isBound()){
                    //Display that the command is being sent
                    if(NASUFLogger.isEnabledFor(Level.DEBUG)){
                        NASUFLogger.debug("Sending Command: " + command);
                    }
                    //Using the P2P interface send the command to the 
                    //dependency service.
                    disus.sendCommand(
                        command,pipe);
                }
                //Check to see if the pipe is open about bound
                //if so close the pipe the device no longer needs
                //it
                if(pipe.isBound())
                    pipe.close();
            }
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("MediaTransmitterPipeMsgListener: runDependencyService: " + 
                    e.toString());
        }
    }
    
    //This method is used to transmit the multimedia content 
    //whether it is audio and/or video data.
    public void transmitMedia(){
        //Run Dependecy Services that will process the 
        //multimedia content
        runDependencyServices();
        
        //Begin transmitting the media.First ensure that
        //the service is not running.
        if(transmit == null){
            transmit = new AVTransmit3(
                new MediaLocator(filename),
                    ipAddress, port, null, this);
            //Start transmitting the movie
            transmit.start();
        }else{
            //Start transmitting the movie
            transmit.start();
        }
        //Update the state variable to indicate that the
        //service is running.
        transmitStateVariable = true;
    }
    
    //This method is used to reset the state of the device.
    //This method will be called if either the service is 
    //stopped or the multimedia content has finished 
    //transmission.
    public void resetState(){
        transmitStateVariable = false;
    }
    
    //This method is used to stop the service.
    public void stop(){
        //Check that the service is not null. If it isn't
        //then stop the service, reset the state and 
        //null the object because it is no longer needed.
        if(transmit != null){
            transmit.stop();
            resetState();
            transmit = null;
        }
    }
    
    //This event listener is used to control the device. It allows
    //the device and or its services to be stopped or started. 
    public void pipeMsgEvent(net.jxta.pipe.PipeMsgEvent pipeMsgEvent) {
        //Create a new message object, which will be used
        //to store the message extracted from the event.
        Message msg = null;
       
        try {
            //Grab the message from the event
            msg = pipeMsgEvent.getMessage();
            
            //If the message is null then exit the event.
            if(msg == null)
                return;
            
            //Extract the command from the message.
            MessageElement msgCommand = 
                msg.getMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    "Command");
            
            //If the command is play then start transmitting the 
            //multimedia content.
            if(msgCommand.toString().equalsIgnoreCase("Play")){
                //Display that the service is running
                if(NASUFLogger.isEnabledFor(Level.INFO))
                    NASUFLogger.info("Playing the Media ...");
                
                //Check that the servic is not first running
                //This being the case start transmitting the data.
                if(!this.getStateVariable())
                    transmitMedia();
            //If the command is stop then stop transmitting the
            //multimedia content.
            }else if(msgCommand.toString().equalsIgnoreCase("Stop")){
                //Display that the servic is being stopped.
                if(NASUFLogger.isEnabledFor(Level.INFO))
                    NASUFLogger.info("Stopping the Media ...");
                
                //First check that the service is running. If it is
                //then stop the service.
                if(this.getStateVariable())
                    stop();
            }else{
                if(NASUFLogger.isEnabledFor(Level.INFO))
                    NASUFLogger.info("Command not recognised");
            }  
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DeCapPipeMessageListener Error: " + 
		    toString());
        }
    }
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        super.cleanUp();
        transmit = null;
        filename = "";
        ipAddress = "";
        port = "";
        audioModuleSpec = null;
        videoModuleSpec = null;
    }
}