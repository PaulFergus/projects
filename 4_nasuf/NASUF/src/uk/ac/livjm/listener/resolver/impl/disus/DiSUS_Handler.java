/*
 * Class Title:         DiSUS_Handler
 * Class Description:   Need to do
 * Developer:           Paul Fergus
 * Project:             Networked Appliance Service Utilisation Framework
 * Supervisor:          Prof. M. Merabti, Dr M. B. Hannegham.
 * Organisation:        Liverpool John Moores University
 * Email:               M.Merabti@livjm.ac.uk (Prof. Merabti)
 *                      M.B.Hanneghan@livjm.ac.uk (Dr. Hanneghan)
                        cmppferg@livjm.ac.uk  
 */
package NASUF.src.uk.ac.livjm.listener.resolver.impl.disus;

//Java SDK imports
import java.io.ByteArrayInputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

//JXTA API imports
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import net.jxta.endpoint.Message;
import net.jxta.protocol.ResolverQueryMsg;
import net.jxta.protocol.ResolverResponseMsg;
import net.jxta.resolver.ResolverService;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.DECAPConstants;
import NASUF.src.uk.ac.livjm.constant.DistrESConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceDescriptionConstants;
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.listener.resolver.ResolverHandler;
import NASUF.src.uk.ac.livjm.listener.resolver.ResolverMsgHandlerFactory;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;
import NASUF.src.uk.ac.livjm.p2p.IDiSUS;
import NASUF.src.uk.ac.livjm.protocol.data_objects.DataObjectFactory;
import NASUF.src.uk.ac.livjm.protocol.data_objects.impl.PrimaryServiceDataObject;
import NASUF.src.uk.ac.livjm.service.abstract_service.IAbstractService;

//Log4J API imports
import org.apache.log4j.Level;

public class DiSUS_Handler extends ResolverHandler{
   
    //This variable contains the handler name, which is used
    //to register the type of messages the resolver service
    //can process.
    private String handlerName = "DiSUSMessage";
    
    //Default constructor. This method takes one parameter
    //which is the P2P interface.
    public DiSUS_Handler(IDiSUS disus) {
        this.disus = disus;
        this.drh = this;
    }
    
    //This setter method sets the result recieved from the
    //DiSUS_DECAP_Handler. This is a call-back function. the
    //function takes two parameters - the first parameter 
    //is the original query message and the second is the 
    //message recieved from the DECAP service.
    public void setDECAPResult(
        ResolverQueryMsg rqMsg,
        Message msg){
      
	//This interface object instance contains
	//an abstract service. 
        IAbstractService aps = null;
	
	//This class variable holds the peer service
	//capability model.
        String srPSCM = null;
        
	//This class variable contains the result 
	//returned by the DECAP service.
        String result = "";
       
        //Check to see if the device has the required capabilities
        //before you try to determine whether the device provides
        //the required application service. If the device is not 
        //capable then do not respond to the querying peer.
        result = msg.getMessageElement(
            ServiceDescriptionConstants.NASUF_NAMESPACE,
            DECAPConstants.MAUT_RESULT).toString();
	System.out.println("DECAP Result: " + result);
        //If the device capabilities match the capabilities 
	//described in the service request.
        if(result.equalsIgnoreCase("True")){
            if(NASUFLogger.isEnabledFor(Level.INFO))
                NASUFLogger.info("The DECAP satisfies the SRDECAP. Checking PSCM ....");
            
            //Check to see if the device has any peer services
            if(disus.getApplicationPeerServices().size() > 0){
                Iterator iter = disus.getApplicationPeerServices().iterator();
                String textDoc = rqMsg.getQuery();
		//loop through each peer service and check to see 
		//if the profile matches the PSCM described in the
		//service request.
                while(iter.hasNext()){
                    aps 
                        = (IAbstractService)iter.next();
		    //Extract the profile.
                    String profile
                        = (String)aps
                            .getServiceDescription();
		    
                    try{
			//Convert the profile into a structured document.
                        StructuredTextDocument std = 
                            (StructuredTextDocument)StructuredDocumentFactory
                                .newStructuredDocument(
                                    new MimeMediaType("text/xml"), 
                                        new ByteArrayInputStream(textDoc.getBytes()) );
			//Extract the PSCM file from the document
                        Enumeration srpscm = 
                            std.getChildren(ServiceRequestConstants.SERVICE_REQUEST_PSCM_TAG);
                        while(srpscm.hasMoreElements()){
                            srPSCM = ((TextElement)srpscm.nextElement()).getTextValue();
                            break;
                        }
                        
			//Create a new SISM handler. This will discover an available SISM
			//servic and pass the PSCM along with the current application
			//peer service profile for assessment.
                        new Thread((Runnable)
                            ResolverMsgHandlerFactory
                                .createDiSUS_SISM_Handler(
                                    disus,
                                    msg,
                                    rqMsg, 
                                    srPSCM,
                                    profile,
                                    aps
                                        .getModuleSpecAdvertisement()
                                            .toString()))
                                            .start();
                    }catch(Exception e){
                        if(NASUFLogger.isEnabledFor(Level.ERROR))
                            NASUFLogger.error("setDECAPResult Error: " + 
				e.toString());
                    }
                }
            }
        }
    }
    
