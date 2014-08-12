/*
 * Class Title:         DistrES
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.service.core_service.distres.impl;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.distres.IDistrESAlgorithm;
import NASUF.src.uk.ac.livjm.constant.DistrESConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.listener.pipe.ServicePipeMsgListenerFactory;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.service.abstract_service.impl.AbstractService;
import NASUF.src.uk.ac.livjm.protocol.data_objects.DataObjectFactory;
import NASUF.src.uk.ac.livjm.protocol.data_objects.peer_service.impl.PeerServiceDataObject;

//Log4J API imports
import org.apache.log4j.Level;

public class DistrES extends AbstractService{

    private IDistrESAlgorithm distres;
    
    //Default constructor. Need to do.
    public DistrES(IDistrESAlgorithm distres) {   
        this.PIPEADV = DistrESConstants.DISTRESPIPE;
	this.distres = distres;
        pml = ServicePipeMsgListenerFactory
                .createDistrESPipeMsgListener(distres);
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
	    pdo.setModuleClass(DistrESConstants.DISTRES_MOD);
	    pdo.setModuleSpec(DistrESConstants.DISTRES_SPEC);
	    pdo.setVersion(ServiceDescriptionConstants.VERSION);
	    pdo.setCreator(ServiceDescriptionConstants.CREATOR);
	    pdo.setPipeAdvInputStream(DistrES
		.class.getResourceAsStream(PIPEADV));
	    pdo.setImplCode("uk.ac.livjm.service.core_service.distres.impl.DistrES");
	    pdo.setCompat(group.getAllPurposePeerGroupImplAdvertisement().getCompat());
	    pdo.setImplDescription("DistrES Service");
	    pdo.setImplProvider("livjm");
            
	    //Publish Service
	    publishService(pdo);
	    
	    //Display that the service is being started.
            if(NASUFLogger.isEnabledFor(Level.INFO))
                NASUFLogger.info("Starting DistrES Service ....");
            
	    //Start the service.
            startService();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DistrES: run: " + e.toString());
        }
    }
    
    //When the service is stopped, stop the listener and set 
    //the running status to false.
    public void stopService(){
	//Stop service listener
        pml.stop();
        RUNNING = false;
	//Display that the service is stopping.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Stopping DistrES Reciever Service");
    }
    
    //Ensure that the service and its associated object usage is 
    //cleaned up
    public void cleanUp(){
        super.cleanUp();
    }
}