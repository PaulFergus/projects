/*
 * Class Title:         MediaTransmitterService
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.service.application_service.media_transmitter.impl;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.impl.ApplicationPSAdvConstants;
import NASUF.src.uk.ac.livjm.constant.impl.ApplicationPSPipeAdvertisementNames;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.listener.pipe.impl.player.MediaTransmitterPipeMsgListener;
import NASUF.src.uk.ac.livjm.listener.pipe.IServicePipeMsgListener;
import NASUF.src.uk.ac.livjm.listener.pipe.ServicePipeMsgListenerFactory;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;
import NASUF.src.uk.ac.livjm.protocol.data_objects.DataObjectFactory;
import NASUF.src.uk.ac.livjm.protocol.data_objects.peer_service.impl.PeerServiceDataObject;
import NASUF.src.uk.ac.livjm.service.abstract_service.impl.AbstractService;

//Log4J API imports
import org.apache.log4j.Level;

public class MediaTransmitterService extends AbstractService{
    
    //Default constructor. This constructor takes seven parameters
    // - the first and second parameters and the audio and video
    //pipes used by this device. The second parameter is the 
    //filename of the multimedia content to be distributed. The
    //forth and fifth parameters comprise the endpoint address
    //and the six parameter is the service desription. The last
    //parameter is the p2p interface.
    public MediaTransmitterService(
        String audioPipe,
        String videoPipe,
        String filename,
        String ipAddress,
        String port,
        String sd,
        IDiSUS disus) {   
        this.PIPEADV = ApplicationPSPipeAdvertisementNames.MediaTransmitterPipe;
        this.sd = sd;
        pml = ServicePipeMsgListenerFactory
            .createPlayerPipeMsgListener(
                audioPipe,
                videoPipe, 
                filename,
                ipAddress,
                port,
                disus);
    }
    
    //This is a threaded service, consequently the run methods
    //is the entry point.
    public void run(){  
        try{
	    //Create a new peer service data object. This object contains
	    //all the required information to publish the service.
	    PeerServiceDataObject pdo = 
		(PeerServiceDataObject)DataObjectFactory
		    .createPeerServiceDataObject();
	    
	    //Set the data object properties.
	    pdo.setModuleClass(ApplicationPSAdvConstants.TRANSMITTER_MOD);
	    pdo.setModuleSpec(ApplicationPSAdvConstants.TRANSMITTER_SPEC);
	    pdo.setVersion(ServiceDescriptionConstants.VERSION);
	    pdo.setCreator(ServiceDescriptionConstants.CREATOR);
	    pdo.setPipeAdvInputStream(MediaTransmitterService
		.class.getResourceAsStream(PIPEADV));
	    pdo.setImplCode("uk.ac.livjm.service.application_service.media_transmitter.impl.MediaTransmitterService");
	    pdo.setCompat(group.getAllPurposePeerGroupImplAdvertisement().getCompat());
	    pdo.setImplDescription("Media Transmitter Service");
	    pdo.setImplProvider("livjm");
	    
	    //Publish the service
	    publishService(pdo);
	    
	    //Display that the service is being started.
            if(NASUFLogger.isEnabledFor(Level.INFO))
                NASUFLogger.info("Starting Media Transmitter Service ....");
        
	    //Start the service.
            startService();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("MediaTransmitterService: run: " + 
		    e.toString());
        }
    }
    
    //When the service is stopped, stop the listener and set 
    //the running status to false.
    public void stopService(){
	//Stop the servcie listener.
	pml.stop();
        RUNNING = false;
	//Display that the service is being stopped.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Stopping  Media Transmitter Service");
    }
    
    //This call-back method is triggered when a current audio service 
    //being used by this device is to be changed. Dependending on 
    //the reason for change the service may be changed to the next
    //best or to a better service that was previously unavailable,
    //which has now become available again.
    public void changeAudioService(String qos,
        String moduleSpec){
	//Change the audio service being used.
        ((MediaTransmitterPipeMsgListener)pml)
            .changeAudioService(qos, moduleSpec);
    }
    
    //This call-back method is triggered when a current video service 
    //being used by this device is to be changed. Dependending on 
    //the reason for change the service may be changed to the next
    //best or to a better service that was previously unavailable,
    //which has now become available again.
    public void changeVideoService(String qos,
        String moduleSpec){
	//Change the video service being used.
        ((MediaTransmitterPipeMsgListener)pml)
            .changeVideoService(qos, moduleSpec);
    }
    
    //Ensure that the service and its associated object usage is 
    //cleaned up
    public void cleanUp(){
        super.cleanUp();
    }
}