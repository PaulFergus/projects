/*
 * Class Title:         MediaCenter_Handler
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

//Jxta API imports
import net.jxta.document.MimeMediaType;
import net.jxta.resolver.ResolverService;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;

//NASUF API imports
import NASUF.src.uk.ac.livjm.constant.ServiceRequestConstants;
import NASUF.src.uk.ac.livjm.device.impl.controller.mediacenter.JMediaCenter;
import NASUF.src.uk.ac.livjm.listener.resolver.ResolverHandler;
import NASUF.src.uk.ac.livjm.logger.impl.NASUFLogger;

//Log4J API imports
import org.apache.log4j.Level;

public class MediaCenter_Handler extends ResolverHandler{
    
    //This class variable provides access to the media
    //center user interface. Any service changes in 
    //the network the UI is interested in are 
    //given to the UI object.
    private JMediaCenter mc;
    
    //Default constructor. This constructor takes one parameter
    //which is the UI interface.
    public MediaCenter_Handler(JMediaCenter mc){
        this.mc = mc;
    }
    
    //This event method processes any queries relating 
    //to the registering and unregistering of services.
    public int processQuery(net.jxta.protocol.ResolverQueryMsg resolverQueryMsg){
	//Display that the query is being processed.
        if(NASUFLogger.isEnabledFor(Level.INFO))
            NASUFLogger.info("Processing Query ...");
        
	//Extract the query from the message.
        String textDoc = resolverQueryMsg.getQuery();
        
        try{
	    //Convert the query into a structured document.
            StructuredTextDocument std = 
                (StructuredTextDocument)StructuredDocumentFactory
                    .newStructuredDocument(
                        new MimeMediaType("text/xml"), 
                            new ByteArrayInputStream(textDoc.getBytes()) );

	    //Exract the request time value.
            Enumeration enumValue = 
                std.getChildren(ServiceRequestConstants.REQUEST_TYPE);
                
            TextElement el = null;
            while(enumValue.hasMoreElements()){
                el = (TextElement)enumValue.nextElement();
                break;
            }

            //If the request type is unregsiter service then 
	    //instruct the UI to unregister the required service.
            if(el.getTextValue().equalsIgnoreCase("unregisterService")){
                Enumeration enumNotif = std.getChildren("ModuleSpec");
                while(enumNotif.hasMoreElements()){
                    //unregsiter the service in the UI
                    mc.unregisterService(
                        ((TextElement)enumNotif.nextElement())
                            .getTextValue());
                    break;
                }
		//Return that the message was successfully handled.
		return ResolverService.OK;
            }
            
	    //If the request is to register a service then
	    //instruct the UI to register the required service.
	    else if(el.getTextValue().equalsIgnoreCase("registerService")){
                Enumeration enumNotif = std.getChildren("ModuleSpec");
                while(enumNotif.hasMoreElements()){
		    //Register the service in the UI
                    mc.registerService(
                        ((TextElement)enumNotif.nextElement())
                            .getTextValue());
                    break;
                }
		//Return that the message was successfully handled.
		return ResolverService.OK;
            }else
		//If the request was not understood just 
		//repropagate the message.
		return ResolverService.Repropagate;
        }catch(Exception e){
            if(NASUFLogger.isEnabledFor(Level.ERROR))
                NASUFLogger.error("MediaCenter_Handler" + 
		    e.toString());
	    //Return that the message needs to be 
	    //repropagated.
            return ResolverService.Repropagate;
        } 
    }    
    
    //This event handler processes query responses. If the UI
    //sends a service discovery query then any service that 
    //matches in terms of the DCM and PSCM are added to the UI.
    public void processResponse(net.jxta.protocol.ResolverResponseMsg resolverResponseMsg){
        mc.addService(resolverResponseMsg.getResponse());
    }  
}