    //This method adds the primary and dependent services for this device
    //to a message object before returning it to the sender. It takes
    //three parameters - the first parameter is the decap message, the 
    //second is teh primary service module specification and the 
    //last parameter is a list of dependent service objects.
    private String addServiceDescriptions(
        Message decapMsg,
        String primaryServiceMS,
        List dependentServices){
    
	//Create a new structured document.
        StructuredTextDocument doc = null;
	//Add a message element indicating that the message is a service
	//descriptions message.
        try{
            doc = (StructuredTextDocument)
            StructuredDocumentFactory.newStructuredDocument(
                new MimeMediaType( "text/xml" ), "JXTA-SD:ServiceDescriptions" );
        }catch(Exception ioe){
	    if(NASUFLogger.isEnabledFor(Level.ERROR))
		NASUFLogger.error("DiSUS_Handler: addServiceDescription: " + 
		    ioe.toString());
        }
        
	//Create a new element
        Element e = null;
        
	//Get the device capability score
        String deviceDeCapScore = 
	    decapMsg.getMessageElement(
		ServiceDescriptionConstants
		    .NASUF_NAMESPACE,
		DECAPConstants
		    .DEVICE_DECAP_TAG).toString();
	
	//Create the device capability score element and append
	//it to the message.
        e = doc.createElement("DeviceDECAPScore", deviceDeCapScore);
        doc.appendChild(e);
        
	//Create the primary service element and add it to the
	//message
        e = doc.createElement("PrimaryService", primaryServiceMS);
        doc.appendChild(e);
        
	//If this device has dependency services, add each service
	//to the message
        if(dependentServices != null){
            Iterator iter = dependentServices.iterator();
            PrimaryServiceDataObject ps;
	    //Iterate through each of the dependency services
            while(iter.hasNext()){
                ps = (PrimaryServiceDataObject)iter.next();
		//Create a dependency service element using the 
		//dependency service module specification as a 
		//value
                e = doc.createElement("DependentService", 
                    ps.getModuleSpec());
		//Append the element to the message
                doc.appendChild(e);
            }
        }
	//Return a string representation of the structured document
	//back to the caller.
        return doc.toString();
    }
    
    //This method is called when a result is returned from the SISM 
    //service. It takes four parameters - the first parameter is the 
    //decap message and the second parameter is the original query
    //message. The third parameter is contains the success value, ie.
    //whether the servic was matched or not and the last variable 
    //contains the module specification of the service that was 
    //processed
    public void setSISMResult(
        Message decapMsg,
        ResolverQueryMsg rqMsg,
        String result,
        String moduleSpec){
       
        //First determine whether SISM semantically  matched
        //the current application peer service
        if(result.equalsIgnoreCase("True")){
            
	    //Extract the response message from the query.
            ResolverResponseMsg rrm = rqMsg.makeResponse();
            
            //Set the resolver handler name
            rrm.setHandlerName(handlerName);
            //Make sure that the query ID type is set,
            //i.e. 1 for audio or 2 for video - this may
            //change
            rrm.setQueryId(rqMsg.getQueryId());
            
            //The response value should be the 
            //module spec for the current application
            //peer service.
            rrm.setResponse(
                addServiceDescriptions(decapMsg, moduleSpec, 
                    disus.getDependentServices()));
            
            //Return the module spec to the querying
            //peer.
            disus.sendResolverResponse(rqMsg.getSrc(), rrm);
        }
    }
    
