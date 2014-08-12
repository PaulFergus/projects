/*
 * Class Title:         DeCapPipeMsgListener
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.listener.pipe.impl.decap;

//Java SDK imports
import java.util.ArrayList;
import java.util.List;

//JXTA API imports
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.decap.IDECAPAlgorithm;
import NASUF.src.uk.ac.livjm.constant.DECAPConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.listener.pipe.ServicePipeMsgListener;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;

//Log4J API imports
import org.apache.log4j.Level;
    
public class DeCapPipeMsgListener extends ServicePipeMsgListener{
    
    //This variable contains the decap algorithm used by the
    //decap service.
    private IDECAPAlgorithm algorithm = null;
   
    //Default constructor. This constructor takes one parameter,
    //which is the alogirthm the service should use. Again an
    //interface is passed in so potentially any decap algorithm
    //could be used.
    public DeCapPipeMsgListener(
        IDECAPAlgorithm algorithm) {
        this.algorithm = algorithm;
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger
                .info("DeCaP Service Started - Waiting for Connections");
    }
    
    //This event listener recieves messages from devices that 
    //determines whether the capability parameters described 
    //in the device's capability model match or surpass the 
    //capability requirements defined in the service request.
    //Each parameter has a fitness value and an associated 
    //importance value.
    public void pipeMsgEvent(net.jxta.pipe.PipeMsgEvent pipeMsgEvent) {
        //Creata a new message object to contain the 
        //message contained in the pipeMsgEvent object.
        Message msg = null;
      
        try {
            //grab the message from the event and assign it to our 
            //message object
            msg = pipeMsgEvent.getMessage();
            
            //If the message object is null then just
            //exit the event procedure.
            if (msg == null)
                return;
            
            //Get the service request device capability model
            MessageElement msgSRDCM = 
                msg.getMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    DECAPConstants.SERVICE_REQUEST_DECAP_TAG);
            
            //Get the device capability model
            MessageElement msgDeviceDCM = 
                msg.getMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    DECAPConstants.DEVICE_DECAP_TAG);
            
            //If any of these models are null then exit the 
            //event procedure.
            if ((msgSRDCM == null)
                || (msgDeviceDCM == null)) {
                if(NASUFLogger.isEnabledFor(Level.ERROR))
                    NASUFLogger.error("null msg received");
      
                return;
            }
            
            //These variales contain string representations of 
            //the two capability models.
            String strSRDCM = "" + msgSRDCM.toString();
            String strDeviceDCM = "" + msgDeviceDCM.toString();
            
            //This collection contains the list of capability parameters
            //used by the decap service.
            //Need to fix this 
            List params = new ArrayList();
            params.add("power");
            params.add("bandwidth");
            params.add("memory");
            params.add("cpu_load");
            
            //First check to see if the devic is capable enough 
            //of executing the service as defined in the service
            //request device capability model. Return the results 
            //back to the caller
            pipe.sendMessage((Message)algorithm.checkCapability(
                params, 
                strDeviceDCM.trim(), 
                strSRDCM.trim()));
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("DeCapPipeMessageListener Error: " + 
                    e.toString());
        }
    }
    
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp() {
        super.cleanUp();
        algorithm = null;
    }    
}