/*
 * Class Title:         SISM
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.service.core_service.sism.impl;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.sism.abstract_matcher.IAbstractMatcher;
import NASUF.src.uk.ac.livjm.algorithm.sism.concrete_matcher.IConcreteMatcher;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.constant.SISMConstants;
import NASUF.src.uk.ac.livjm.listener.pipe.ServicePipeMsgListenerFactory;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.protocol.data_objects.DataObjectFactory;
import NASUF.src.uk.ac.livjm.protocol.data_objects.peer_service.impl.PeerServiceDataObject;
import NASUF.src.uk.ac.livjm.service.abstract_service.impl.AbstractService;

//Log4J API imports
import org.apache.log4j.Level;

public class SISM extends AbstractService{
    
    //These variables contain references to the 
    //abstract and concrete matcher algorithms used
    //by SISM. The variables are interfaces, consequently
    //any abstract or concrete matcher algorithm can be
    //used as long as the IAbstractMatcher and 
    //IConcreteMatcher interfaces are implemented.
    private IAbstractMatcher abstractMatcher = null;
    private IConcreteMatcher concreteMatcher = null;

    //Default constructor. This constructor takes two 
    //parameters - the first parameter is the abstract
    //matcher algorithm used and the second is the 
    //concrete matcher algorithm used.
    public SISM(
        IAbstractMatcher abstractMatcher,
        IConcreteMatcher concreteMatcher) {   
	//Assign the parameters to the class variables.
        this.abstractMatcher = abstractMatcher;
        this.concreteMatcher = concreteMatcher;
	//Get the pipe advertisement.
        this.PIPEADV = SISMConstants.SISMPIPE;
	//Register a listener for this service.
        pml = ServicePipeMsgListenerFactory
            .createSISMPipeMsgListener(
                abstractMatcher,
                concreteMatcher);
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
	    pdo.setModuleClass(SISMConstants.SISM_MOD);
	    pdo.setModuleSpec(SISMConstants.SISM_SPEC);
	    pdo.setVersion(ServiceDescriptionConstants.VERSION);
	    pdo.setCreator(ServiceDescriptionConstants.CREATOR);
	    pdo.setPipeAdvInputStream(SISM
		.class.getResourceAsStream(PIPEADV));
	    pdo.setImplCode("uk.ac.livjm.service.core_service.sism.impl.SISM");
	    pdo.setCompat(group.getAllPurposePeerGroupImplAdvertisement().getCompat());
	    pdo.setImplDescription("SISM Service");
	    pdo.setImplProvider("livjm");
	    
	    //Publish the service
	    publishService(pdo);
	    
            //Display that the service is being started.
            if(NASUFLogger.isEnabledFor(Level.INFO))
                NASUFLogger.info("Starting SISM Service ....");
            
	    //Start service.
            startService();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("SISM: run: " + 
		    e.toString());
        }
    }
    
    //When the service is stopped, stop the listener and set 
    //the running status to false.
    public void stopService(){
	//Stop the service listener.
        pml.stop();
        RUNNING = false;
	//Display tha the service is being stopped.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Stopping SISM Reciever Service");
    }
    
    //Ensure that the service and its associated object usage is 
    //cleaned up
    public void cleanUp(){
        super.cleanUp();
        abstractMatcher.cleanUp();
        abstractMatcher = null;
        concreteMatcher.cleanUp();
        concreteMatcher = null;
    }
}