/*
 * Class Title:         AudioRecieverService
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.service.application_service.media_reciever.impl.audio_reciever;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.impl.ApplicationPSAdvConstants;
import NASUF.src.uk.ac.livjm.constant.impl.ApplicationPSPipeAdvertisementNames;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.listener.pipe.ServicePipeMsgListenerFactory;
import NASUF.src.uk.ac.livjm.protocol.data_objects.DataObjectFactory;
import NASUF.src.uk.ac.livjm.protocol.data_objects.peer_service.impl.PeerServiceDataObject;
import NASUF.src.uk.ac.livjm.service.abstract_service.impl.AbstractService;

//Log4J API imports
import org.apache.log4j.Level;

public class AudioRecieverService extends AbstractService{
 
    //This variable contains the service type.
    private String type;
   
    //Default constructor. This constructor takes three parameters
    //- the first parameter is the port address the service recieves
    //data on. The second service is the service type and the third
    //is the service description.
    public AudioRecieverService(String port, String type, String sd) {   
	//Get the service pipe advertisement name.
        this.PIPEADV = ApplicationPSPipeAdvertisementNames.AudioRecieverPipe;
	//Create the service listener.
        pml = ServicePipeMsgListenerFactory
            .createAudioPipeMsgListener(port);
	//Set the service type and the service description.
        this.type = type;
        this.sd = sd;
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
	    pdo.setModuleClass(ApplicationPSAdvConstants.AUDIO_RECIEVER_MOD);
	    pdo.setModuleSpec(ApplicationPSAdvConstants.AUDIO_RECIEVER_SPEC);
	    pdo.setVersion(ServiceDescriptionConstants.VERSION);
	    pdo.setCreator(ServiceDescriptionConstants.CREATOR);
	    pdo.setPipeAdvInputStream(AudioRecieverService
		.class.getResourceAsStream(PIPEADV));
	    pdo.setImplCode("uk.ac.livjm.service.application_service.media_reciever.impl.audio_reciever.AudioRecieverService");
	    pdo.setCompat(group.getAllPurposePeerGroupImplAdvertisement().getCompat());
	    pdo.setImplDescription("Audio Reciever Service");
	    pdo.setImplProvider("livjm");
	    
	    //Publish the service.
            publishService(pdo);
                
            //Display that the service is being started.
	    if(NASUFLogger.isEnabledFor(Level.INFO))
                NASUFLogger.info("Starting Media Reciever Service ....");
            //Start service.
            startService();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("AudioRecieverService: run: " + 
		    e.toString());
        }
    }
    
    //When the service is stopped, stop the listener and set 
    //the running status to false.
    public void stopService(){
        pml.stop();
        RUNNING = false;
	//Display that the service is being stopped.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Stopping Audio Reciever Service...");
    }
    
    //Ensure that the service and its associated object usage is 
    public void cleanUp(){
        super.cleanUp();
        type = "";
    }
}