    public void setDistrESResult(
	String result,
	ResolverQueryMsg rqMsg){
	    
	//Extract the response message from the query.
        ResolverResponseMsg rrm = rqMsg.makeResponse();
            
        //Set the resolver handler name
        rrm.setHandlerName(handlerName);
        //Make sure that the query ID type is set,
        //i.e. 1 for audio or 2 for video - this may
        //change
        rrm.setQueryId(rqMsg.getQueryId());
            
        //The response value should be the 
        //module spec for the current application
        //peer service.
        rrm.setResponse(result);
	//Return the module spec to the querying
        //peer.
        disus.sendResolverResponse(rqMsg.getSrc(), rrm);
    }
    //This method is one of the resolver service event listeners. Messages sent
    //by devices within the network are recieved via this event if the message
    //handler name is the same in the message as it is for the type of messages
    //the resolver service for this devices.
    public int processQuery(net.jxta.protocol.ResolverQueryMsg resolverQueryMsg) {
        //Display that the query is being processed.
	if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Processing Query ...");
        
	//Get the query from the resolver query message
        String textDoc = resolverQueryMsg.getQuery();
        
        try{
	    //Convert the query into a structured document
            StructuredTextDocument std = 
                (StructuredTextDocument)StructuredDocumentFactory
                    .newStructuredDocument(
                        new MimeMediaType("text/xml"), 
                            new ByteArrayInputStream(textDoc.getBytes()) );

	    //Extract the request type
            Enumeration enumValue = 
                std.getChildren(ServiceRequestConstants.REQUEST_TYPE);
                
            TextElement el = null;
            while(enumValue.hasMoreElements()){
                el = (TextElement)enumValue.nextElement();
                break;
            }
	    
	    //These variables contain the service request capabiltiy model and the 
	    //device capability model
            String srDCM = "";
            String deviceDCM = "";
            
	    //Check to see if the query is a request to find an application peer
	    //service
            if(el.getTextValue().equalsIgnoreCase("findApplicationPeerService")){
                //Extract the device capability model 
		Enumeration srdcm = std.getChildren(DECAPConstants.SERVICE_REQUEST_DECAP_TAG);
                while(srdcm.hasMoreElements()){
                    srDCM = ((TextElement)srdcm.nextElement()).getTextValue();
                    break;
                }
		//Extrace the device's device capability model
                deviceDCM = disus.getDCM();
		
		//Check that the device satisfies the capabilities to 
		//effectively execute the services is provides before
		//checking whether it has a required service.
                new Thread((Runnable)
                    ResolverMsgHandlerFactory
                        .createDiSUS_DECAP_Handler(
                            disus, 
                            resolverQueryMsg,
                            srDCM, deviceDCM)).start();
		return ResolverService.OK;   
            }
	    else if(el.getTextValue().equalsIgnoreCase("extractConcept")){
		String term = "";
		//Extract the device capability model 
		Enumeration enumEC = std.getChildren(DistrESConstants.X_TERM);
                while(enumEC.hasMoreElements()){
                    term = ((TextElement)enumEC.nextElement()).getTextValue();
                    break;
                }
		
		//Check that the device satisfies the capabilities to 
		//effectively execute the services is provides before
		//checking whether it has a required service.
                new Thread((Runnable)
                    ResolverMsgHandlerFactory
                        .createDiSUS_DistrES_Handler(
                            disus, 
                            resolverQueryMsg,
                            term)).start();
		return ResolverService.OK;   
            }
	    //If the query is to unregister a service then extract the 
	    //module specification and unregister the service in the 
	    //P2P interface.
	    else if(el.getTextValue().equalsIgnoreCase("unregisterService")){
                Enumeration enumNotif = std.getChildren("ModuleSpec");
                while(enumNotif.hasMoreElements()){
		    //Unregister the service.
                    disus.unregisterService(
                        ((TextElement)enumNotif.nextElement())
                            .getTextValue());
                    break;
                }
		return ResolverService.OK;
            }
	    //If the query is to register a service then extract the 
	    //module specification and register the service in the
	    //P2P interface.
	    else if(el.getTextValue().equalsIgnoreCase("registerService")){
                Enumeration enumNotif = std.getChildren("ModuleSpec");
                while(enumNotif.hasMoreElements()){
                    disus.registerService(
                        ((TextElement)enumNotif.nextElement())
                            .getTextValue());
                    break;
                }
		return ResolverService.OK;
            }
	    //If the message could not be processed the repropagate
	    //the message.
	    else{
		return ResolverService.Repropagate;
	    }
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("Process Query Error: " + e.toString());
	    
	    //Return the message to indicate that the message could
	    //not be handler and consequently needs to be repropagated.
            return ResolverService.Repropagate;
        }
    }
    
    //This event processes the response to a query.
    public void processResponse(net.jxta.protocol.ResolverResponseMsg resolverResponseMsg) {
	//Display that the response message is being 
	//processed
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Processing Response ...");
        
	//Set the result in the P2P interface.
	//NOTE: CHANGE THIS METHOD NAME TO setSISMResult
        disus.setDECAPResult(resolverResponseMsg);
    }
}