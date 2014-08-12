/*
 * Class Title:         DeCap
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.service.core_service.decap.impl;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.decap.IDECAPAlgorithm;
import NASUF.src.uk.ac.livjm.constant.DECAPConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.listener.pipe.ServicePipeMsgListenerFactory;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.protocol.data_objects.DataObjectFactory;
import NASUF.src.uk.ac.livjm.protocol.data_objects.peer_service.impl.PeerServiceDataObject;
import NASUF.src.uk.ac.livjm.service.abstract_service.impl.AbstractService;

//Log4J API imports
import org.apache.log4j.Level;

public class DeCap extends AbstractService{
    
    //This variable holds a reference to the DeCap algorithm
    //being used. This is an inteface reference, consequently
    //any DeCap algorithm can be used as long as the 
    //IDECAPAlgorithm is implemented.
    private IDECAPAlgorithm dcmAlgorithm = null;
    
    //Default Constructor. This constructor takes one
    //parameter, which is the dcm algorithm being used.
    public DeCap(IDECAPAlgorithm algorithm) {   
	//Get the pipe 
        this.PIPEADV = DECAPConstants.DECAPPIPE;
	//Set the service listener for this service.
        pml = ServicePipeMsgListenerFactory
            .createDeCapPipeMsgListener(
                algorithm);
	//Set the decap algorithm being used.
        this.setAlgorithm(algorithm);
    }
    
    //This setter sets the decap algorithm being used.
    public void setAlgorithm(IDECAPAlgorithm algorithm){
        this.dcmAlgorithm = algorithm;
    }
    
    //This getter returns the decap algorithm being 
    //used.
    public IDECAPAlgorithm getAlgorithm(){
        return this.dcmAlgorithm;
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
	    pdo.setModuleClass(DECAPConstants.DCM_MOD);
	    pdo.setModuleSpec(DECAPConstants.DCM_SPEC);
	    pdo.setVersion(ServiceDescriptionConstants.VERSION);
	    pdo.setCreator(ServiceDescriptionConstants.CREATOR);
	    pdo.setPipeAdvInputStream(DeCap
		.class.getResourceAsStream(PIPEADV));
	    pdo.setImplCode("uk.ac.livjm.service.core_service.decap.impl.DeCap");
	    pdo.setCompat(group.getAllPurposePeerGroupImplAdvertisement().getCompat());
	    pdo.setImplDescription("DeCap Service");
	    pdo.setImplProvider("livjm");
            
	    //Publish service
	    publishService(pdo);
	    
	    //Display that the service is being started.
            if(NASUFLogger.isEnabledFor(Level.INFO))
                NASUFLogger.info("Starting DeCaP Service ....");
            
	    //Start the service.
            startService();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DeCap: run: " + 
		    e.toString());
        }
    }
    
    //When the service is stopped, stop the listener and set 
    //the running status to false.
    public void stopService(){
	//Stop the service listener
        pml.stop();
        RUNNING = false;
	//Display that the service is being stopped.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Stopping DeCap Service");
    }
    
    //Ensure that the service and its associated object usage is 
    //cleaned up
    public void cleanUp(){
        super.cleanUp();
        dcmAlgorithm.cleanUp();
        dcmAlgorithm = null;
    }   
}