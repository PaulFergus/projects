/*
 * Class Title:         Device
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.device;

//Java SDK imports
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;
import NASUF.src.uk.ac.livjm.p2p.P2PFactory;
import NASUF.src.uk.ac.livjm.protocol.data_objects.IDataObject;
import NASUF.src.uk.ac.livjm.service.abstract_service.IAbstractService;
import NASUF.src.uk.ac.livjm.utils.ReadFileToString;

//Jxta API imports
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.impl.protocol.ResolverQuery;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.ResolverQueryMsg;
import net.jxta.protocol.ResolverResponseMsg;
import net.jxta.resolver.QueryHandler;
import net.jxta.util.JxtaBiDiPipe;

//Log4J imports
import org.apache.log4j.Level;

public abstract class Device implements IDevice{
    
    protected IDiSUS disus = null;
    private NASUFLogger logger;
    protected String port = "";
    protected String handlerName = "DiSUSMessage";
    protected String dcm = "";
    
    //Default constructor
    public Device(){
        disus = P2PFactory.createJxtaP2P(this);
        ((Runnable)disus).run();
        disus.startJxta();
        logger = new NASUFLogger();
        this.port = "0";
    }
    
    //This constructor takes one parameter - the port
    //address the device listens for data on.
    public Device(String port) {
        this();
        this.port = port;
    }
    
    //This constructure takes a custom handler to be used by 
    //disus. And instance of a device that calls this constructor
    //does not listen for data so no port number is required. 
    public Device(QueryHandler handler){
        disus = P2PFactory.createJxtaP2P(this);
        ((Runnable)disus).run();
        disus.startJxta(handler);
        logger = new NASUFLogger();
    }
    
    //This constructure takes to parameters - the first parameter
    //is the port on which the device listens for data and the 
    //second is the custom handler to be used by DiSUS.
    public Device(String port, QueryHandler handler){
        this(handler);
        this.port = port;
    }
    
    //This getter method returns an instance of the disus manager
    //being used by the device. Again this returned object is an
    //interface. This makes the framework extensible in that any
    //disus object can be used as long as it implements the 
    //IDiSUS interface.
    public IDiSUS getDiSUS(){
        return disus;
    }
    
    //This setter method sets the device capability model for the
    //device. The parameter passed in is the filename where the 
    //file exists.
    public void setDCM(String filename){
        try{
            //Read the file contents into a string and set the DCM
            //class variable. 
            this.dcm = ReadFileToString
                .getContents(
                    new File(filename));
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("Device: setDCM: " + 
		    e.toString());
        }
    }
    
    //This getter method returns the device capability model for 
    //this device.
    public String getDCM(){
        return dcm;
    }
    
    //Devices may discover dependency services. When an event is recieved
    //that a new service has been found, this method is triggered and a
    //new service is passed in as a parameter. Again this parameter is an
    //interface of type IApplicationPeerService so that any type of service
    //can be processed as long as it implements the IApplicationPeerService
    //interface.
    protected void addApplicationPeerService(IAbstractService service){
        disus.addApplicationPeerService(service);
    }
    
    //This must be overridden in the suclasses. This method is designed
    //to enable the device to add its own application specific services
    //Consequently this is why this method must be overridden because
    //the type of application peer services added is dependent on the 
    //type of device. If the device does not offer any services then 
    //this method is not overriden - it is called locally within the 
    //Device class but nothing happens.
    public void addApplicationPeerServices(){ 
    }
    
    //A device may choose to offer a number of core services
    //that comprise the NASUF framework. Each core service 
    //can be added this this method. 
    protected void addCoreService(IAbstractService service){
        disus.addCoreService(service);
    }
    
    //This method must be overridden in the subclasses. This method is 
    //designed to enable the device to offer one or more core services.
    //Depending on the devices capabilities the device may choose to 
    //implement zero or more core services. If the device does not
    //offer any services then this method is not overriden - it is called 
    //locally within the Device class but nothing happens.
    public void addCoreServices(){
    }
    
    //If a device offers any application or core services then this 
    //method is used to start each service in turn.
    public void startServices(){
        disus.startServices();
    }
    
    //If a device requires any dependent services then this method
    //is used to disocver these services remotely within the 
    //network. This version of the method allows a precreated
    //ResolverQueryMsg to be passed in.
    public void discoverApplicationPeerService(
        ResolverQueryMsg msg){
        disus
            .discoveryApplicationPeerService(
                msg);
    }
    
    //If a device requires any dependent services then this method
    //is used to disocver these services remotely within the 
    //network. This version of the method allows an query id to be
    //passed in along with a device capability model and a peer
    //service capability model
    public void discoverApplicationPeerService(
        int id,
        String dcm,
        String pscm){
    
        disus.discoverApplicationPeerService(
            id, 
            dcm, 
            pscm);
    } 
    
    //This method should be overridden in the subclassed device. Again
    //how the service is mananged is dependent on the implementation,
    //consequently this method must be overridden in the subclass 
    //This method is triggered when an event is recieved that a service
    //has been found.
    public void setApplicationPeerServiceFound(ResolverResponseMsg value) {
    }
    
    //This method should be overridden in the subclassed device. This method
    //must be overridden in the subclass because the dependent services 
    //required by a device is device dependent. For example a DVD player may
    //require a visual and audio service whilst an intercom system may only 
    //require audio services. 
    public void discoverDependentServices() {
    }
    
    //This method should be overridden in the subclassed device. Dependent 
    //services will be managed dependent on the implementation consequently
    //how dependent services are collected is implementation specific.
    public List getDependentServices() {
        return null;
    }
    
    //When a service is discovered there may be several candiate services
    //that alough pass the DCM requirements, have different capabilties. 
    //This method selects the service that will provide the best quality
    //of service.
    protected String selectBestService(List serviceCollection){
        IDataObject bestService = null;
        IDataObject tempService;
        double dcm_score = 0.0;
        Iterator iter = serviceCollection.iterator();
        try{
            while(iter.hasNext()){
                tempService = (IDataObject)iter.next();
                if((Double.valueOf(tempService.getDecapValue()).doubleValue()) > dcm_score){
                    bestService = tempService;
                    dcm_score = Double.valueOf(tempService.getDecapValue()).doubleValue();
                }
            }
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("Device: selectBestService: " + 
                    e.toString());
        }
        return bestService.getModuleSpec();
    }
    
    //Added on the 27th March 2005, I am not sure whether this
    //should stay here at the minute. It was originally was in
    //the playdevice code however it is envisaged that this code
    //will be used by any device that has dependent services,
    //consequently it should be generic code that can be used by
    //any device.
    public JxtaBiDiPipe bindToService(
        String moduleSpecAdv,
        PipeMsgListener listener){
        try{
            return disus.bindToService(
                moduleSpecAdv,
                listener);
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.DEBUG))
                NASUFLogger
                    .debug("Device: bindToService Error: " + 
                        e.toString());
            
            return null;
        }
    }
    
    //This method allows commands to be sent to the
    //pipe. It takes two parameters; the first is the
    //command and the second is the pipe.
    public void sendCommand(
        String command, 
        JxtaBiDiPipe pipe){
        disus.sendCommand(command, pipe);
    }
    
    //This method is used by the device to send an unregistering message
    //to other devices within the network.
    public void sendServiceUnregisteringMsg(IAbstractService service){
        StructuredTextDocument doc;
        doc = (StructuredTextDocument)
            StructuredDocumentFactory.newStructuredDocument(
                new MimeMediaType("text/xml"),"UnregisterService");
        //Create the message type element. In this case teh message is
        //a request to unregsiter a service.
        doc.appendChild(
            doc.createElement(
                ServiceRequestConstants.REQUEST_TYPE,
                "unregisterService"));
        //Add the module spec for the service the device is unregistering.
        doc.appendChild(
            doc.createElement(
                "ModuleSpec", ((IAbstractService)service)
                    .getModuleSpecAdvertisement()
                        .toString()));
        //Start to create the query message used with JXTA.
        ResolverQueryMsg message;
        StringWriter out = new StringWriter();
        
        //Convert the structured document into a string writer.
        try{
            doc.sendToWriter(out);
        }catch(Exception ex){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("Device: sendServiceUnregisteringMsg: " + 
                    ex.toString());
        }
        //Create the new query and add the message information.
        message = new ResolverQuery();
        //This sets the type of message used by resolver handlers 
        //implemented by other devices. 
        message.setHandlerName(handlerName);
        //Credentials are not used within this implementation. This is
        //the subject for future work.
        message.setCredential(null);
        //Conver the string writer data into a string and use it
        //to set the query data.
        message.setQuery(out.toString());
       
        //Display that the service unregistering message is being sent.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Sending Unregister message  ...");
        
        //Broadcast to all members of the group. In this instance
        //we are broadcasting to every device becuase we have set 
        //the PeerID to null. It we were to target a specific 
        //device then we would use the PeerID for that device.
        //This would ensure that only that device processed the 
        //message.
        sendResolverQuery(null, message);
    }
    
    //This method is used by the device to register a service. Again
    //this is to signify to devices that use this service as part of 
    //a bigger composition that the service is being offered again.
    public void sendServiceRegistrationMsg(IAbstractService service){
        StructuredTextDocument doc;
        doc = (StructuredTextDocument)
            StructuredDocumentFactory.newStructuredDocument(
                new MimeMediaType("text/xml"),"registerService");
        //Create the message type element. In this case the message is
        //a request to regsiter a service.
        doc.appendChild(
            doc.createElement(
                ServiceRequestConstants.REQUEST_TYPE,
                "registerService"));
        //Add the module spec for the service the device is registering.
        doc.appendChild(
            doc.createElement(
                "ModuleSpec", service
                    .getModuleSpecAdvertisement()
                        .toString()));
        //Start to create the query message used with JXTA.
        ResolverQueryMsg message;
        StringWriter out = new StringWriter();
        
        //Convert the structured document into a string writer.
        try{
            doc.sendToWriter(out);
        }catch(Exception ex){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("Device: sendServiceRegistrationMsg: " + 
                    ex.toString());
        }
        
        //Create the new query and add the message information.
        message = new ResolverQuery();
        //This sets the type of message used by resolver handlers 
        //implemented by other devices. 
        message.setHandlerName(handlerName);
        //Credentials are not used within this implementation. This is
        //the subject for future work.
        message.setCredential(null);
        //Conver the string writer data into a string and use it
        //to set the query data.
        message.setQuery(out.toString());
       
        //Display that the device is sending a service registration message
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Sending Register message  ...");
        
        //Broadcast to all members of the group. In this instance
        //we are broadcasting to every device becuase we have set 
        //the PeerID to null. It we were to target a specific 
        //device then we would use the PeerID for that device.
        //This would ensure that only that device processed the 
        //message.
        sendResolverQuery(null, message);
    }
    
    //This method allows the device to recieve messages from other devices
    //that are registering a service. When a device is initially switched on
    //it connects to the network and sends a service registration message.
    //This sole purpose of this functionality is allow devices that used.
    //previously registed services to be re-added to the composition. This 
    //ensures that the device does not have to re-disocver the service, 
    //because it is already aware of the service and the device that provides
    //the service.
    protected String registerService(
                    String serviceName,
                    List serviceCollection,
                    List serviceCollectionBin){
        //These string values contain the service module specs, inluding
        //the service names and peer ids.
        String currentService;
        String unregServiceName;
        String unregServicePeerID;
        String curreServiceName;
        String curreServicePeerID;
        
        String moduleSpec = "";
        
        //These variables contain a structured document version of the 
        //service passed in to be registed and a current service 
        //retrieved from a service collection.
        StructuredTextDocument unregService;
        StructuredTextDocument curreService;
        
        try{
            //Convert the service to be unregsitered into a 
            //Structured document.
            unregService = (StructuredTextDocument)
                    StructuredDocumentFactory.newStructuredDocument(
                        new MimeMediaType("text/xml"),
                            new ByteArrayInputStream((serviceName.getBytes())));
            Enumeration en;
            Element el;
            
            //Extract the name for the service to be unregistered.
            en = unregService.getChildren("Name");
            el = (Element)en.nextElement();
            unregServiceName = (String) el.getValue();

            //Extract the Peer ID for the service to be unregistered.
            en = unregService.getChildren("Parm");
            el = (Element)en.nextElement();
            unregServicePeerID = (String)
                ((Element)el
                    .getChildren("PeerID")
                        .nextElement())
                            .getValue();
            
            
            //Create an iterator for the service collection bin. A bin 
            //collection is used to store services that have become
            //inactive. Instead of just deleting these services this 
            //functionality enables the device to cache services in case
            //the come back on-line again.
            Iterator iter = serviceCollectionBin.iterator();
            
            //This is used to contain any services found in the service 
            //bin collection. This mechanism is used to avoid concurrent
            //modification errors, i.e. you cannot iterate through a 
            //collection and modify it at the same time.
            Collection temp = new ArrayList();
            
            //This variable contains the next object to be retrieved from 
            //the service bin collection.
            Object currentObjService;
            while(iter.hasNext()){
                //Exract the next service and convert it into a structured
                //document.
                currentObjService = iter.next();
                curreService = (StructuredTextDocument)
                    StructuredDocumentFactory.newStructuredDocument(
                        new MimeMediaType("text/xml"),
                            new ByteArrayInputStream((
                                (((IDataObject)currentObjService))
                                    .getModuleSpec()
                                        .getBytes())));
                //Extract the name for the current service being processed
                en = curreService.getChildren("Name");
                el = (Element)en.nextElement();
                curreServiceName = (String) el.getValue();

                //Extract the Peer ID for the current service being processed.
                en = curreService.getChildren("Parm");
                el = (Element)en.nextElement();
                curreServicePeerID = (String)
                    ((Element)el
                        .getChildren("PeerID")
                            .nextElement())
                                .getValue();
                //If the service names and service peer ids match then the 
                //device has previously used the service to be registered.
                if((unregServiceName.equalsIgnoreCase(curreServiceName))
                    && (unregServicePeerID.equalsIgnoreCase(curreServicePeerID))){
                    //Display that the service is being re-registered.
                    if(NASUFLogger.isEnabledFor(Level.INFO))
                        NASUFLogger.info("Registering Service" );
                       
                    //Add the service to the bin before you remove it. This will
                    //be used incase the service registers itself again. In this
                    //instance the device checks the bin when a registerService
                    //message is recieved - if the service to be registered is
                    //contained in the bin then the object will be removed and
                    //added back to the active service collection for future use
                    serviceCollection.add(currentObjService);
                    temp.add(currentObjService);
                    //Display that the service was successfully re-registered.
                    if(NASUFLogger.isEnabledFor(Level.INFO))
                        NASUFLogger.info("Service Registered" );
                    
                    //Select the best service and regiter it as the active 
                    //service.
                    if(serviceCollection.size() > 0 ){
                        //Display that the device is trying to register a new
                        //service.
                        if(NASUFLogger.isEnabledFor(Level.INFO))
                            NASUFLogger.info("Registering new Service ... ");
                        
                        //Although the services returned to this device matched
                        //the QoS specification defined in the service request,
                        //services will have varied capabilities, that although
                        //satisfy the QoS parameters in the service request
                        //device capability model, certain services will provide
                        //the best QoS. So this method finds the service that 
                        //has the highest QoS value.
                        moduleSpec = selectBestService(serviceCollection);

                        //Display that a new service has been registered.
                        if(NASUFLogger.isEnabledFor(Level.INFO))
                            NASUFLogger.info("New Service Registered");
                    }
                    break;
                }
            }
            //If the bin contains the registered service then remove
            //it because it has been added to the active service
            //collection.
            if(temp.size() > 0)
                serviceCollectionBin.removeAll(temp);
            
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("Device: registerService: " + 
                    e.toString());
        }
        return moduleSpec;
    }
    //This method should be overridden in the subclassed device. This method
    //is only used by services that use dependency services. If a depencency
    //service that has been previously registed reappears again, an event
    //in the subclass will be triggered. This trigger will use this method
    //to determine whether the service to be registed was once part of 
    //a composition of services the device previously discovered. How the
    //service is registered is dependent on the device registration
    //mechansim consequently this method must be overriden in the subclass.
    public void registerService(String serviceName){
    }
    
    //When devices are removed from the network they first broadcast a message
    //that states it is disconnecting from the network and removing the 
    //services it provides. Devices must process these messages to determine 
    //whether the service being removed forms part of a composition of
    //services it has created. In this instance the service must be registered
    //and an alternative service must be selected. Again this is achieved using
    //the observer pattern. Messages are recieved and the following method is 
    //triggered. Again if the device does not have any dependency services then
    //the device may choose not to override the unregisterService method.
    protected String unregisterService(String serviceName,
                    List serviceCollection,
                    List serviceCollectionBin){
        //These strings contain the service descriptions
        //for the service to be unregistred and the current
        //service known to the device, whether this is a 
        //service the device provides or one that forms
        //part of a composition that was previously 
        //discovered. These string variables also contain
        //the name of the service including the peer id.
        //These values are important becuase there will be 
        //several services that are the same, however when
        //a service was previously discovered a number of 
        //tests where carried out to determine its QoS value.
        //This being the case the device must ensure that the 
        //service registered contains the same PeerID as the
        //service the device has stored. This point is discussed
        //in more depth in the Thesis.
        String currentService;
        String curreServiceName;
        String curreServicePeerID;
        String moduleSpec = "";
        String unregServiceName;
        String unregServicePeerID;
        
        //These structured documents are used to convert the
        //strings versions of the service descriptions into
        //and XML format. This makes the processing of the 
        //data easier using the document APIs provided by
        //JXTA.
        StructuredTextDocument unregService;
        StructuredTextDocument curreService;
        
        //Again the service to be unregistered is passed in as a string 
        //representation. The service is converted into a structured 
        //text document so that the XML elements can be more easily 
        //navigated.
        try{
            unregService = (StructuredTextDocument)
                    StructuredDocumentFactory.newStructuredDocument(
                        new MimeMediaType("text/xml"),
                            new ByteArrayInputStream((serviceName.getBytes())));
            Enumeration en;
            Element el;
            //Extract the name of the service.
            en = unregService.getChildren("Name");
            el = (Element)en.nextElement();
            unregServiceName = (String) el.getValue();
            
            //Extract the PeerID associated with the service.
            en = unregService.getChildren("Parm");
            el = (Element)en.nextElement();
            unregServicePeerID = (String)
                ((Element)el
                    .getChildren("PeerID")
                        .nextElement())
                            .getValue();
            Object currentObjService;
            
            //This is used to iterator through all the registered 
            //services in the service Collection so that the device
            //can determine whether it uses the service that is being
            //unregistered.
            Iterator iter = serviceCollection.iterator();
            
            //In order to stop concurrent modification problems the object
            //that describes the service is added to the temp collection
            //which will be used to remove the service from the 
            //service collection once the device has iterated over all 
            //services. 
            Collection temp = new ArrayList();
            
            //This is used to indicate whether the service has been found.
            //The reason for this variable is described below.
            boolean serviceRemoved = false;
            while(iter.hasNext()){
                //Get the next service and convert the module spec
                //into a structured document.
                currentObjService = iter.next();
                curreService = (StructuredTextDocument)
                    StructuredDocumentFactory.newStructuredDocument(
                        new MimeMediaType("text/xml"),
                            new ByteArrayInputStream((
                                (((IDataObject)currentObjService)
                                    .getModuleSpec().getBytes()))));
                //Get the servcies name. This value will be compared
                //with the service name that is unregistering itself
                en = curreService.getChildren("Name");
                el = (Element)en.nextElement();
                curreServiceName = (String) el.getValue();
                
                //Get the services PeerID. The peerid is used to determine
                //whethere the service the device knows about is the same
                //service provided by a specif device whose ids match. For
                //example the same service could be provided by multiple 
                //devices, consequently the device only wants to unregister
                //the service if it belongs to the device it discovered.
                en = curreService.getChildren("Parm");
                el = (Element)en.nextElement();
                curreServicePeerID = (String)
                    ((Element)el
                        .getChildren("PeerID")
                            .nextElement())
                                .getValue();
                
                //If the the service names and peerids match then the service
                //needs to be unregistered.
                if((unregServiceName.equalsIgnoreCase(curreServiceName))
                    && (unregServicePeerID.equalsIgnoreCase(curreServicePeerID))){
                    
                    //Display that the service is being unregsitered.    
                    if(NASUFLogger.isEnabledFor(Level.INFO))
                        NASUFLogger.info("Unregistering Service" );
                    
                    //Add the service to the bin before you remove it. This will
                    //be used incase the service registers itself again. In this
                    //instance the device checks the bin when a registerService
                    //message is recieved - if the service to be registered is
                    //contained in the bin then the object will be removed and
                    //added back to the active service collection for future use
                    if(!serviceCollectionBin.contains(currentObjService)){
                        serviceCollectionBin.add(currentObjService);
                        temp.add(currentObjService);
                        //State that the service has been removed.
                        serviceRemoved = true;
                    }
                    
                    //Display that the service was successfully unregistered.
                    if(NASUFLogger.isEnabledFor(Level.INFO))
                        NASUFLogger.info("Service Unregistered" );
                    
                    break;
                }
            }
            
            //If the service to be unregistered is contained in the active
            //service collection then remove it. 
            if(temp.size() > 0)
                serviceCollection.removeAll(temp);
            
            //If the service has been removed then ensure that the device
            //can self-adapt by selecting the same service type from is 
            //collection. Rember when a device discovers a composite service
            //it will store every service it recieves in its collection for
            //that service. Each of these services will match semantically and
            //in terms of the required QoS parameters. It is clear that even
            //though a service has passed the service request requirements, 
            //services will provide a better QoS so when a new service is
            //selected this algoirthm must take into account the QoS value that
            //provides the best QoS. In short the device picks the next best
            //service.
            if(serviceRemoved){
                if(serviceCollection.size() > 0 ){
                    //Display that a new service is being registered.
                    if(NASUFLogger.isEnabledFor(Level.INFO))
                        NASUFLogger.info("Registering new Service ...");
                    
                    //Determine if the device has any backup services.
                    //If it does select the best service, i.e. the one
                    //that contains the highest QoS value.
                    moduleSpec = selectBestService(serviceCollection);
                    
                    //Display that the new service was successfully 
                    //registered.
                    if(NASUFLogger.isEnabledFor(Level.INFO))
                        NASUFLogger.info("New Service Registered");
                }
            }
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("PlayerDevice: unregisterService: " + 
                    e.toString());
        }
        return moduleSpec;
    }
    
    //This method should be overridden in the subclassed device. This method
    //is triggered when a device removes the services it provides from the 
    //network. How services are unregistered is application specific so 
    //this method must be overriden in the subclass.
    public void unregisterService(String serviceName){
    }
    
    //This method is used to send messages to either specific peer 
    //resolver services or to every resolver service implemented on 
    //devices connected to the network.
    public void sendResolverQuery(String id, ResolverQueryMsg msg){
        disus.sendResolverQuery(id, msg);
    }
    
    //When the device is stopped make sure that all the 
    //objects used by the device are stopped and reset.
    public void stopDevice(){
        disus.stopManager();
    }
   
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        disus.cleanUp();
        disus = null;
        logger = null;
    }
}