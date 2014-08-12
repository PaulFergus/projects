/*
 * Class Title:         SISMPipeMsgListener
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.listener.pipe.impl.sism;

//JXTA API imports
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;

//NASUF API imports
import NASUF.src.uk.ac.livjm.algorithm.sism.abstract_matcher.IAbstractMatcher;
import NASUF.src.uk.ac.livjm.algorithm.sism.concrete_matcher.IConcreteMatcher;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.listener.pipe.ServicePipeMsgListener;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;

//Log4J API imports
import org.apache.log4j.Level;

public class SISMPipeMsgListener extends ServicePipeMsgListener{
    
    //These two objects contain the matching algorithms
    //used to perform abstract and concrete matching
    private IAbstractMatcher abstractMatcher = null;
    private IConcreteMatcher concreteMatcher = null;
    
    //Default constructor. This constructor takes two 
    //parameters - the first parameter is the abstract
    //matcher interface and the second is the concrete
    //matcher interface
    public SISMPipeMsgListener(
        IAbstractMatcher abstractMatcher,
        IConcreteMatcher concreteMatcher) {
        //Assign both parameter values to the class 
        //variables
        this.abstractMatcher = abstractMatcher;
        this.concreteMatcher = concreteMatcher;
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("SISM Service Started - Waiting for Connections");
    }
    
    //This event method is used to process abstract and concrete
    //matching requests. 
    public void pipeMsgEvent(net.jxta.pipe.PipeMsgEvent pipeMsgEvent) {
        //Create a new message object that will contain the 
        //message contained in the pipeMsgEvent object
        Message msg = null;
       
        try {
            //Grab the message from the event
            msg = pipeMsgEvent.getMessage();
            
            //If the message is null then exit the event 
            //method
            if (msg == null)
                return;
            
            //Get the peer service capability model from the 
            //service request.
            MessageElement msgSRPSCM = 
                msg.getMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    ServiceRequestConstants.SERVICE_REQUEST_PSCM_TAG);
            
            //Get the peer service profile from the service
            //request.
            MessageElement msgPSProfile = 
                msg.getMessageElement(
                    ServiceDescriptionConstants.NASUF_NAMESPACE, 
                    ServiceRequestConstants.PEER_SERVICE_PROFILE_TAG);
            //Check that neither of these models are null.
            if ((msgSRPSCM == null)
                || (msgPSProfile == null)) {
                if(NASUFLogger.isEnabledFor(Level.INFO))
                    NASUFLogger.info("null msg received");
               
                //Exit the event method
                return;
            }
            //Return the result from the abstract matching
            //algorithm to the requester. Note that the 
            //concrete matcher has not been implemented at
            //this stage. This is the subject of future work
            pipe.sendMessage((Message)abstractMatcher.match(
                msgSRPSCM.toString(), 
                msgPSProfile.toString()));     
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("SISM Pipe Msg Listener Error: " + 
                    e.toString());
            //Exit the event method.
            return;
        }
    }
   
    //Each device must implement the cleanUp method which 
    //must ensure that all the objects used by the device
    //are correctly distroyed.
    public void cleanUp(){
        super.cleanUp();
        abstractMatcher = null;
        concreteMatcher = null;
    }
}