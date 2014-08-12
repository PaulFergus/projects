/*
 * Class Title:         PlayerDevice
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.device.impl.player;

//Java SDK imports
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

//Jxta API imports
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.impl.protocol.ResolverQuery;
import net.jxta.protocol.ResolverResponseMsg;
import net.jxta.protocol.ResolverQueryMsg;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.AdaptionConstants;
import NASUF.src.uk.ac.livjm.constant.impl.MetadataFiles;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.device.Device;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.protocol.data_objects.DataObjectFactory;
import NASUF.src.uk.ac.livjm.protocol.data_objects.impl.PrimaryServiceDataObject;
import NASUF.src.uk.ac.livjm.service.abstract_service.IAbstractService;
import NASUF.src.uk.ac.livjm.service.application_service.ApplicationPeerServiceFactory;
import NASUF.src.uk.ac.livjm.service.application_service.media_transmitter.impl.MediaTransmitterService;
import NASUF.src.uk.ac.livjm.service.core_service.CoreServiceFactory;
import NASUF.src.uk.ac.livjm.utils.ReadFileToString;

//Log4J imports
import org.apache.log4j.Level;

public class PlayerDevice extends Device{
   
    //This is the application specific service used to 
    //transmit audio/video data. The implementation specific
    //details have been abstracted to an interface. This allows
    //any media_transmitter implementation to be used as long
    //as it implements the IApplicationPeerService. However,
    //the functionality provided by this interface is generic,
    //consequently this object will need to be downcasted to
    //the required implementation class to access the app.
    //specific methods it provides. This is performed in the 
    //implementation classes for a particular application
    //not in the framework classes.
    private IAbstractService media_transmitter = null;
    
    //This statics are used as an id for audio
    //and video services. At the generic level
    //both of these services are media_reciever 
    //services, so the id determines what type of
    //media they process, i.e. audio or video.
    private static final int AudioID = 1;
    private static final int VideoID = 2;
    
    //Collections used to store the application
    //specific peer services. These collections
    //also include bin collections so that when
    //a service in a composition is lost or
    //unregistered it can be used again if it 
    //comes back on-line without having to 
    //rediscover it again.
    private List audioServices = null;
    private List audioServicesBin = null;
    private List videoServices = null;
    private List videoServicesBin = null;
   
    //These string variables contain the 
    //peer service module spec advertisements,
    //including the media file the the player
    //broadcasts. These strings also contain
    //the device capability models for the 
    //dependent services required by this 
    //device
    private String audioModuleSpec = null;
    private String videoModuleSpec = null;
    private String mediaFile;
    private String audio_dcm;
    private String video_dcm;
    
    //The player implementation here is based on TCP/IP and
    //RTP, therefore the following constants are used to 
    //specify the IP broadcast address and the base port
    //address.
    //public final String BROADCAST_ADDRESS = "192.168.0.255";
    public final String BROADCAST_ADDRESS = "150.204.255.255";
    //public final String BROADCAST_ADDRESS = "169.254.255.255";
    
    public final String ENDPOINT = "49148";
    
    //Default Constructor
    public PlayerDevice(
        String mediaFile){
        //Call the constructure in the Device abstract class
        super();
        //Store the media file to be processed.
        this.mediaFile = mediaFile;
        //Set the device capability model for this device. this
        //will be used to determine how capable the device is of
        //running the service.
        this.setDCM(MetadataFiles.PLAYER_DCM);
        audioServices = new ArrayList();
        audioServicesBin = new ArrayList();
        videoServices = new ArrayList();
        videoServicesBin = new ArrayList();
    }
    
    //Return the Audio Module Specification to the caller. The
    //value contain in audioModuleSpec is in fact an XML structure
    //that can be loaded into any XML passed parser. The design 
    //rational behind this is that the intefaces are designed to
    //be generic so that any implementation can be used.
    public String getAudioModuleSpec(){
        return audioModuleSpec;
    }
    
    //This setter allows the audio Module Specification to be 
    //be assigned to the audioModuleSpec variable. Again the
    //requirement is that is must be in a string format.
    public void setAudioModuleSpec(String audioModuleSpec){
        this.audioModuleSpec = audioModuleSpec;
    }
    
    //The get video module specification method performs the
    //same functionality as the getter described above. See
    //comments for further informatin.
    public String getVideoModuleSpec(){
        return videoModuleSpec;
    }
    
    //The setter method for the video module specification method
    //performs the same functionality as the setter described above.
    //See comments for further information.
    public void setVideoModuleSpec(String videoModuleSpec){
        this.videoModuleSpec = videoModuleSpec;
    }
    
    //The get audio services method returns the collection
    //containing all the audio services discovered by this
    //device.
    public List getAudioServices(){
        return audioServices;
    }
    
    //The get video services method returns the collection
    //containing all the video services discovered by this
    //device.
    public List getVideoServices(){
        return videoServices;
    }
    
    //This list of dependency services is returned to the 
    //DiSUS service so that the dependency service module
    //specs can be added to the returned query response. 
    //This allows the media center interface to determine
    //what dependent services the device has found. This 
    //also allows the services to be controlled via the 
    //media center
    public List getDependentServices(){
        List dServices = new ArrayList();

        //Iterator through all the audio services and add
        //them to the dependent services collection
        Iterator iter = getAudioServices().iterator();
        while(iter.hasNext()){
            dServices
                .add(iter.next());
        }
        //Iterator through all the video services and add
        //them to the dependent services collection
        iter = getVideoServices().iterator();
        while(iter.hasNext()){
            dServices
                .add(iter.next());
        }
        //Return all the application specific peer services
        return dServices;
    }
    
    //NASUF is an asynchrounous message oriented system. Consequently
    //when a device sends a message it listens for return messages
    //using an observer pattern. When the observer recieves a message,
    //this method will be called. In this instance the device sends 
    //out a service discovery message containing a device capability 
    //model that specifies the QoS value the device must support, and
    //a peer service capability model, which semantically defines the 
    //capabilites a candidate service must support. Any device that 
    //matches the device capability model and the peer service capability
    //model returns the service details to the caller, which results in
    //the setApplicationPeerServiceFound method being called. In this
    public void setApplicationPeerServiceFound(
                    ResolverResponseMsg value){
        StructuredTextDocument doc;
        StructuredTextDocument primaryService;
        //The response contained in the ResolverResponseMsg object is 
        //in a string format, which is structured as XML. This string 
        //is converted into a StructuredTextDocument so that the XML 
        //document can be more easily navigated.
        try{
            doc = (StructuredTextDocument)
                StructuredDocumentFactory.newStructuredDocument(
                    new MimeMediaType( "text/xml" ), 
                        new ByteArrayInputStream(value
                            .getResponse()
                                .getBytes()) );
            
            //Extract the DeCap score. This will be added to a
            //DependencyService object and used when selecting
            //services that have the highest QoS value.
            Enumeration en = doc.getChildren("DeviceDECAPScore");
            Element el = (Element)en.nextElement();
            String decapValue = (String)el.getValue();
            
            //Extract the Primary service. When a service response is returned
            //the service itself may be dependent on several other dependency
            //services. So to begin with the Primary service must be seperated
            //from its depedency services. 
            en = doc.getChildren("PrimaryService");
            el = (Element)en.nextElement();
            
            //Again a primary service is a string representation of an XML 
            //structure. Consequently the string is converted to a
            //StructuredTextDocument so that the XML can be more easily 
            //processed.
            primaryService = (StructuredTextDocument)
                StructuredDocumentFactory.newStructuredDocument(
                    new MimeMediaType("text/xml"),
                        new ByteArrayInputStream(((String)el
                            .getValue())
                                .getBytes()));
        
            //Extact the query ID. Remember I have already pointed out in my
            //comments that an audio and video service are both described as
            //a media_reciever service within my implementation. This being 
            //the case the id distinguishes between what type of data the 
            //service processes, i.e. audio or video. In this instance if the
            //id is 1 then this service processes audio data so the primary
            //service is added to the audio services collection.
            if(value.getQueryId() == 1){
                audioServices.add(
                    DataObjectFactory
			.createPrimaryServiceDataObject(
			    decapValue, 
			    primaryService
				.toString()));
            //If the id is 2 then this services processes video data so this 
            //services is added to the video services collection.
            }else if(value.getQueryId() == 2){
                videoServices.add(
                    DataObjectFactory
			.createPrimaryServiceDataObject(
			    decapValue,
			    primaryService
				.toString()));
            }
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR)){
                NASUFLogger
                    .error("PlayerDevice: setApplicationPeerServiceFound: " + 
                        e.toString());
            }
        }
    }
    
    //Devices will provide different application specific services
    //dependent on the type of device it is. For example a speaker
    //may offer an audio service. Alternatively a TV may offer serveral
    //services which include an audio service and a visual service. 
    //Again a device is not required to implement any application specific
    //core services. For example it may just offer core services for
    //monatory purposes. Again if a device offers one or more application
    //specific services then the device must override the 
    //addApplicationPeerServices method in its parent abstract class.
    public void addApplicationPeerServices(){
        //The Player Device only provides one service, which is
        //a service that transmitts media content, whether it
        //be audio or/and Video, as RTP packets.
        String playerServiceDescription =
            ReadFileToString.getContents(
                new File(MetadataFiles.PLAYER_PSCM));
        //When this service is created the audio and video 
        //module specifications are passed including the 
        //media file, the broadcast address and base port 
        //address. This implementation is based on TCP/IP, 
        //however the interface classes at protocol independent
        //and may support several different network protocols.
        //The module specifications for audio and video services
        //are passed that the transmitter service knows which
        //services to use for audio/video processing. These are
        //in effect dependency services the player device requires.
        media_transmitter 
            = ApplicationPeerServiceFactory
                .createMediaTransmitterService(
                    getAudioModuleSpec(),
                    getVideoModuleSpec(),
                    mediaFile,
                    BROADCAST_ADDRESS,
                    ENDPOINT,
                    playerServiceDescription,
                    this.getDiSUS());
        //Once a new intance of the application peer service has been 
        //created it is added to the application peer service collection 
        //for this device.
        addApplicationPeerService(media_transmitter);
    }
    
    //This method allows the device to add the core services it provides.
    //This is not a compulsary requirement and allow the device has to 
    //implement this method it could in fact be removed from the device
    //subclass itself because the method is implemented in is parent 
    //abstract class. A device subclass only needs to override this method
    //if it implements its own core services. If this method is not used
    //by the device, i.e. it does not provide its own core services, then
    //it will discover these services remotely within the P2P network. This
    //makes NASUF flexible in that a device has the option to explicitly
    //implement its own core services. This would be dependent on the 
    //capabilities of the device. For example a more capable device such as 
    //a PC may choose to implement all the core services locally, however a
    //device with limited capabilities may choose to outsource this
    //functionality to other devices because it is not capable of offering 
    //them itself.
    public void addCoreServices(){
        try{
            //Create and start the DECAP Service. Becuase this code has been
            //implemented this means that the device hosts its own version
            //of the DECAP Service. Consequently the device does not need to
            //discover this service remotely  
            addCoreService(CoreServiceFactory
                    .createDECAPService());
            
            //Display that the DeCap service has been added. This is only for
            //debug purposes and through log4j debug messages will be disabled
            //when the device software is deployed.
            if(NASUFLogger.isEnabledFor(Level.DEBUG))
                NASUFLogger.debug("Added the DECAP Core Service");
            
            //Create and start the SISM Service. Because this code has been
            //implemented this means that the device hosts its own version
            //of the SISM Service. Consequently the device does not need to 
            //discover this service remotely
            addCoreService(CoreServiceFactory
                    .createDistrESService());
            
            //Display that the DistrES service has been added. This is only for
            //debug purposes and through log4j debug messages will be disabled
            //when the device software is deployed.
            if(NASUFLogger.isEnabledFor(Level.DEBUG))
                NASUFLogger.debug("Added the DistrES Core Service");
            
            //Create and start the DistrES Service. Because this code has been
            //implemented this means that the device hosts its own version
            //of the DistrES Service. Consequently the device does not need to 
            //discover this service remotely
            addCoreService(CoreServiceFactory
                    .createSISMService(disus));
            
            //Display that the core service has been added. This is only for
            //debug purposes and through log4j debug messages will be disabled
            //when the device software is deployed.
            if(NASUFLogger.isEnabledFor(Level.DEBUG))
                NASUFLogger.debug("Added the SISM Core Service");
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("PlayerDevice: addCoreService: " + 
                    e.toString());
        }
    }
    
    //This method allows the device to discover any dependent services
    //it requires. For example a DVD player will know that it needs
    //two additional services; one to process the audio and one to 
    //process the video. This could in fact be one service that does
    //both audio and video.
    public void discoverDependentServices() {
        //This variable is used to count the number of attempts
        //the device has made to try and find a service.
        int count = 0;
                
        //Read the required device capability model that describes the 
        //QoS requirements a device that provides an audio service must
        //support. In this instance a file object containing the location 
        //of the file is passed to a helper class that reads the contents 
        //of the file into a string. 
        audio_dcm = ReadFileToString.getContents(
            new File(MetadataFiles.AUDIO_DCM));
        
        //Read the requied device capability model that describes the 
        //QoS requirements a device that provides a video service must
        //support.
        video_dcm = ReadFileToString.getContents(
            new File(MetadataFiles.VIDEO_DCM));
        
        //Display that the device is trying to find an audio service.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Tring to find Audio Services ...");
        
        //Make three attempts to find the service.
        for(int i = 0; i < 3; i++){
            try{
                //If a servcie is found the best service is selected and 
                //the loop is exited.
                if(!this.getAudioServices().isEmpty()){
                    //The device has found an audio service. This may have
                    //resulted in several services, that although satisfy the
                    //QoS requirements defined in the service request, will be 
                    //provided by devices with varied capabilities. This method
                    //call selects the service from the collection that has the 
                    //highest QoS value.
                    this.setAudioModuleSpec(
                        selectBestService(this.getAudioServices()));
                    break;
                }
                //We have tried three times without being able to find a service
                //exit the loop.
                if(count > 2){
                    if(NASUFLogger.isEnabledFor(Level.INFO))
                        NASUFLogger.info("Unable to find Audio Services");
                    
                    break;
                }
                //Discover audio services capable of processing 
                //the media audio stream.
                this
                    .discoverApplicationPeerService(
                        1, 
                        audio_dcm,
                        ReadFileToString.getContents(
                            new File(MetadataFiles.AUDIO_PSCM)));
                
                //increment the count to keep track of the number of attempts
                //made to try and discover the service.
                count++;
                
                //What five seconds before you try again. This may be configurable
                //I still need to look at this to determine how this will 
                //be achieved. The value required will be dependent on a number
                //of factors so it should be configurable externally.
                Thread.sleep(5000);
            }catch(Exception e){
                if(NASUFLogger.isEnabledFor(Level.ERROR))
                    NASUFLogger.error("PlayerDevice: discoverDependentServices: " + 
                        e.toString());
            }
        }
        //Re-initialise the count before tring to find a video service.
        count = 0;
        
        //Display that the device is trying to find a video service.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Tring to find Video Services ...");
        
        //Make three attempts to find the service.
        for(int i = 0; i < 3; i++){
            try{
                //If a service is found the best service is selected and 
                //the loop is exited.
                if(!this.getVideoServices().isEmpty()){
                    //The device has found an audio service. This may have
                    //resulted in several services, that although satisfy the
                    //QoS requirements defined in the service request, will be 
                    //provided by devices with varied capabilities. This method
                    //call selects the service from the collection that has the 
                    //highest QoS value.
                    this.setVideoModuleSpec(
                        selectBestService(this.getVideoServices()));
                    break;
                }
                    
                //We have tried three times without being able to find a service
                //exit the loop.
                if(count > 2){
                    if(NASUFLogger.isEnabledFor(Level.INFO))
                        NASUFLogger.info("Unable to find Video Services");
                    break;
                }
                //Discover video services capable of processing 
                //the media video stream.
                this
                    .discoverApplicationPeerService(
                        2, 
                        video_dcm,
                        ReadFileToString.getContents(
                            new File(MetadataFiles.VIDEO_PSCM)));
                
                //increment the count to keep track of the number of attempts
                //made to try and discover the service.
                count++;
                
                //What five seconds before you try again. This may be configurable
                //I still need to look at this to determine how this will 
                //be achieved. The value required will be dependent on a number
                //of factors so it should be configurable externally.
                Thread.sleep(5000);
            }catch(Exception e){
                if(NASUFLogger.isEnabledFor(Level.ERROR))
                    NASUFLogger.error("PlayerDevice: discoverDependentServices:" + 
                        e.toString());
            }
        }
    }
    
    //This method is overridden by devices that have dependen services 
    //that form part of a service composition. Only devices that use
    //depndency services need to override this method implemented in 
    //the abstract parent class.
    public void unregisterService(String serviceName){
        String moduleSpec;
        try{
            //Determine whether the service to be unregistered is an audio
            //service.
            moduleSpec = unregisterService(serviceName, audioServices, audioServicesBin);
            //If a new service has been returned, i.e. the device has a backup
            //service in its collection then this new service must be 
            //automatically run. For example if the surround sound speakers 
            //accociated with a Entertainment system become unavailable for
            //some reason, the DVD player for example may have previously 
            //discovered the TV speaker services and in this case these services
            //will be automatically selected and run. The user will notice a 
            //QoS change, however the idea is that the system can automatically
            //self-adpat and provide a solution.
            if(moduleSpec.length() > 0){
                audioModuleSpec = moduleSpec;
                ((MediaTransmitterService)media_transmitter)
                    .changeAudioService(
                        AdaptionConstants.REGISTER_NEXT_BEST_SERVICE, 
                        audioModuleSpec);
                return;
            }

            //Determine whether the service to be unregistered is a video 
            //service
            moduleSpec = unregisterService(serviceName, videoServices, videoServicesBin);
            //See the above comment for the reasoning behing this code.
            if(moduleSpec.length() > 0){
                videoModuleSpec = moduleSpec;
                ((MediaTransmitterService)media_transmitter)
                    .changeVideoService(
                        AdaptionConstants.REGISTER_NEXT_BEST_SERVICE,
                        videoModuleSpec);
                return;
            }
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("PlayerDevice: unregisterService: " + 
                    e.toString());
        }
    }
    
    //This method is overriden by device subclasses that us dependent 
    //servies as part of a bigger compsition.
    public void registerService(String serviceName){
        String moduleSpec;
        //Try to register the service as an Audio service.
        moduleSpec = registerService(serviceName, audioServices, audioServicesBin);
        //If a new service has been returned, then change the current 
        //service in the composition. If the composition is active then
        //the servie will be automatically executed, else it will just be
        //plugged in ready for use when the composition is next executed.
        if(moduleSpec.length() > 0){
            audioModuleSpec = moduleSpec;
            ((MediaTransmitterService)media_transmitter)
		.changeAudioService(
                    AdaptionConstants.REGISTER_BEST_SERVICE, 
                    audioModuleSpec);
            //Return from the method as we have registered the service.
            return;
        }
        //If you reach this stage this means that the service to be re-registered
        //was not an Audio service. Try to register the service as a Video service.
        moduleSpec = registerService(serviceName, videoServices, videoServicesBin);
        if(moduleSpec.length() > 0){
            videoModuleSpec = moduleSpec;
            //Change the video service. Use the register_best_service 
            //parameter as this is a previously registered service 
            //that has better capabilities than the one currently running
            ((MediaTransmitterService)media_transmitter)
            	.changeVideoService(
                    AdaptionConstants.REGISTER_BEST_SERVICE,
                    videoModuleSpec);
            //Return from the method as we have registered the service.
            return;
        }
    }
    
    //This method is used by the device to register a service. Again
    //this is to signify to devices that use this service as part of 
    //a bigger composition that the service is being offered again.
    public void sendServiceRegistrationMsg(){
        sendServiceRegistrationMsg((IAbstractService)media_transmitter);
    }
    
    //When the device is stopped make sure that all the 
    //objects used by the device are stopped and reset.
    public void stopDevice(){
        sendServiceUnregisteringMsg((IAbstractService)media_transmitter);
        audioServices.clear();
        audioServicesBin.clear();
        videoServices.clear();
        videoServicesBin.clear();
        super.stopDevice();
    }
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        super.cleanUp();
        media_transmitter.cleanUp();
        media_transmitter = null;
        audioServices = null;
        audioServicesBin = null;
        videoServices = null;
        videoServicesBin = null;
    }
}