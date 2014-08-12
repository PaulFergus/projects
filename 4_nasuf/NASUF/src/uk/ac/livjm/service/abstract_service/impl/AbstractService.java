/*
 * Class Title:         AbstractService
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.service.abstract_service.impl;

//Java SDK imports
import java.io.InputStream;
import java.io.IOException;
import java.net.SocketException;

//JXTA API imports
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.ModuleClassID;
import net.jxta.platform.ModuleSpecID;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaServerPipe;
import net.jxta.util.JxtaBiDiPipe;

//NASUF API imports
import NASUF.src.uk.ac.livjm.listener.pipe.IServicePipeMsgListener;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.protocol.data_objects.peer_service.impl.PeerServiceDataObject;
import NASUF.src.uk.ac.livjm.service.abstract_service.IAbstractService;

//Log4J API imports
import org.apache.log4j.Level;

public abstract class AbstractService 
    implements IAbstractService, Runnable {
   
    //This variable is used to determine whether the 
    //service is running.
    protected boolean RUNNING;
    //This variable contains a reference to the listener
    //for the service.
    protected IServicePipeMsgListener pml;
    //This is a bidirectional pipe for service communications
    protected JxtaServerPipe jxtaServerPipe;
    //These varaibles contain the advertisements used
    //to publish the service. 
    private ModuleClassAdvertisement mcadv;
    private ModuleSpecAdvertisement mdadv ;
    private ModuleImplAdvertisement miadv;
    //This variable contains the peer id.
    protected PeerID peerID;
    //Each search refrences one peer group conseuqntly
    //this static variable contains a reference to the 
    //group the service belongs to.
    protected static PeerGroup group;
    protected static DiscoveryService discoSvc;
    //These variables contain the pipe advertisement 
    //and the service desription.
    protected String PIPEADV;
    protected String sd;
    
    //This setter method sets the peer group the service 
    //belongs to.
    public static void setPeerGroup(PeerGroup peerGroup){
        if(group != null)
            group = null;
        group = peerGroup;
        discoSvc = group.getDiscoveryService();
    }
    
    //This getter returns the peer group reference.
    public static PeerGroup getPeerGroup(){
        return group;
    }
    
    //This getter returns the discovery service used by the
    //service.
    public static DiscoveryService getDiscoveryService(){
        return discoSvc;
    }
    
    //This getter returns the service description.
    public Object getServiceDescription(){
        return sd;
    }
    
    //This getter returns the module class advertisement
    //for the service.
    public ModuleClassAdvertisement getModuleClassAdvertisement(){
        return mcadv;
    }
    
    public ModuleSpecAdvertisement getModuleSpecAdvertisement(){
        return mdadv;
    }
    
    //This getter returns the implementation advertisement for
    //the service. 
    public ModuleImplAdvertisement getModuleImplAdvertisement() {
        return miadv;
    }
    
    //Advertisements need to be flushed from time to time so 
    //this method allows individual services advertisements 
    //to be flushed from the cache. It takes three parameters
    //- the first parameter is the advertisment id, the second
    //is the advertisment type and the last is the discovery 
    //service, which flushes the advertisement.
    private void flushAdvertisements(
            String id, 
            int advType,
            DiscoveryService discoSvc){
        try{
	    //Flush the advertisements.
            discoSvc.flushAdvertisements(id, advType);
        }catch(IOException ioe){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("AbstractService: flushAdvertisements: " +
		    ioe.toString());
        }
    }
   
    private void publish(Advertisement adv){
	//Try three times to publish the advertisment. If an
	//exeption is raised simply try again. I may need
	//to look at this code later on to see why the 
	//exception is being thrown.
	for(int i = 0; i < 3; i++){
	    try{	
		discoSvc.publish(adv);
		//Publish the advertisement remotely
		discoSvc.remotePublish(adv);
		break;
	    }catch(Exception e){
		//I was having a problem with publishing.
		//For some reason an exection is be raised
		//which I think is related to concurrent
		//modification of the cache. However I am 
		//not sure at this moment. Adding this 
		//loop solves this problem. When an error
		//is raised simply try again.
		continue;
	    }
	}
    }
    //This method publishes the module class advertisement. It takes
    //four parameters - the first parameter is the module class id
    //and the second is the name of the module class. The third is
    //the service desription, and the forth is the discovery service
    //used to publish the advertisement.
    private void publishModuleClassAdvertisement(
                ModuleClassID mcID,
                String Name,
                String description,
                DiscoveryService discoSvc){
		    
	mcadv = null;
	
	//Create a module class advertisement.
	mcadv = (ModuleClassAdvertisement)
	    AdvertisementFactory.newAdvertisement(
		ModuleClassAdvertisement.getAdvertisementType());
		
	//Set the advertisement name
	mcadv.setName(Name);
	//Set the description
	mcadv.setDescription(description);
	//Set the id
	mcadv.setModuleClassID(mcID);
        
	//Publish the service locally and remotely
	publish(mcadv);
    }
    
    //This method publishes the module spec advertisement. It takes
    //seven parameters - the first parameter is the module class id
    //and the second is the pipe advertisement and the third is the
    //name of the module spec. The fourth is is the version number
    //and the firth is the creator of the service. This sixth is the
    //module spec id and the last one is the discovery service
    //used to publish the advertisement.
    private void publishModuleSpecAdvertisement(
            ModuleClassID mcID,
            PipeAdvertisement pipeadv,
            String name,
            String version,
            String creator,
            ModuleSpecID id,
            DiscoveryService discoSvc){
		
	//Create Module Specification Advertisement using the 
	//Advertisement Factory.
	mdadv = null;
	mdadv = (ModuleSpecAdvertisement)
	    AdvertisementFactory.newAdvertisement(
		ModuleSpecAdvertisement.getAdvertisementType());

	//Set Module Specification properties.        
	mdadv.setName(name);
	mdadv.setVersion(version);
	mdadv.setCreator(creator);
	mdadv.setModuleSpecID(id);
	mdadv.setPipeAdvertisement(pipeadv);

	//Create a structured document
	StructuredDocument param 
	    = StructuredDocumentFactory
		.newStructuredDocument( 
		    new MimeMediaType( "text/xml" ), "param" );

	//Add the peer id to the document.
	TextElement elem 
	    = (TextElement)param
		.createElement(
		    "PeerID", 
		    group.getPeerID()
			.toString() );
			
//	elem.appendChild(
//	    (TextElement)param
//		.createElement("Test", "Value"));
	
	param.appendChild(elem);
	//Add the document to the module specification 
	//advertisement.
	
	
	mdadv.setParam(param);

	//Publish the service locally and remotely
	publish(mdadv);
    }
    
    //TODO: What I still need to do is implement the uri so that 
    //the service can be discovered and downloaded. I need to work
    //out how this is achieved. I the ModuleImpl can be used to
    //point to a jar file that can be discovered and downloaded.
    //This method publishes the module impl. advertisement. It 
    //takes six parameters - the first parameter is the code, the 
    //second is the compat and the third is the description. The 
    //forth is the provider information and the fifth is the 
    //module specification the impl. belongs to. The last parameter
    //is the discovery service used to publish the advertisement.
    private void publishModuleImplAdvertisement(
        String code,
        Element compat,
        String description,
        String provider,
        ModuleSpecID id,
        DiscoveryService discoSvc){
        
	//Create a new module implementation advertisement.
	miadv = null;
	miadv = (ModuleImplAdvertisement)
	    AdvertisementFactory.newAdvertisement(
		ModuleImplAdvertisement.getAdvertisementType());
	//Set the advertisement properties.
	miadv.setCode(code);
	miadv.setCompat(compat);
	miadv.setDescription(description);
	miadv.setModuleSpecID(id);
	miadv.setProvider(provider);

	//Create a new structured document.
	StructuredDocument param 
	    = StructuredDocumentFactory
		.newStructuredDocument( 
		    new MimeMediaType( "text/xml" ), "param" );

	//Add the peer id to the document.
	TextElement elem 
	    = (TextElement)param
		.createElement(
		    "PeerID", 
		    group.getPeerID()
			.toString() );

	//Add the peer id to the advertisement.
	param.appendChild(elem);
	miadv.setParam(param);

	//Publish the service locally and remotely
	publish(miadv);
    }
    
    //This method reads the pipe advertisement from the input stream
    //parameter passed in.
    private PipeAdvertisement readPipeAdv(
            InputStream is){
	//Create a new pipe advertisement object.
        PipeAdvertisement pipeadv = null;
        try{
	    //Read in the pipe advertisement.
            pipeadv = (PipeAdvertisement)
                AdvertisementFactory.newAdvertisement(
                    MimeMediaType.XMLUTF8, is);
	    //Close the input stream it is no longer needed.
            is.close();
	    //return the pipe advertisement.
            return pipeadv;
        }catch (IOException e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("AbsractService; readPipeAdv: " + 
		    e.toString());
            return null;
        }
    }
    
    //This method adds a service to the service manager.
    public void publishService(Object service){
	PeerServiceDataObject pdo = 
	    (PeerServiceDataObject)service;
	try{
	    //Create the module class id.
            ModuleClassID mcID = IDFactory.newModuleClassID();
            publishModuleClassAdvertisement(mcID, 
                    pdo.getModuleClass(), pdo.getModuleClass(), discoSvc);
	    
	    //Get the pipe advertisement.
            PipeAdvertisement pipeadv = 
                readPipeAdv(pdo.getPipeAdvInputStream());
           
	    //Create the module spec id.
            ModuleSpecID msID = IDFactory.newModuleSpecID(mcID);
            publishModuleSpecAdvertisement(mcID,
                    pipeadv, pdo.getModuleSpec(), 
		    pdo.getVersion(), 
		    pdo.getCreator(), 
                    msID,
                    discoSvc);
            
	    //Publish the module implementation advertisement.
            publishModuleImplAdvertisement(
		    pdo.getImplCode(),
		    pdo.getImplCompat(),
		    pdo.getImplDescription(),
		    pdo.getImplProvider(),
		    msID,
		    discoSvc);
	    
           //Create a new JxtaServerPipe for the service to recieve
	   //incomming service requests.
           jxtaServerPipe = new JxtaServerPipe(group, pipeadv);
       }catch (Exception ex){
           if(NASUFLogger.isEnabledFor(Level.ERROR))
               NASUFLogger.error("AbstractService: publishService: " + 
		    ex.toString());
       }
	
    }
    
    //This method removes a service from the service manager.
    public void removeService(Object service){
	
    }
    
    //This method starts the service.
    public void startService() {
	//Set the status to running.
        RUNNING = true;
        int count = 0;
	//While the service is running loop.
        while(RUNNING){
            try{ 
		//Set the server pipe to two seconds. This stops deadlock
                if(jxtaServerPipe != null){
                    jxtaServerPipe.setPipeTimeout(2000);
		    //When a service connection request is recieved make a
		    //new thread to process the transaction.
                    new Thread( 
                        new MessageHandler(jxtaServerPipe.accept(),
                            pml)).start();
                }else{
                    //The Server Pipe has been set to null
                    //so just exit the loop
                    break;
                }
            }catch (SocketException se){
                //This is a controlled exception so just resume.
                continue;
            }catch(Exception e){
                if(NASUFLogger.isEnabledFor(Level.ERROR))
                    NASUFLogger.error("AbstractService: startService " + 
			e.toString());
            }
        }
	//Display that the service has stopped.
        if(NASUFLogger.isEnabledFor(Level.DEBUG)){
            NASUFLogger.debug("Service Stopped");
        }
    }
    
    //This method stops the service 
    public void stopService(){
        pml.stop();
        RUNNING = false;
    }
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp() {
        peerID = null;
        group = null;
        discoSvc = null;
        jxtaServerPipe = null;
        PIPEADV = null;
        sd = null;
        RUNNING = true;
        pml = null;
        mcadv = null;
        mdadv = null;
    }
    
    //This inner class handles the service connection request.
    public class MessageHandler implements Runnable {
        JxtaBiDiPipe pipe = null;
        IServicePipeMsgListener pipeListener = null;
       
        public MessageHandler(JxtaBiDiPipe pipe,
                IServicePipeMsgListener pipeListener){
            this.pipe = pipe;
            this.pipeListener = pipeListener;
            pipeListener.setPipe(pipe);
        }
       
        public void run() { 
            try{
                if(pipe != null)
                    pipe.setListener(pipeListener);
            }catch(Exception e){
                if(NASUFLogger.isEnabledFor(Level.ERROR))
                    NASUFLogger.error("MessageHandler: run: " + 
			e.toString());
            }
        }       
    }
}