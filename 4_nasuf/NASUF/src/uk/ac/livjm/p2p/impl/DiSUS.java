/*
 * Class Title:         DiSUS
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.p2p.impl;

//Java SDK imports
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

//JXTA API imports
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.impl.protocol.ResolverQuery;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.ResolverQueryMsg;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.protocol.ResolverResponseMsg;
import net.jxta.resolver.ResolverService;
import net.jxta.resolver.QueryHandler;
import net.jxta.util.JxtaBiDiPipe;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.constant.DECAPConstants;
import NASUF.src.uk.ac.livjm.device.IDevice;
import NASUF.src.uk.ac.livjm.listener.resolver.ResolverMsgHandlerFactory;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;
import NASUF.src.uk.ac.livjm.service.abstract_service.impl.AbstractService;
import NASUF.src.uk.ac.livjm.service.abstract_service.IAbstractService;

//Log4J API imports
import org.apache.log4j.Level;

public class DiSUS implements IDiSUS, Runnable{
    
    //This contains a reference to the device that is using 
    //the P2P interface.
    private IDevice device;
    //This collection contains a collection of core services
    //the device provides.
    private List coreServices;
    //This collection contains a collection of application
    //specific services the device provides.
    private List applicationServices;
    //This variable contains the peer group the device
    //belongs to.
    private PeerGroup peerGroup;
    //Devices use the resolver service to propagate messages
    //to all devices within the network, consequently this 
    //variable contains the resolver service needed to achieve
    //this
    private ResolverService resolverSvr;
    //The resolver service sends messages using a handler name.
    //Devices registered to recieve such messages will process
    //any incoming message recieved. This varaible contains the 
    //handler name for DiSUS messages.
    private String handlerName;
   
    //Default constructor. This constructor takes one parameter
    //which is the reference to the device that implements the 
    //p2p interface.
    public DiSUS(IDevice device) {
	//Display that a new instance of DiSUS is being created.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Creating new DiSUS Instance");
        //Store the device reference.
        this.device = device;
	//Create the core services collection
        coreServices = new ArrayList();
	//Create the application specific services collection.
        applicationServices = new ArrayList();
	//Assign the handler name for resolver messages
	handlerName = "DiSUSMessage";
    }
   
    //This method is a call back method that is called when 
    //the DECAP service returns a device capability result.
    //This method takes two parmeters - the first parameter
    //is the response message recieved from the DECAP service
    //and the second is the result.
    public void setDECAPResult(
	    ResolverResponseMsg
            result){
	//Let the device know that an application specific
	//peer service has been found.
        device.setApplicationPeerServiceFound(result);
    }
    
    //Return the depedency services used by the device
    //to the caller
    public List getDependentServices(){
        return device.getDependentServices();
    }
    
    //This getter returns the devices capability model
    public String getDCM(){
        return device.getDCM();
    }
    
    //Start JXTA using a specified peerGroup
    public void startJxta(PeerGroup peerGroup){
	try{
	    //Create a new peer group instance. In this case the device
	    //joins the NetPeerGroup.
            peerGroup = peerGroup;
	    //Set the peer group in the Abstract Service so that all
	    //core and application specific services have access to the
	    //group services.
            AbstractService.setPeerGroup(peerGroup);
            
	    //Create a new resolver service, set the handler name
	    //and register a message handler.
            resolverSvr = peerGroup.getResolverService();
            resolverSvr
                .registerHandler(handlerName,  
                    (QueryHandler)ResolverMsgHandlerFactory
                        .createDISUS_Handler(
                            this));
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS: startJxta: " + 
		    e.toString());
            System.exit(1);
        }
    }
    
    //This method starts JXTA and connects the device
    //to the P2P network.
    public void startJxta(){
        try{
	    //Create a new peer group instance. In this case the device
	    //joins the NetPeerGroup.
            peerGroup = PeerGroupFactory.newNetPeerGroup();
	    //Set the peer group in the Abstract Service so that all
	    //core and application specific services have access to the
	    //group services.
            AbstractService.setPeerGroup(peerGroup);
            
	    //Create a new resolver service, set the handler name
	    //and register a message handler.
            resolverSvr = peerGroup.getResolverService();
            resolverSvr
                .registerHandler(handlerName,  
                    (QueryHandler)ResolverMsgHandlerFactory
                        .createDISUS_Handler(
                            this));
        }catch(PeerGroupException e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS: startJxta: " + 
		    e.toString());
            System.exit(1);
        }
    }
    
    //This version of the startJxta method allows the device
    //to register a custom query handler for resolver messages.
    public void startJxta(QueryHandler handler){
        try{
	    //Create a new peer group instance. In this case the device
	    //joins the NetPeerGroup.
            peerGroup = PeerGroupFactory.newNetPeerGroup();
	    //Set the peer group in the Abstract Service so that all
	    //core and application specific services have access to the
	    //group services.
            AbstractService.setPeerGroup(peerGroup);
            
	    //Create a new resolver service, set the handler name
	    //and register a message handler.
            resolverSvr = peerGroup.getResolverService();
            resolverSvr
                .registerHandler(handlerName,  
                    handler);
        }catch(PeerGroupException e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS: startJxta: " + 
		    e.toString());
            System.exit(1);
        }
    }
    
    //This getter returns the peer group to the caller.
    public PeerGroup getPeerGroup(){
        return peerGroup;
    }
    
    //This method allows the device to change peer groups
    public void changePeerGroup(PeerGroup peerGroup) {
	this.peerGroup = peerGroup;
    }
    
    //This method adds an application specific peer service to 
    //the applicationServices collection. This method takes one
    //parameter which is a IApplicationPeerService interface, 
    //consequently any service can be used as long as it implements
    //the IApplicationPeerService interface.
    public void addApplicationPeerService(IAbstractService service){
        applicationServices.add(service);
    }
    
    //This method removes an application service from the collection.
    public void removeApplicationPeerService(IAbstractService service){
        applicationServices.remove(service);
    }
    
    //This method removes all application services from the collection
    public void removeAllApplicationPeerServices(){
        applicationServices.clear();
    }
    
    //This method returns the application peer services collection to
    //the caller.
    public List getApplicationPeerServices(){
        return applicationServices;
    }
    
    //This method adds a core service to the core services collection
    public void addCoreService(IAbstractService service){
        coreServices.add(service);
    }
    
    //This method removes a core service from the core services
    //collection
    public void removeCoreService(IAbstractService service){
        coreServices.remove(service);
    }
    
    //This method removes all core servces from the core services
    //collection.
    public void removeAllCoreServices(){
        coreServices.clear();
    }
    
    //This method returns the core services collection back 
    //to the caller.
    public List getCoreServices(){
        return coreServices;
    }
    
    //This method starts each of the core services 
    //the device provides.
    public void startCoreServices(){
	//Iterate through the services collection
        Iterator iter = coreServices.iterator();
        while(iter.hasNext()){
	    //Start service.
            new Thread(
                (Runnable)iter.next())
                    .start();
        }
    }
    
    //This method stops all the core services provided by the 
    //device.
    private void stopCoreServices(){
	//Iterate through the core services collection
        Iterator iter = coreServices.iterator();
        while(iter.hasNext()){
	    //Stop the core service.
            ((AbstractService)iter
		.next())
		    .stopService();
        }
    }
    
    //This method starts each of the application specific 
    //services provided by the device.
    public void startApplicationPeerServices(){
	//Iterate through the application peer services
	//collection
        Iterator iter = applicationServices.iterator();
        while(iter.hasNext()){
	    //Start the application peer service.
            new Thread(
                (Runnable)iter.next())
                    .start();
        }
    }
    
    //This method stops all the application peer services 
    //provided by the device.
    private void stopApplicationServices(){
	//Iterate through the application peer services
	//collection
        Iterator iter = applicationServices.iterator();
        while(iter.hasNext()){
	    //Stop the service
            ((AbstractService)iter.next()).stopService();
        }
    }
    
    //This method starts both core and application specific
    //services.
    public void startServices(){
	//Display that the services are being started.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Starting Services ...");
        
        try{
	    //Start core services
            startCoreServices();
	    //Start application specific services.
            startApplicationPeerServices();
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS: startServices" + 
		    e.toString());
        }
    }
    
    //Need to look at this in more detail. Each handler at the moment
    //has the below duplicated code. This code should only appear in 
    //the handler and used by each of its subclass handlers.
    public ModuleSpecAdvertisement discoverCoreService(
        String serviceName) {
	//Display that the service is being searched for.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Searching for the " + 
                serviceName + " Service advertisement");
        
        Enumeration serviceEnum = null;
	//Try to discover the service locally and remotely.
        while(true){
            try{
		//Discover the service locally.
                serviceEnum = peerGroup.getDiscoveryService()
                    .getLocalAdvertisements(DiscoveryService.ADV, 
                        "Name",
                        serviceName);
		//If the service is found then break out of the loop
                if((serviceEnum != null) && serviceEnum.hasMoreElements())
                    break;

		//Try to find the service remotely.
                peerGroup.getDiscoveryService()
                    .getRemoteAdvertisements(null, 
                        DiscoveryService.ADV, 
                        "Name",
                        serviceName, 1, null);
                //Wait for 2 seconds for a response.
                Thread.sleep(2000);
            }catch(Exception e){
                if(NASUFLogger.isEnabledFor(Level.ERROR)){
                    NASUFLogger.error("DiSUS: discoverCoreService: " 
			+ e.toString());
                    return null;
                }
            }
        }
        //Dislay that the service has been found.
	if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Found " 
                + serviceName 
                + " service advertisement:");
        //Return the module specification advertisement.
        return (ModuleSpecAdvertisement) serviceEnum.nextElement();
    }
    
    //This method discovers application specific peer services. It
    //takes one parameter, which is a query that contains the 
    //required DCM and peer service PSCM. Services are discovered
    //using the resolver service.
    public void discoveryApplicationPeerService(
        ResolverQueryMsg query){
        resolverSvr.sendQuery(null, query);
    }
    
    //This method allows applicatoin peer services to be
    //discovered. It takes three parameters - the first parameter
    //is an id that describes a specific peer service, the second
    //is the dcm and the third is the pscm.
    public void discoverApplicationPeerService(
            int id,
            String dcm,
            String pscm){
        
	//Create a new structured document of type service request.
        StructuredTextDocument doc;
        doc = (StructuredTextDocument)
            StructuredDocumentFactory.newStructuredDocument(
                new MimeMediaType("text/xml"),"ServiceRequest");

	//Add the child element that describes the service request
	//type.
        doc.appendChild(
            doc.createElement(
                ServiceRequestConstants.REQUEST_TYPE,
                "findApplicationPeerService"));

	//Add the dcm
        doc.appendChild(
            doc.createElement(
                DECAPConstants.SERVICE_REQUEST_DECAP_TAG, dcm));
        
	//Add the pscm
        doc.appendChild(
            doc.createElement(
                ServiceRequestConstants.SERVICE_REQUEST_PSCM_TAG, pscm));

	//Create a new query.
        ResolverQueryMsg message;
	//Create a new string writer,which will contain the 
	//structured document.
        StringWriter out = new StringWriter();
        
	//Write the structured document to the string writer
	//object.
        try{
            doc.sendToWriter(out);
        }catch(Exception ex){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS: discoverApplicationPeerService: " + 
		    ex.toString());
        }

	//Create the query.
        message = new ResolverQuery();
	//Set the message handler name.
        message.setHandlerName(handlerName);
        message.setCredential(null);
	//Add the structured document to the query.
        message.setQuery(out.toString());
	//Set the query id.
        message.setQueryId(id);
       
	//Display that the query is being sent.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Sending Query ...");
     
        // Broadcast to all members of the group
        resolverSvr.sendQuery(null, message);
    }
    
    //This method allows a device to bind to a service. It takes
    //two parameters - the first paramter is the module
    //specification, which contains the pipe advertisement for the
    //service endpoint. The second parameter is the pipe listener
    //the device uses to receive messages from the service.
    public JxtaBiDiPipe bindToService(
        String moduleSpecAdv,
        PipeMsgListener listener){
	//Create a JxtaBiDiPipe type.
        JxtaBiDiPipe pipe = null;
	
        try{
	    //Create a new Module Specification Advertisement object.
            ModuleSpecAdvertisement msadv =
            (ModuleSpecAdvertisement)
                AdvertisementFactory
                    .newAdvertisement(  
                        MimeMediaType.XMLUTF8,
                        new StringReader(moduleSpecAdv));
			
	    //Extract the pipe advertisement from the module specification.
            PipeAdvertisement pipeadv = msadv.getPipeAdvertisement();
	    //If the pipe is null exit.
            if(pipeadv == null){
                if(NASUFLogger.isEnabledFor(Level.ERROR))
                    NASUFLogger.error("Error -- Null pipe advertisement!");
                System.exit(1);
            }
            
            //New code added to get the Peer ID from the 
            //Module Specification Advertisement.
            StructuredTextDocument paramDoc
                = (StructuredTextDocument)msadv.getParam();
            Enumeration elements = paramDoc.getChildren("PeerID");
	    
            TextElement peerIDElement = (TextElement)elements.nextElement();
            String strPeerID = "";
            if(peerIDElement.getName().equalsIgnoreCase("PeerID")){
                strPeerID = peerIDElement.getTextValue();
            }
            
	    //Try three times to bind to the service.
            for(int i =0; i < 3; i++){
		//Display that the DiSUS is trying to bind to the pipe.
                if(NASUFLogger.isEnabledFor(Level.INFO))
                    NASUFLogger.info("Trying to bind to pipe ...");
                
                try{
                    pipe = new JxtaBiDiPipe();
                    pipe.setReliable(true);
		    //Connect to the service.
                    pipe.connect(
                        this.getPeerGroup(),
                        (PeerID)IDFactory.fromURI(new URI(strPeerID)),
                        pipeadv,
                        3000, 
                        listener,
                        true);
                    //Once a connetion is made break from the loop
                    break;
                }catch(Exception ioe){
                    if(NASUFLogger.isEnabledFor(Level.ERROR))
                        NASUFLogger.error("DiSUS: bindToService: " + 
			    ioe.toString());
                }
            }
           
	    //If the pipe is null return null;
            if(pipe == null){
                if(NASUFLogger.isEnabledFor(Level.ERROR))
                    NASUFLogger.error("Error resolving pipe endpoint");
                return null;
            } 
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS: bindToService: " + 
		    e.toString());
            
        }
	//Return pipe
        return pipe;
    }
    
    //This method allows the device to send commands to the device.
    public void sendCommand(String command, JxtaBiDiPipe pipe){
        try{
	    //Create a message
            Message dcmMsg = new Message();
            //Add the command type the message.
            dcmMsg.addMessageElement(
                ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    new StringMessageElement(
                        "Command", 
                        command, null));
	    //Send the message to the service.
            pipe.sendMessage(dcmMsg);
	    //Close the pipe 
            pipe.close();
	    //Destroy the pipe instance.
            pipe = null;
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS: sendCommand: " 
		    + e.toString());
        }
    }
    
    //This method allows the device to send a resolver query to every 
    //device within the network.
    public void sendResolverQuery(String id, ResolverQueryMsg msg){
        //Display that the query is being sent.
	if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Sending Resolver Query...");
        
        try{
	    //Send query.
            resolverSvr.sendQuery(null, msg);
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DiSUS: sendResolverQuery: " + 
		    e.toString());
        }
    }
    
    //This method allows the device to send a response message
    //for a query.
    public void sendResolverResponse(
        String source,
        ResolverResponseMsg msg){
	//Send response to querying peer.
        resolverSvr.sendResponse(source, msg);
    }
  
    //This object is threaded as such the run method is the 
    //code that gets run when the thread is created. In this 
    //instance most of the methods in this class are either 
    //call back methods or controlled directly by the device.
    public void run(){  
        //Just run the DiSUS Manager there is no need to do
        //anything else.
    }
    
    //This call  back method instructs devices to unregister
    //services because the service has become unavailable.
    public void unregisterService(String serviceName){
        device.unregisterService(serviceName);
    }
    
    //This call back method instructs the device to 
    //register a service it knows about because it has
    //become available again.
    public void registerService(String serviceName){
        device.registerService(serviceName);
    }
    
    //When the manager is stopped make sure that all the core 
    //and application services are stopped, before stopping the 
    //resolver service and peer group.
    public void stopManager() {
        stopCoreServices();
        stopApplicationServices();
        resolverSvr.unregisterHandler(handlerName);
        resolverSvr.stopApp();
        peerGroup.stopApp();
    }
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        peerGroup = null;
        resolverSvr = null;
        coreServices = null;
        applicationServices = null;
    }
}