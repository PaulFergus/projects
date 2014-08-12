/*
 * Class Title:         MediaCenter
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.device.impl.controller.mediacenter;

//Jxta API imports
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.ResolverQueryMsg;
import net.jxta.resolver.QueryHandler;
import net.jxta.util.JxtaBiDiPipe;

//NASUF API imports
import NASUF.src.uk.ac.livjm.device.Device;
import NASUF.src.uk.ac.livjm.listener.resolver.ResolverMsgHandlerFactory;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;

//Log4J API imports
import org.apache.log4j.Level;

public class MediaCenter extends Device{
    
    //This class variable contains a reference to the 
    //media center user interface.
    private JMediaCenter jmc = null;
    
    //Default constructor. This constructor takes one parameter
    //which is the media center interface.
    public MediaCenter(JMediaCenter jmc) {
        //This constructor uses a custom listener.
        super(null, (QueryHandler)
            ResolverMsgHandlerFactory
                .createMediaCenterHandler(jmc));
        this.jmc = jmc;
	//Display that a new media center object is being 
	//created.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Creating new Media Center...");
    } 
   
    //This method is called by an event listener. 
    //When a service unregisters itself it sends 
    //a message to any device interested. The media 
    //center controller is interested in recieving 
    //these messages so that the ui can be updated.
    public void unregisterService(String serviceName){
        jmc.unregisterService(serviceName);
    }
    
    //This method is called by an event listener. 
    //When a service registers itself it sends 
    //a message to any device interested. The media 
    //center controller is interested in recieving 
    //these messages so that the ui can be updated.
    public void registerService(String serviceName){
        jmc.registerService(serviceName);
    }
    
    //When the device is stopped make sure that all the 
    //objects used by the device are stopped and reset.
    public void stopDevice(){
        //Stop the device.
        super.stopDevice();
    }
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        //Clean up the parent class, i.e. close pipes.
        super.cleanUp();
    }
}