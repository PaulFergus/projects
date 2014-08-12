/*
 * Class Title:         AudioProcessor
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.device.impl.audio;

//Java SDK imports
import java.io.File;
import java.io.StringWriter;

//Jxta API imports
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.MimeMediaType;
import net.jxta.impl.protocol.ResolverQuery;
import net.jxta.protocol.ResolverQueryMsg;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.impl.MetadataFiles;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.device.Device;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.service.application_service.ApplicationPeerServiceFactory;
import NASUF.src.uk.ac.livjm.service.abstract_service.IAbstractService;
import NASUF.src.uk.ac.livjm.service.core_service.CoreServiceFactory;
import NASUF.src.uk.ac.livjm.utils.ReadFileToString;

//Log4J API imports
import org.apache.log4j.Level;

public class AudioProcessor extends Device{
    
    //This is the application specific service used to 
    //recieve audio data. The implementation specific
    //details have been abstracted to an interface. This allows
    //any media_reciever implementation to be used as long
    //as it implements the IApplicationPeerService. However,
    //the functionality provided by this interface is generic,
    //consequently this object will need to be downcasted to
    //the required implementation class to access the app.
    //specific methods it provides. This is performed in the 
    //implementation classes for a particular application
    //not in the framework classes.
    private IAbstractService media_reciever;
    
    //Defalt constructor - this constructor takes one parameter
    //which is the port address the audio service recieves data
    //on
    public AudioProcessor(String port) {
        //Call the constructure in the Device abstract class
        super(port);
        //Set the device capability model for this device. this
        //will be used to determine how capable the device is of
        //running the service.
        this.setDCM(MetadataFiles.AUDIO_DCM);
        //Display tha the new audio processor has been created.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Created new Audio Processor");
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
        //a service that recieves media content, whether it
        //be audio or/and Video, as RTP packets. In this instance
        //the media content is audio
        String audioServiceDescription =
            ReadFileToString.getContents(
                new File(MetadataFiles.AUDIO_PSCM));
        
        //Create a new media recieved application specific service
        //capable of processing audio data.
        media_reciever = 
            ApplicationPeerServiceFactory
                .createAudioRecieverService(port, 
                    "Audio", 
                    audioServiceDescription);
        //Add this service to the application specific service 
        //collection
        addApplicationPeerService(media_reciever);
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
            //Display that the core service has been added
            if(NASUFLogger.isEnabledFor(Level.DEBUG))
                NASUFLogger.debug("Added the DECAP Core Service");
            
            //Create and start the DistrES Service. Because this code has been
            //implemented this means that the device hosts its own version
            //of the DistrES Service. Consequently the device does not need to 
            //discover this service remotely
            addCoreService(CoreServiceFactory
                    .createSISMService(disus));
	    
            //Display that the core service has been added
            if(NASUFLogger.isEnabledFor(Level.DEBUG))
                NASUFLogger.debug("Added the SISM Core Service");
            
            //Create and start the SISM Service. Because this code has been
            //implemented this means that the device hosts its own version
            //of the SISM Service. Consequently the device does not need to 
            //discover this service remotely
            addCoreService(CoreServiceFactory
                    .createDistrESService());
	    
            //Display that the core service has been added
            if(NASUFLogger.isEnabledFor(Level.DEBUG))
                NASUFLogger.debug("Added the DistrES Core Service");
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("AudioProcessor: addCoreServices: startCoreServices Error" + 
                    e.toString());
        }
    }
    
    //This method is used by the device to register a service. Again
    //this is to signify to devices that use this service as part of 
    //a bigger composition that the service is being offered again.
    public void sendServiceRegistrationMsg(){
        //Send a service registration message for the 
        //media recieve service.
        sendServiceRegistrationMsg((IAbstractService)media_reciever);
    }
    
    //When the device is stopped make sure that all the 
    //objects used by the device are stopped and reset.
    public void stopDevice(){
        //Send a service unregister message for the 
        //media reciever service.
        sendServiceUnregisteringMsg((IAbstractService)media_reciever);
        //Stop the device.
        super.stopDevice();
    }
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        //Clean the media reciever before it is destroyed, 
        //i.e. close pipes.
        media_reciever.cleanUp();
        //Destroy the service we no longer need it.
        media_reciever = null;
        //Clean up the parent class, i.e. close pipes.
        super.cleanUp();
    }
}